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
package org.apache.servicemix.nmr.osgi;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.servicemix.executors.impl.ExecutorConfig;
import org.apache.servicemix.executors.impl.ExecutorFactoryImpl;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * A managed service that will update the configurations based on the ConfigAdmin configuration
 */
public class ExecutorConfigurator implements ManagedService {

    private ExecutorFactoryImpl factory;

    public ExecutorFactoryImpl getFactory() {
        return factory;
    }

    public void setFactory(ExecutorFactoryImpl factory) {
        this.factory = factory;
    }

    public void updated(Dictionary properties) throws ConfigurationException {
        ExecutorConfig defaultConfig = new ExecutorConfig();
        Map<String, ExecutorConfig> configs = new HashMap<String, ExecutorConfig>();
        for (Enumeration e = properties.keys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            if (key.endsWith(".corePoolSize")) {
                getConfig(configs, key).setCorePoolSize(getInt(properties, key));
            } else if (key.endsWith(".maximumPoolSize")) {
                getConfig(configs, key).setMaximumPoolSize(getInt(properties, key));
            } else if (key.endsWith(".keepAliveTime")) {
                getConfig(configs, key).setKeepAliveTime(getLong(properties, key));
            } else if (key.endsWith(".threadDaemon")) {
                getConfig(configs, key).setThreadDaemon(getBool(properties, key));
            } else if (key.endsWith(".threadPriority")) {
                getConfig(configs, key).setThreadPriority(getInt(properties, key));
            } else if (key.endsWith(".queueSize")) {
                getConfig(configs, key).setQueueSize(getInt(properties, key));
            } else if (key.endsWith(".shutdownDelay")) {
                getConfig(configs, key).setShutdownDelay(getLong(properties, key));
            } else if (key.endsWith(".allowCoreThreadsTimeout")) {
                getConfig(configs, key).setAllowCoreThreadsTimeout(getBool(properties, key));
            } else if (key.endsWith(".bypassIfSynchronous")) {
                getConfig(configs, key).setBypassIfSynchronous(getBool(properties, key));
            } else if (key.equals("corePoolSize")) {
                defaultConfig.setCorePoolSize(getInt(properties, key));
            } else if (key.equals("maximumPoolSize")) {
                defaultConfig.setMaximumPoolSize(getInt(properties, key));
            } else if (key.equals("keepAliveTime")) {
                defaultConfig.setKeepAliveTime(getLong(properties, key));
            } else if (key.equals("threadDaemon")) {
                defaultConfig.setThreadDaemon(getBool(properties, key));
            } else if (key.equals("threadPriority")) {
                defaultConfig.setThreadPriority(getInt(properties, key));
            } else if (key.equals("queueSize")) {
                defaultConfig.setQueueSize(getInt(properties, key));
            } else if (key.equals("shutdownDelay")) {
                defaultConfig.setShutdownDelay(getLong(properties, key));
            } else if (key.equals("allowCoreThreadsTimeout")) {
                defaultConfig.setAllowCoreThreadsTimeout(getBool(properties, key));
            } else if (key.equals("bypassIfSynchronous")) {
                defaultConfig.setBypassIfSynchronous(getBool(properties, key));
            }
        }
        factory.setDefaultConfig(defaultConfig);
        factory.setConfigs(configs);
    }

    private ExecutorConfig getConfig(Map<String, ExecutorConfig> configs, String key) {
        String name = key.substring(0, key.lastIndexOf('.'));
        ExecutorConfig config = configs.get(name);
        if (config == null) {
            config = new ExecutorConfig();
            configs.put(name, config);
        }
        return config;
    }

    private int getInt(Dictionary properties, String key) {
        return Integer.parseInt(properties.get(key).toString());
    }

    private long getLong(Dictionary properties, String key) {
        return Long.parseLong(properties.get(key).toString());
    }

    private boolean getBool(Dictionary properties, String key) {
        return Boolean.parseBoolean(properties.get(key).toString());
    }
}
