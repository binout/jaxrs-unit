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
package io.github.binout.jaxrsunit.resteasy;

import io.github.binout.jaxrsunit.JaxrsResource;
import io.github.binout.jaxrsunit.JaxrsServer;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;

public class RestEasyServer implements JaxrsServer {

    private String baseUrl;
    private Dispatcher dispatcher;

    public RestEasyServer() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getProviderFactory().registerProvider(ResteasyJacksonProvider.class);
    }

    @Override
    public void configure(JaxrsServerConfig config) {
        baseUrl = config.getBaseUrl();
        for (Class<?> resourceClass : config.getResources()) {
            dispatcher.getRegistry().addResourceFactory(new POJOResourceFactory(resourceClass));
        }
    }

    @Override
    public JaxrsResource resource(String uri) {
        uri = uri.replaceAll(baseUrl, "");
        return new RestEasyResource(dispatcher,  uri);
    }
}
