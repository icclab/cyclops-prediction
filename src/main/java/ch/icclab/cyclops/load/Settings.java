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
package ch.icclab.cyclops.load;

import ch.icclab.cyclops.load.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Properties;

/**
 * Author: Skoviera
 * Created: 21/01/16
 * Description: Parent for specific environmental settings
 * Upgraded by: Oleksii
 * Upgraded on: 01/02/16
 */
public class Settings {

    final static Logger logger = LogManager.getLogger(Settings.class.getName());

    // Object for reading and accessing configuration properties
    private Properties properties;

    // List of different settings that are being loaded from configuration file
    protected InternalSchedulerSettings internalSchedulerSettings;
    protected UdrServiceSettings udrServiceSettings;

    /**
     * Load settings based on provided settings
     */
    public Settings(Properties prop) {
        properties = prop;
    }

    //========== Loading configuration file settings

    /**
     * Internally load internalSchedulerSettings options from configuration file
     * @return internalSchedulerSettings options
     */
    private InternalSchedulerSettings loadInternalSchedulerSettings() {
        InternalSchedulerSettings schedulerSettings = new InternalSchedulerSettings();

        // add here other fields you want to track
        schedulerSettings.setSchedulerFrequency(properties.getProperty("SchedulerFrequency"));

        return schedulerSettings;
    }

    private UdrServiceSettings loadUdrServiceSettings() {
        UdrServiceSettings udrServiceSettings = new UdrServiceSettings();

        // add here other fields you want to track
        udrServiceSettings.setUdrServiceUrl(properties.getProperty("RcServiceUrl"));

        return udrServiceSettings;
    }

    /**
     * Access loaded influxDBCredentials from configuration file
     * @return cached influxDBCredentials
     */
    public InternalSchedulerSettings getInternalSchedulerSettings() {

        if (internalSchedulerSettings == null) {
            try {
                internalSchedulerSettings = loadInternalSchedulerSettings();
            } catch (Exception e) {
                logger.error("Could not load InternalSchedulerSettings from configuration file: " + e.getMessage());
            }
        }

        return internalSchedulerSettings;
    }
    public UdrServiceSettings getUdrServiceSettings() {

        if (udrServiceSettings == null) {
            try {
                udrServiceSettings= loadUdrServiceSettings();
            } catch (Exception e) {
                logger.error("Could not load UdrServiceSettings from configuration file: " + e.getMessage());
            }
        }

        return udrServiceSettings;
    }

}
