/*
 * Copyright (c) 2015. Zuercher Hochschule fuer Angewandte Wissenschaften
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

package ch.icclab.cyclops.client;

import ch.icclab.cyclops.load.Loader;
import ch.icclab.cyclops.model.PredictionResponse;
import ch.icclab.cyclops.model.UdrServiceResponse;
import ch.icclab.cyclops.prediction.LinearRegressionPredict;
import ch.icclab.cyclops.prediction.RandomForestRegressionPredict;
import ch.icclab.cyclops.util.Time;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: Oleksii
 * Created on: 21/01/16
 * Description: The UDR client class for connecting to the CYCLOPS UDR Service
 */
public class UdrServiceClient extends ClientResource {
    final static Logger logger = LogManager.getLogger(UdrServiceClient.class.getName());

    private String url = Loader.getSettings().getUdrServiceSettings().getUdrServiceUrl();

    /**
     * Connects to the UDR Service and requests for the CDRs for a user between a time period
     *
     * @param from   String
     * @param to     String
     * @param userId String
     * @param resourceId String
     * @return String
     */
    public String getUserUsageData(String userId, String resourceId, Integer from, Integer to) {
        logger.trace("BEGIN UserUsage getUserUsageData(String userId, String resourceId, Integer from, Integer to) throws IOException");
        logger.trace("DATA UserUsage getUserUsageData...: user=" + userId);
        Gson gson = new Gson();
        RandomForestRegressionPredict predict = new RandomForestRegressionPredict();
        //parse dates
        DateTime now = new DateTime(DateTimeZone.UTC);
        Long time_to = now.plusDays(to).getMillis();
        String time_string_from = now.minusDays(from).toString("yyyy-MM-dd'T'HH:mm:ss'Z'");
        ArrayList<Double> list_of_points = Time.makeListOfTIme(now, time_to, to);

        ClientResource resource = new ClientResource(url + "/usage/users/" + userId);
        resource.getReference().addQueryParameter("from", time_string_from);
        logger.trace("DATA UserUsage getUserUsageData...: url=" + resource.toString());
        resource.get(MediaType.APPLICATION_JSON);
        Representation output = resource.getResponseEntity();
        PredictionResponse result = new PredictionResponse();
        try {
            JSONObject resultArray = new JSONObject(output.getText());
            logger.trace("DATA UserUsage getUserUsageData...: output=" + resultArray.toString());
            logger.trace("DATA UserUsage getUsageUsageData...: resultArray=" + resultArray);
            UdrServiceResponse usageDataRecords = gson.fromJson(resultArray.toString(), UdrServiceResponse.class);
            logger.trace("DATA UserUsage getUserUsageData...: userUsageData=" + usageDataRecords);
            result = predict.predict(usageDataRecords, resourceId, list_of_points);
            // Fit "from" and "to" fields
            result.setFrom(time_string_from);
            result.setTo(Time.MillsToString(time_to.doubleValue()));
            logger.trace("DATA UserUsage getUserUsageData...: userUsageData=" + gson.toJson(result));


        } catch (JSONException e) {
            e.printStackTrace();
            logger.error("EXCEPTION JSONEXCEPTION UserUsage getUserUsageData...");
        } catch (IOException e) {
            logger.error("EXCEPTION IOEXCEPTION UserUsage getUserUsageData...");
            e.printStackTrace();
        }

        return gson.toJson(result);
    }
}

