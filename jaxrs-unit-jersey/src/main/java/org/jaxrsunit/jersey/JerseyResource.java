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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.jaxrsunit.JaxrsResource;
import org.jaxrsunit.JaxrsResponse;

public class JerseyResource implements JaxrsResource {

    private final WebResource webResource;

    JerseyResource(WebResource resource) {
        this.webResource = resource;
    }

    @Override
    public JaxrsResponse get() {
        return new JerseyResponse(webResource.get(ClientResponse.class));
    }

    @Override
    public JaxrsResponse post(String body) {
        return new JerseyResponse(webResource.post(ClientResponse.class, body));
    }

    @Override
    public JaxrsResponse delete() {
        return new JerseyResponse(webResource.delete(ClientResponse.class));
    }
}
