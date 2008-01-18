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
package org.apache.servicemix.jbi.deployer.handler;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.servicemix.jbi.deployer.impl.FileUtil;
import org.apache.servicemix.kernel.filemonitor.DeploymentListener;


public class JBIDeploymentListener implements DeploymentListener {
	
	private static final Log Logger = LogFactory.getLog(JBIDeploymentListener.class);
	
	public boolean canHandle(File artifact) {
		try {
            // Accept jars and zips
            if (!artifact.getName().endsWith(".zip") &&
                !artifact.getName().endsWith(".jar")) {
                return false;
            }
			JarFile jar = new JarFile(artifact);
			JarEntry entry = jar.getJarEntry("META-INF/jbi.xml");
            // Only handle JBI artifacts
            if (entry == null) {
				return false;
			}
            // Only handle non OSGi bundles
            Manifest m = jar.getManifest();
            if (m.getMainAttributes().getValue(new Attributes.Name("Bundle-SymbolicName")) != null &&
                m.getMainAttributes().getValue(new Attributes.Name("Bundle-Version")) != null) {
                return false;
            }
            return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	public File handle(File artifact, File tmpDir) {
		try{
			String bundleName = artifact.getName().substring(0, artifact.getName().length() -4 ) + ".jar";
			File destFile = new File(tmpDir, bundleName);
			if (destFile.exists()) {
				destFile.delete();
			}			
			Transformer.transformToOSGiBundle(artifact, destFile);
			return destFile;
			
		} catch (Exception e) {
			Logger.error("Failed in transforming the JBI artifact to be OSGified. error is: " + e);
			return null;
		}
	}


}
