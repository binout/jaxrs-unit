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
package org.jaxrsunit.resteasy;

import org.jaxrsunit.internal.AbstractJaxrsResponse;
import org.jboss.resteasy.mock.MockHttpResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class RestEasyResponse extends AbstractJaxrsResponse {

    private MockHttpResponse mockResponse;

    RestEasyResponse(MockHttpResponse mockResponse) {
        this.mockResponse = mockResponse;
    }

    @Override
    public String content() {
        return mockResponse.getContentAsString();
    }

    @Override
    protected boolean isStatus(Response.Status status) {
        return status.getStatusCode() == mockResponse.getStatus();
    }

    @Override
    public MediaType mediaType() {
        MultivaluedMap<String, Object> headers = mockResponse.getOutputHeaders();
        return (MediaType) headers.getFirst("Content-Type");
    }

}
