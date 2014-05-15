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
import org.jaxrsunit.internal.AbstractJaxrsResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class JerseyResponse extends AbstractJaxrsResponse {

    private final ClientResponse clientResponse;

    JerseyResponse(ClientResponse clientResponse) {
        this.clientResponse = clientResponse;
    }

    @Override
    public String content() {
        return clientResponse.getEntity(String.class);
    }

    @Override
    public String contentLanguage() {
        return clientResponse.getLanguage();
    }

    @Override
    protected boolean isStatus(Response.Status status) {
        return clientResponse.getStatus() == status.getStatusCode();
    }

    @Override
    public MediaType mediaType() {
        return clientResponse.getType();
    }

}
