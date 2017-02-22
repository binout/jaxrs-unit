package io.github.binout.jaxrsunit.tck;

import io.github.binout.jaxrsunit.JaxrsResource;
import io.github.binout.jaxrsunit.JaxrsResponse;
import io.github.binout.jaxrsunit.JaxrsServer;
import io.github.binout.jaxrsunit.JaxrsUnit;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class StatusTest {
    @Path("/hello")
    public static class HelloResource {
        @GET
        public String hello() {
            return "hello";
        }
    }

    private JaxrsServer server;

    @Before
    public void init() {
        server = JaxrsUnit.newServer(HelloTest.HelloResource.class);
    }

    @Test
    public void should_return_hello() {
        JaxrsResource resource = server.resource("/hello");

        JaxrsResponse response = resource.get();

        assertThat(response.status()).isEqualTo(Response.Status.OK);
        assertThat(response.content()).isEqualTo("hello");
    }
}
