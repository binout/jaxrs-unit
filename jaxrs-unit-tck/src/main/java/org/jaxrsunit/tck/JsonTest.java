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
import javax.xml.bind.annotation.XmlRootElement;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonTest {

    @XmlRootElement //only for jersey
    public static class Person {
        private String firstName;
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    @Path("/john.doe")
    public static class JohnDoeResource {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Person person() {
            Person johnDoe = new Person();
            johnDoe.setFirstName("john");
            johnDoe.setLastName("doe");
            return johnDoe;
        }
    }

    private JaxrsServer server;

    @Before
    public void init() {
        server = JaxrsUnit.newServer(JohnDoeResource.class);
    }

    @Test
    public void should_return_hello() {
        JaxrsResource resource = server.resource("/john.doe");

        JaxrsResponse response = resource.get(MediaType.APPLICATION_JSON_TYPE);

        assertThat(response.ok()).isTrue();
        assertThat(response.content()).isEqualTo("{\"firstName\":\"john\",\"lastName\":\"doe\"}");
    }
}
