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
package org.apache.servicemix.nmr.core;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.servicemix.nmr.api.service.ServiceRegistry;

/**
 * A very basic implementation of a ServiceRegistry that can be
 * inherited to add more specific methods if needed.
 */
public class ServiceRegistryImpl<T> implements ServiceRegistry<T> {

    private Map<T, Map<String, ?>> registry = new ConcurrentHashMap<T, Map<String, ?>>();

    public void register(T service, Map<String, ?> properties) {
        assert service != null : "service should not be null";
        registry.put(service, properties);
    }

    public void unregister(T service, Map<String, ?> properties) {
        assert service != null : "service should not be null";
        registry.remove(service);
    }

    public Set<T> getServices() {
        return registry.keySet();
    }

    public Map<String, ?> getProperties(T service) {
        return registry.get(service);
    }
}
