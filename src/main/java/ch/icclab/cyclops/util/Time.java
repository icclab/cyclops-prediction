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
package ch.icclab.cyclops.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Author: Oleksii
 * Created: 25/01/16
 * Description: Time methods implemented here
 */
public class    Time {
    /**
     * Will create a List of time points points to predict
     *
     * @param time as date
     * @param millis_to as long
     * @param to as int
     * @return ListOfPoints as list of times
     */
    public static ArrayList<Double> makeListOfTIme(DateTime time, Long millis_to, Integer to) {
        Double millis_now = (double) time.getMillis();
        ArrayList<Double> ListOfPoints = new ArrayList<Double>();
        Double value = millis_now;
        ListOfPoints.add(value);
        Double step = (millis_to - millis_now)/to;
        for(int i = 0; i < to; ++i) {
            value += step;
            ListOfPoints.add(value);
            }
        return ListOfPoints;
    }

    /**
     * Will transform TimeDate into the right format
     *
     * @param mills as long
     * @return String with right format
     */
    public static String MillsToString (Double mills) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(mills.longValue()));
    }

    public static String MillsToStringMilis (Double mills) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date(mills.longValue()));
    }

    public static Double StringToDouble (String date) {
            DateTime new_date = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parseDateTime(date);
        return (double) new_date.getMillis();
    }
}
