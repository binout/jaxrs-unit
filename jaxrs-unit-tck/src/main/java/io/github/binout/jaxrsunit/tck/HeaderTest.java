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
package io.github.binout.jaxrsunit.tck;

import io.github.binout.jaxrsunit.JaxrsResource;
import io.github.binout.jaxrsunit.JaxrsResponse;
import io.github.binout.jaxrsunit.JaxrsServer;
import io.github.binout.jaxrsunit.JaxrsUnit;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderTest {

    private JaxrsResource resource;

    @Path("/hello")
    public static class HeaderResource {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response get() {
            return Response.ok().entity("{\"message\":\"hello\"}").language("en").build();
        }
    }

    private JaxrsServer server;

    @Before
    public void init() {
        server = JaxrsUnit.newServer(HeaderResource.class);
        resource = server.resource("/hello");
    }

    @Test
    public void should_return_headers_of_hello() {
        JaxrsResponse response = resource.head();

        assertThat(response.ok()).isTrue();
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.mediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.contentLanguage()).isEqualTo("en");
    }

}
