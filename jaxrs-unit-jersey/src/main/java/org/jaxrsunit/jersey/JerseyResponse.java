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
import org.jaxrsunit.JaxrsResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class JerseyResponse implements JaxrsResponse {

    private final ClientResponse clientResponse;

    JerseyResponse(ClientResponse clientResponse) {
        this.clientResponse = clientResponse;
    }

    @Override
    public String content() {
        return clientResponse.getEntity(String.class);
    }

    private boolean isStatus(Response.Status status) {
        return clientResponse.getStatus() == status.getStatusCode();
    }

    @Override
    public boolean ok() {
        return isStatus(Response.Status.OK);
    }

    @Override
    public boolean created() {
        return isStatus(Response.Status.CREATED);
    }

    @Override
    public boolean notAcceptable() {
        return isStatus(Response.Status.NOT_ACCEPTABLE);
    }

    @Override
    public MediaType mediaType() {
        return clientResponse.getType();
    }

    @Override
    public String contentType() {
        return mediaType().toString();
    }
}
