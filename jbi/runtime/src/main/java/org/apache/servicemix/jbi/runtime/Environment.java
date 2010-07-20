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
package org.apache.servicemix.jbi.runtime;

import java.util.EventObject;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

/**
 */
public interface Environment {

    MBeanServer getMBeanServer();

    Object getTransactionManager();

    InitialContext getNamingContext();

    void manageObject(Object managedObject) throws Exception;

    void unmanageObject(Object managedObject) throws Exception;

    void unmanageNamedObject(ObjectName name) throws Exception;

    ObjectName getManagedObjectName(Object object) throws Exception;

    ObjectName getManagedObjectName(Object object, String customName) throws Exception;

    String getJmxDomainName() throws Exception;

    boolean isManaged(Object managedObject);

    void notify(EventObject event) throws Exception;
}
