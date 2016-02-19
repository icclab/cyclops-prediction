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
import java.util.Collections;
import java.util.List;
import ch.icclab.cyclops.model.PredictionResponse;
import ch.icclab.cyclops.model.UdrServiceResponse;
import ch.icclab.cyclops.util.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.regression.RandomForestRegressor;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.RandomForestRegressionModel;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

/**
 * Author: Oleksii
 * Created: 25/01/16
 * Description: Linear regression implemented here
 */
public class RandomForestRegressionPredict extends RegressionPredict {

    final static Logger logger = LogManager.getLogger(LinearRegressionPredict.class.getName());

    /**
     * Will create a response with predicted usages
     *
     * @param usages     incoming information
     * @param resourceId name of resources
     * @param points     list of times
     * @return response with predicted usages
     */
    public PredictionResponse predict(UdrServiceResponse usages, String resourceId, ArrayList<Double> points) {

        //create a list with valid data
        ArrayList<ArrayList<String>> resource_data = getListOfResourceData(usages, resourceId);
        List<LabeledPoint> valid_data = getValidData(resource_data);
        PredictionResponse response = new PredictionResponse();
        if (valid_data.isEmpty()) {
            logger.error("resource " + resourceId + " is not available for current user");
        } else {
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
            DataFrame training = sqlContext.createDataFrame(jsc.parallelize(valid_data), LabeledPoint.class);
            RandomForestRegressor lr = new RandomForestRegressor();

            logger.trace("Create Linear regression model");
            // Fit the model
            RandomForestRegressionModel lrModel = lr.fit(training);
            logger.trace("Train Linear regression model");
            List<Double> values = new ArrayList<Double>(Collections.nCopies(points.size(), 0.0));
            List<LabeledPoint> predictedData = fillDataToPredict(points, values);
            DataFrame predictions = sqlContext.createDataFrame(jsc.parallelize(predictedData), LabeledPoint.class);
            //Predict data
            logger.trace("Predict values for defined features " + points + "transform()");
            DataFrame results = lrModel.transform(predictions);
            response = makePredictObject(results, resource_data, resourceId);
            logger.trace("Get predicted usages usages =" + response);
            logger.trace("Stop SQLContext()");
            jsc.stop();
        }

        return response;
    }
}