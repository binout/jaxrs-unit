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
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class MessageTest {

    private JaxrsResource resource;

    @Path("/message")
    public static class MessageResource {
        static String message = "";
        @GET
        public String getMessage() {
            return message;
        }
        @POST
        public Response setMessage(String msg) {
            message = msg;
            return Response.status(Response.Status.CREATED).build();
        }
        @PUT
        public Response setMessage2(String msg) {
            return setMessage(msg);
        }
        @DELETE
        public Response clearMessage() {
            message = "";
            return Response.ok().build();
        }
    }

    private JaxrsServer server;

    @Before
    public void init() {
        server = JaxrsUnit.newServer(MessageResource.class);
        MessageResource.message = "";
        resource = server.resource("/message");
    }

    @Test
    public void should_return_empty_message() {
        JaxrsResponse response = resource.get();

        assertThat(response.ok()).isTrue();
        assertThat(response.content()).isEqualTo("");
    }

    @Test
    public void should_set_message_with_post() {
        JaxrsResponse response = resource.post("A new message");

        assertThat(response.created()).isTrue();
        assertThat(MessageResource.message).isEqualTo("A new message");
    }

    @Test
    public void should_set_message_with_put() {
        JaxrsResponse response = resource.put("A new message");

        assertThat(response.created()).isTrue();
        assertThat(MessageResource.message).isEqualTo("A new message");
    }

    @Test
    public void should_delete_message() {
        resource.post("A new message");
        assertThat(MessageResource.message).isEqualTo("A new message");

        JaxrsResponse response = resource.delete();
        assertThat(response.ok()).isTrue();
        assertThat(MessageResource.message).isEqualTo("");
    }
}
