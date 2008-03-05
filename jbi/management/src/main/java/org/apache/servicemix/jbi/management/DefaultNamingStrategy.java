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
package org.apache.servicemix.jbi.management;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

/**
 */
public class DefaultNamingStrategy implements NamingStrategy {

    private String jmxDomainName;

    public String getJmxDomainName() {
        return jmxDomainName;
    }

    public void setJmxDomainName(String jmxDomainName) {
        this.jmxDomainName = jmxDomainName;
    }

    public ObjectName getObjectName(ManagedSharedLibrary sharedLibrary) throws MalformedObjectNameException {
        return new ObjectName(jmxDomainName + ":" +
                                    "Type=SharedLibrary," +
                                    "Name=" + sanitize(sharedLibrary.getName()) + "," +
                                    "Version=" + sanitize(sharedLibrary.getVersion()));
    }

    public ObjectName getObjectName(ManagedComponent component) throws MalformedObjectNameException {
        return new ObjectName(jmxDomainName + ":" +
                                    "Type=Component," +
                                    "Name=" + sanitize(component.getName()) + "," +
                                    "SubType=LifeCycle");
    }

    public ObjectName getObjectName(ManagedServiceAssembly serviceAssembly) throws MalformedObjectNameException {
        return new ObjectName(jmxDomainName + ":" +
                                    "Type=ServiceAssembly," +
                                    "Name=" + sanitize(serviceAssembly.getName()));
    }

    private String sanitize(String in) {
        String result = null;
        if (in != null) {
            result = in.replace(':', '_');
            result = result.replace('/', '_');
            result = result.replace('\\', '_');
            result = result.replace('?', '_');
            result = result.replace('=', '_');
            result = result.replace(',', '_');
        }
        return result;
    }

}

