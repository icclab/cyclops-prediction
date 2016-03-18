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
 * Created on: 28/01/16
 * Description: The POJO class for receiving the response form the UDR Service
 */
public class UdrServiceResponse {
    private Usage usage;


    public static class TimeStamp {
        private String from;
        private String to;

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

    public static class Usage {
        private ArrayList<External> External;

        public ArrayList<External> getExternal() {
            return External;
        }

        public void setExternal(ArrayList<External> external) {
            this.External = external;
        }

        public static class External {
            private String name;
            private ArrayList<String> columns;
            private ArrayList<ArrayList<String>> points;
            private String tags;
            private TimeStamp time;
            private String userid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public ArrayList<String> getColumns() {
                return columns;
            }

            public void setColumns(ArrayList<String> columns) {
                this.columns = columns;
            }

            public ArrayList<ArrayList<String>> getPoints() {
                return points;
            }

            public void setPoints(ArrayList<ArrayList<String>> points) {
                this.points = points;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public TimeStamp getTime() {
                return time;
            }

            public void setTime(TimeStamp time) {
                this.time = time;
            }

        }
    }


    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }


}
