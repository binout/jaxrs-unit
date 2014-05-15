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
package org.jaxrsunit.tck;

import org.jaxrsunit.JaxrsResource;
import org.jaxrsunit.JaxrsResponse;
import org.jaxrsunit.JaxrsServer;
import org.jaxrsunit.JaxrsUnit;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AcceptTest {

    @Path("/hello")
    public static class JsonResource {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public String hello() {
            return "{\"message\":\"hello\"}";
        }
    }

    private JaxrsServer server;

    @Before
    public void init() {
        server = JaxrsUnit.newServer(JsonResource.class);
    }

    @Test
    public void should_not_return_hello_because_content_negociation_failed() {
        JaxrsResource resource = server.resource("/hello");

        JaxrsResponse response = resource.get(MediaType.APPLICATION_XML);

        assertThat(response.ok()).isFalse();
        assertThat(response.notAcceptable()).isTrue();
    }

    @Test
    public void should_return_hello_in_json() {
        JaxrsResource resource = server.resource("/hello");

        JaxrsResponse response = resource.get(MediaType.APPLICATION_JSON);

        assertThat(response.ok()).isTrue();
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.mediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.content()).isEqualTo("{\"message\":\"hello\"}");
    }
}
