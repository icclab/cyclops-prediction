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
package ch.icclab.cyclops.load.model;

/**
 * Author: Skoviera
 * Created: 21/01/16
 * Description: Settings for Internal Scheduler
 */
public class InternalSchedulerSettings {

    // These fields correspond with the configuration file
    private String schedulerFrequency;

    //==== Getters and Setters
    public String getSchedulerFrequency() {
        return schedulerFrequency;
    }
    public void setSchedulerFrequency(String schedulerFrequency) {
        this.schedulerFrequency = schedulerFrequency;
    }
}
