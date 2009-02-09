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
package org.apache.servicemix.jbi.task;

import org.apache.servicemix.jbi.management.AdminCommandsServiceMBean;
import org.apache.tools.ant.BuildException;

/**
 * Start a Component
 * 
 * @version $Revision$
 */
public class StartComponentTask extends JbiTask {
    
    private String name;

    /**
     * @return Returns the component name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The componentName to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * execute the task
     * 
     * @throws BuildException
     */
    public void doExecute(AdminCommandsServiceMBean acs) throws Exception {
        if (name == null) {
            throw new BuildException("null componentName");
        }
        acs.startComponent(name);
    }
    
}