package ch.icclab.cyclops.prediction;

import ch.icclab.cyclops.model.PredictionResponse;
import ch.icclab.cyclops.model.UdrServiceResponse;
import ch.icclab.cyclops.util.Time;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lexxito on 17.02.16.
 */
public class RegressionPredict {
    public static ArrayList<ArrayList<String>> getListOfResourceData(UdrServiceResponse usages, String resourceId) {
        //get external values
        ArrayList<UdrServiceResponse.Usage.External> externalValues = usages.getUsage().getExternal();
        //get resource number
        Integer num = null;
        for(int i = 0; i < externalValues.size(); ++i) {
            String name = externalValues.get(i).getName();
            if (name.equals(resourceId)) {
                num = i;
                break;
            }
        }
        //get all usages for defined resource
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        if (num == null){
            //logger.error("resource "+ resourceId+ " is not available for current user");
        }
        else {
            data = usages.getUsage().getExternal().get(num).getPoints();
        }
        return data;
    }


    /**
     * Return a valid data to train a model
     */
    public static List<LabeledPoint> getValidData(ArrayList<ArrayList<String>> data) {
        ArrayList<Double> listOfPoints = new ArrayList<Double>();
        ArrayList<Double> listOfValues = new ArrayList<Double>();

        //create a list of labled date for training
        try {
            for (ArrayList<String> pointOfData : data) {
                listOfPoints.add(Time.StringToDouble(pointOfData.get(0)));
                listOfValues.add(Double.parseDouble(pointOfData.get(1)));
            }
        }
        catch (Exception e){
            for (ArrayList<String> pointOfData : data) {
                listOfPoints.add(Double.parseDouble(pointOfData.get(0)));
                listOfValues.add(Double.parseDouble(pointOfData.get(1)));
            }
        }

        return fillDataToPredict(listOfPoints, listOfValues);
    }

    /**
     * Transform list of time poins and values into the labled data
     */
    public static List<LabeledPoint> fillDataToPredict(ArrayList<Double> listOfPoints, List<Double> listOfValues) {
        List<LabeledPoint> listOfData = new ArrayList<LabeledPoint>();
        for (int i =0; i < listOfPoints.size(); i++ )
            listOfData.add(new LabeledPoint(listOfValues.get(i),
                            Vectors.dense(0.0, listOfPoints.get(i))
                    )
            );
        return listOfData;
    }

    /**
     * Fit response with predicted data
     */
    public static PredictionResponse makePredictObject (DataFrame results, ArrayList<ArrayList<String>> data, String resourceId) {
        PredictionResponse response = new PredictionResponse();
        ArrayList<PredictionResponse.Usage> usages = new ArrayList<PredictionResponse.Usage>();
        for (ArrayList<String> pointOfData : data) {
            PredictionResponse.Usage usage = new PredictionResponse.Usage();
            usage.setLabel(resourceId);
            try{
                usage.setTime(Time.MillsToString(Double.parseDouble(pointOfData.get(0))));
            }catch (Exception e){
                usage.setTime(pointOfData.get(0));
            }
            usage.setUsage(Double.parseDouble(pointOfData.get(1)));
            usages.add(usage);
        }

        for (Row r : results.select("features", "prediction").collect()) {
            PredictionResponse.Usage usage = new PredictionResponse.Usage();
            Vector denses =  r.getAs(0);
            usage.setLabel("predicted."+resourceId);
            usage.setTime(Time.MillsToString(denses.toArray()[1]));
            usage.setUsage((Double) r.get(1));
            usages.add(usage);
        }
        response.setUsages(usages);
        return response;
    }
}

