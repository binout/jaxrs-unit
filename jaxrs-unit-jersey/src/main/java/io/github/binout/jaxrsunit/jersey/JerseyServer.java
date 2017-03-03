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
package io.github.binout.jaxrsunit.jersey;

import java.net.URI;
import java.util.HashSet;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import io.github.binout.jaxrsunit.JaxrsResource;
import io.github.binout.jaxrsunit.JaxrsServer;

public class JerseyServer implements JaxrsServer  {

    private String baseUrl;
    private ResourceConfig resourceConfig;
    private Client client;
    private TestContainer testContainer;

    public JerseyServer() {
       resourceConfig = new ResourceConfig();
    }

    @Override
    public void configure(JaxrsServerConfig config) {
        baseUrl = config.getBaseUrl();
        HashSet<Class<?>> cl = new HashSet<Class<?>>();
        cl.addAll(config.getProviders());
        cl.addAll(config.getResources());
        resourceConfig = new ResourceConfig(cl);
        initServer();
    }
    @Override
    public JaxrsResource resource(String uri) {
        uri = uri.replaceAll(baseUrl, "");
        return new JerseyResource(client.target(uri));
    }

    private Client getClient(ClientConfig clientConfig) {
        if (clientConfig == null) {
            clientConfig = new ClientConfig();
        }
        return ClientBuilder.newClient(clientConfig);
    }

    private URI getBaseUri() {
        if (testContainer != null)
            return testContainer.getBaseUri();
        return UriBuilder.fromUri("http://localhost/").port(TestProperties.DEFAULT_CONTAINER_PORT).build();
    }

    private void initServer() {
        if (testContainer != null) {
            testContainer.stop();
        }
        DeploymentContext context = DeploymentContext.builder(resourceConfig).build();
        TestContainerFactory tcf = new InMemoryTestContainerFactory();

        this.testContainer = tcf.create(getBaseUri(), context);

        testContainer.start();

        this.client = getClient(testContainer.getClientConfig());
    }
}
