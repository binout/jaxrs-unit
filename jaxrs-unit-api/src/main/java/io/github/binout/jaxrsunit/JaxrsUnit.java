/*
 * Copyright 2014 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.binout.jaxrsunit;

import javax.ws.rs.core.Application;
import java.util.Iterator;
import java.util.ServiceLoader;

import static io.github.binout.jaxrsunit.JaxrsServer.JaxrsServerConfig;

public class JaxrsUnit {

    /**
     * Build a new JaxrsServer from a list of resources
     * @param resources a list of JAX-RS resources
     * @return an instance of JaxrsServer
     */
    public static JaxrsServer newServer(Class<?>... resources) {
        return configureServer(JaxrsServerConfig.empty().withResources(resources));
    }

    /**
     * Build a new JaxrsServer from an application.
     * The path of application will be used as base URL..
     * The resources are discovered by the getClasses method.
     * @param application a JAX-RS application
     * @return an instance of JaxrsServer
     */
    public static JaxrsServer newServer(Application application) {
        return configureServer(JaxrsServerConfig.fromApplication(application));
    }

    /**
     * Build a new JaxrsServer from a list of resources as result of a scan of a package
     * @param baseResourcePackage the base package for JAX-RS resources
     * @return an instance of JaxrsServer
     */
    public static JaxrsServer newServer(String baseResourcePackage) {
        return configureServer(JaxrsServerConfig.empty().withScanClasses(baseResourcePackage));
    }

    private static JaxrsServer configureServer(JaxrsServerConfig config) {
        JaxrsServer jaxrsServer = buildServer();
        jaxrsServer.configure(config);
        return jaxrsServer;
    }

    private static JaxrsServer buildServer() {
        ServiceLoader<JaxrsServer> serviceLoader = ServiceLoader.load(JaxrsServer.class);
        Iterator<JaxrsServer> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            throw new RuntimeException("No implemention found for JaxrsServer");
        }
    }
}
