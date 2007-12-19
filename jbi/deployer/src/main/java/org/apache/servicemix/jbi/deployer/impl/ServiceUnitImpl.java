/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicemix.jbi.deployer.impl;

import java.io.File;

import javax.jbi.JBIException;
import javax.jbi.component.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.servicemix.jbi.deployer.ServiceUnit;
import org.apache.servicemix.jbi.deployer.descriptor.ServiceUnitDesc;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


public class ServiceUnitImpl implements ServiceUnit {
	
	private static final Log Logger = LogFactory.getLog(ServiceUnitImpl.class);
	
	private ServiceUnitDesc serviceUnitDesc;
	
	private File rootDir;
	
	private Component component;
	
	public ServiceUnitImpl(ServiceUnitDesc serviceUnitDesc, File rootDir, Component component) {
		this.serviceUnitDesc = serviceUnitDesc;
		this.rootDir = rootDir;
        this.component = component;
    }

	public String getKey() {
		return getComponentName() + "/" + getName();
	}

	public String getName() {
		return serviceUnitDesc.getIdentification().getName();
	}

    public String getDescription() {
        return serviceUnitDesc.getIdentification().getDescription();
    }

    public String getComponentName() {
		return serviceUnitDesc.getTarget().getComponentName();
	}

	public File getRootDir() {
		return rootDir;
	}

    public void deploy() throws JBIException {
        component.getServiceUnitManager().deploy(getName(), getRootDir().getAbsolutePath());
    }

	public void init() throws JBIException {
        component.getServiceUnitManager().init(getName(), getRootDir().getAbsolutePath());
	}
		
	public void shutdown() throws JBIException {
        component.getServiceUnitManager().shutDown(getName());
	}

	public void start() throws JBIException {
        component.getServiceUnitManager().start(getName());
    }

	public void stop() throws JBIException {
        component.getServiceUnitManager().stop(getName());
    }

}
