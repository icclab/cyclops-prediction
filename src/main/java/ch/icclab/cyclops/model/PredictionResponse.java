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

package ch.icclab.cyclops.model;

import java.util.ArrayList;


/**
 * Author: Oleksii
 * Created on: 01/02/16
 * Description: The POJO class for Prediction response
 */
public class PredictionResponse {
    private String from;
    private String to;
    private ArrayList<Usage> usages;

    public static class Usage {
        private String time;
        private Double usage;
        private String label;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Double getUsage() {
            return usage;
        }

        public void setUsage(Double usage) {
            this.usage = usage;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

    }

    public ArrayList<Usage> getUsages() {
        return usages;
    }

    public void setUsages(ArrayList<Usage> usages) {
        this.usages = usages;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}