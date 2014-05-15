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
package org.jaxrsunit.jersey;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainer;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.inmemory.InMemoryTestContainerFactory;
import org.jaxrsunit.JaxrsResource;
import org.jaxrsunit.JaxrsServer;

import javax.ws.rs.core.UriBuilder;
import java.util.Arrays;

public class JerseyServer implements JaxrsServer {

    private DefaultResourceConfig resourceConfig;
    private Client client;
    private TestContainer testContainer;

    public JerseyServer() {
       resourceConfig = new DefaultResourceConfig();
    }

    @Override
    public void addResources(Class<?>... resourceClasses) {
        resourceConfig.getClasses().addAll(Arrays.asList(resourceClasses));
        initServer();
    }

    @Override
    public JaxrsResource resource(String uri) {
        return new JerseyResource(client.resource(uri));
    }

    private void initServer() {
        if (testContainer != null) {
            testContainer.stop();
        }
        AppDescriptor ad = new LowLevelAppDescriptor.Builder(resourceConfig).build();
        TestContainerFactory tcf = new InMemoryTestContainerFactory();
        testContainer = tcf.create(UriBuilder.fromUri("/").build(), ad);
        client = testContainer.getClient();
        testContainer.start();
    }
}
