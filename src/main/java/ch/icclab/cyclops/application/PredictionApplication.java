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

package ch.icclab.cyclops.application;

import ch.icclab.cyclops.endpoint.*;
import ch.icclab.cyclops.endpoint.Error;
import ch.icclab.cyclops.load.Loader;
import ch.icclab.cyclops.load.Settings;
import ch.icclab.cyclops.schedule.Scheduler;
import ch.icclab.cyclops.schedule.runner.TemplateRunner;
import ch.icclab.cyclops.util.APICallCounter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import java.util.concurrent.TimeUnit;

/**
 * Author: Oleksii
 * Created: 25/01/16
 * Description: Application class that acts as router to service endpoints
 */
public class PredictionApplication extends Application {

    final static Logger logger = LogManager.getLogger(PredictionApplication.class.getName());

    // Router for registering api endpoints
    private Router router;

    // Settings from configuration file
    Settings settings;

    // API counter
    APICallCounter counter;

    //============== Add your implementation here
    /**
     * This method is being called when initialisation process finishes successfully
     */
    private void implementHere() {
        // add your own endpoints
        attachCustomEndpoints();

        // add your own scheduled tasks
        addScheduledTasks();
    }

    /**
     * Implement here your own endpoints you want to expose and track
     */
    private void attachCustomEndpoints() {
        //attach endpoint to router
        router.attach("/prediction/{userid}/{resourceid}", Prediction.class);

        // track API calls for this endpoint
        counter.registerEndpoint("/prediction");
    }

    /**
     * Implement here your own schedulers
     */
    private void addScheduledTasks() {
        Scheduler scheduler = Scheduler.getInstance();

        try {
            // get frequency
            Integer frequency = Integer.parseInt(Loader.getSettings().getInternalSchedulerSettings().getSchedulerFrequency());

            // add schedulers
            scheduler.addRunner(new TemplateRunner(), 0, frequency, TimeUnit.SECONDS);

            // start them up
            scheduler.start();

        } catch (Exception ignored) {
            logger.error("Couldn't start scheduler: " + ignored.getMessage());
        }
    }

    //============== Initialisation and default routes
    /**
     * Construct application by accessing context, creating router and counter, as well as loading settings
     * @return whether initialisation was successful or not
     */
    private Boolean initialiseAndLoadSettings () {
        logger.trace("Initialising Prediction microservice application");

        Context context = getContext();
        router = new Router(context);
        counter = APICallCounter.getInstance();

        // Loader and Settings
        logger.trace("Going to load configuration file and access settings");
        Loader.createInstance(context);
        settings = Loader.getSettings();

        if (settings == null) {
            logger.trace("Initialisation failed due to problem of loading configuration settings");
            return false;
        } else {
            logger.trace("Predict microservice successfully initialised");

            return true;
        }
    }

    /**
     * This method handles the incoming request and routes it to the appropriate resource class
     */
    public Restlet createInboundRoot(){

        // let's start by initialising and loading configuration settings
        Boolean status = initialiseAndLoadSettings();

        logger.trace("Creating routes for Prediction microservice");

        // in case that everything went well
        if (status) {
            // following endpoints are available
            router.attach("", Root.class);
            router.attach("/", Root.class);
            router.attach("/status", Status.class);

            // start counting requests for individual endpoints
            counter.registerEndpoint("/");
            counter.registerEndpoint("/status");

            // call your implementation of additional endpoints, schedulers, rabbitmqs
            implementHere();

            logger.trace("Routes for Prediction microservice successfully created");
        } else {
            router.attach("", Error.class);
            router.attach("/", Error.class).setMatchingMode(Template.MODE_STARTS_WITH);
        }

        return router;
    }
}
