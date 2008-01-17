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

import javax.jbi.JBIException;
import javax.jbi.component.ComponentContext;
import javax.jbi.component.ComponentLifeCycle;
import javax.jbi.component.ServiceUnitManager;
import javax.jbi.management.LifeCycleMBean;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.servicedesc.ServiceEndpoint;
import javax.management.ObjectName;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;

import org.apache.servicemix.jbi.deployer.Component;
import org.apache.servicemix.jbi.deployer.descriptor.ComponentDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Jan 3, 2008
 * Time: 5:15:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentImpl implements Component {

    private static final Log LOGGER = LogFactory.getLog(ComponentImpl.class);

    private static final String STATE = "state";

    protected enum State {
        Unknown,
        Initialized,
        Started,
        Stopped,
        Shutdown,
    }

    private ComponentDesc componentDesc;
    private javax.jbi.component.Component component;
    private State state = State.Unknown;
    private Preferences prefs;
    private State runningState;

    public ComponentImpl(ComponentDesc componentDesc,
                         javax.jbi.component.Component component,
                         Preferences prefs,
                         boolean autoStart) {
        this.componentDesc = componentDesc;
        this.component = new ComponentWrapper(component);
        this.prefs = prefs;
        this.runningState = State.valueOf(this.prefs.get(STATE, (autoStart ? State.Started : State.Initialized).name()));
    }

    public String getName() {
        return componentDesc.getIdentification().getName();
    }

    public String getDescription() {
        return componentDesc.getIdentification().getDescription();
    }

    public ObjectName getExtensionMBeanName() throws JBIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public javax.jbi.component.Component getComponent() {
        return component;
    }

    public void start() throws JBIException {
        component.getLifeCycle().start();
        state = State.Started;
        saveState();
    }

    public void stop() throws JBIException {
        if (state == State.Started) {
            component.getLifeCycle().stop();
            state = State.Stopped;
            saveState();
        }
    }

    public void shutDown() throws JBIException {
        if (state == State.Started) {
            stop();
        }
        if (state == State.Stopped) {
            component.getLifeCycle().shutDown();
            state = State.Shutdown;
            saveState();
        }
    }

    private void saveState() {
        this.prefs.put(STATE, state.name());
        try {
            this.prefs.flush();
        } catch (BackingStoreException e) {
            LOGGER.warn("Unable to persist state", e);
        }
    }

    public String getCurrentState() {
        switch (state) {
            case Started:
                return LifeCycleMBean.STARTED;
            case Stopped:
                return LifeCycleMBean.STOPPED;
            case Initialized:
            case Shutdown:
                return LifeCycleMBean.SHUTDOWN;
            default:
                return LifeCycleMBean.UNKNOWN;
        }
    }

    protected class ComponentWrapper implements javax.jbi.component.Component, ComponentLifeCycle {
        private javax.jbi.component.Component component;
        private ComponentLifeCycle lifeCycle;

        public ComponentWrapper(javax.jbi.component.Component component) {
            this.component = component;
        }

        public ComponentLifeCycle getLifeCycle() {
            if (lifeCycle == null) {
                lifeCycle = component.getLifeCycle();
            }
            return this;
        }

        public ServiceUnitManager getServiceUnitManager() {
            return component.getServiceUnitManager();
        }

        public Document getServiceDescription(ServiceEndpoint endpoint) {
            return component.getServiceDescription(endpoint);
        }

        public boolean isExchangeWithConsumerOkay(ServiceEndpoint endpoint, MessageExchange exchange) {
            return component.isExchangeWithConsumerOkay(endpoint, exchange);
        }

        public boolean isExchangeWithProviderOkay(ServiceEndpoint endpoint, MessageExchange exchange) {
            return component.isExchangeWithProviderOkay(endpoint, exchange);
        }

        public ServiceEndpoint resolveEndpointReference(DocumentFragment epr) {
            return component.resolveEndpointReference(epr);
        }

        public ObjectName getExtensionMBeanName() {
            return lifeCycle.getExtensionMBeanName();
        }

        public void init(ComponentContext context) throws JBIException {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(lifeCycle.getClass().getClassLoader());
                lifeCycle.init(context);
                state = State.Initialized;
                if (runningState == State.Started) {
                    start();
                    state = State.Started;
                } else if (runningState == State.Stopped) {
                    start();
                    state = State.Started;
                    stop();
                    state = State.Stopped;
                } else if (runningState == State.Shutdown) {
                    shutDown();
                    state = State.Shutdown;
                }
            } finally {
                Thread.currentThread().setContextClassLoader(cl);
            }
        }

        public void shutDown() throws JBIException {
            lifeCycle.shutDown();
        }

        public void start() throws JBIException {
            lifeCycle.start();
        }

        public void stop() throws JBIException {
            lifeCycle.stop();
        }
    }

}
