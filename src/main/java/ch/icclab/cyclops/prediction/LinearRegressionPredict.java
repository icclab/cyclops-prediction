/*
 * Copyright (c) 2016. Zuercher Hochschule fuer Angewandte Wissenschaften
 *  All Rights Reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License"); you may
 *     not use this file except in compliance with the License. You may obtain
 *     a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *     WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *     License for the specific language governing permissions and limitations
 *     under the License.
 */
package ch.icclab.cyclops.prediction;

import java.util.ArrayList;
import java.util.List;
import ch.icclab.cyclops.model.PredictionResponse;
import ch.icclab.cyclops.model.UdrServiceResponse;
import ch.icclab.cyclops.util.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

/**
 * Author: Oleksii
 * Created: 25/01/16
 * Description: Linear regression implemented here
 */
public class LinearRegressionPredict {

    final static Logger logger = LogManager.getLogger(LinearRegressionPredict.class.getName());
    /**
     * Will create a response with predicted usages
     *
     * @param usages incoming information
     * @param resourceId name of resource)
     * @param points list of times
     * @return response with predicted usages
     */
    public PredictionResponse Predict(UdrServiceResponse usages, String resourceId, ArrayList<Long> points) {

        //create a list with valid data
        List<LabeledPoint> data = GetValidData(usages, resourceId);
        PredictionResponse response = new PredictionResponse();
        if (data.isEmpty()){
            logger.error("resource "+ resourceId+ " is not available for current user");
        }
        else{
        //define spark configurations
        logger.trace("Set up spark configuration");
        SparkConf conf = new SparkConf()
                .setAppName("LinearRegression")
                .setMaster("local")
                .set("spark.driver.allowMultipleContexts", "true");
        //create spark context
        JavaSparkContext jsc = new JavaSparkContext(conf);
        logger.trace("Start spark contextS SQLContext()");
        SQLContext sqlContext = new SQLContext(jsc);
        //Create linear regression conf
        logger.trace("Create training data sqlContext.createDataFrame()");
        DataFrame training = sqlContext.createDataFrame(jsc.parallelize(data), LabeledPoint.class);
        LinearRegression lr = new LinearRegression()
                .setMaxIter(10)
                .setRegParam(0.3)
                .setElasticNetParam(0.8);
        logger.trace("Create Linear regression model");
        // Fit the model
        LinearRegressionModel lrModel = lr.fit(training);
        logger.trace("Train Linear regression model");
        List<LabeledPoint> predicted_data =  FillDataToPredict(points);
        DataFrame predictions = sqlContext.createDataFrame(jsc.parallelize(predicted_data), LabeledPoint.class);
        //Predict data
        logger.trace("Predict values for defined features " + points +"transform()");
        DataFrame results = lrModel.transform(predictions);
        response = MakePredictObject(results, resourceId, points);
        logger.trace("Get predicted usages usages ="+ response);
        logger.trace("Stop SQLContext()");
        jsc.stop();
        }

        return response;
    }

    /**
     * Return a valid data to train a model
     */
    public static List<LabeledPoint> GetValidData(UdrServiceResponse usages, String resourceId) {
        //get external values
        ArrayList<UdrServiceResponse.Usage.External> external_values = usages.getUsage().getExternal();
        //get resource number
        Integer num = null;
        for(int i = 0; i < external_values.size(); ++i) {
            String name = external_values.get(i).getName();
            if (name.equals(resourceId)) {
                num = i;
                break;
            }
        }
        //get all usages for defined resource
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        if (num == null){
            logger.error("resource "+ resourceId+ " is not available for current user");
        }
        else {
            data = usages.getUsage().getExternal().get(num).getPoints();
        }
        ArrayList<LabeledPoint> list_data = new ArrayList<LabeledPoint>();
        //create a list of labled date for training
        for (ArrayList<String> aData : data)
            list_data.add(new LabeledPoint
                    (Double.parseDouble(aData.get(1)), Vectors.dense(0.0, Double.parseDouble(aData.get(0)))));

        return list_data;
    }

    /**
     * Will create a list with labled data
     */
    public static List<LabeledPoint> FillDataToPredict(ArrayList<Long> list_of_points) {
        List<LabeledPoint> list_data = new ArrayList<LabeledPoint>();
        for (Long list_of_point : list_of_points)
            list_data.add(new LabeledPoint(0.0, Vectors.dense(0.0, list_of_point)));

        return list_data;
    }

    /**
     * Fit response with predicted data
     */
    /* FIXME: Time could be got from r[1](Vector.dense), for now hardcoded  */
    public static PredictionResponse MakePredictObject (DataFrame results, String resourceId, ArrayList<Long> points) {
        PredictionResponse response = new PredictionResponse();
        int i = 0;
        ArrayList<PredictionResponse.Usage> usages = new ArrayList<PredictionResponse.Usage>();
            for (Row r : results.select("features", "prediction").collect()) {
                PredictionResponse.Usage usage = new PredictionResponse.Usage();
                usage.setLabel(resourceId);
                usage.setTime(Time.MillsToString(points.get(i)));
                usage.setUsage((Double) r.get(1));
                usages.add(usage);
                i ++;
            }
        response.setUsages(usages);
            return response;
    }
}