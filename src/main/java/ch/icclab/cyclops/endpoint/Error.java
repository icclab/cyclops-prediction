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
package ch.icclab.cyclops.endpoint;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Author: Skoviera
 * Created: 21/01/16
 * Description: Endpoint for informing that initialisation process failed
 */
public class Error extends ServerResource {

    @Get
    public String root(){
        return "Microservice could not be initialised properly and therefore will not function as expected. Please examine log files and configuration settings.";
    }
}
