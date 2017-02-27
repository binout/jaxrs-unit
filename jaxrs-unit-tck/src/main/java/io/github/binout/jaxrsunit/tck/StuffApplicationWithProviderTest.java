package io.github.binout.jaxrsunit.tck;

import io.github.binout.jaxrsunit.JaxrsResource;
import io.github.binout.jaxrsunit.JaxrsResponse;
import io.github.binout.jaxrsunit.JaxrsServer;
import io.github.binout.jaxrsunit.JaxrsUnit;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class StuffApplicationWithProviderTest {

    @ApplicationPath("rest")
    public class HelloApplication extends Application {
        @Override
        public Set<Class<?>> getClasses() {
            Set<Class<?>> classes = new HashSet<>();
            classes.add(StuffResource.class);
            classes.add(IllegalArgumentExceptionMapper.class);
            return classes;
        }
    }

    @Path("/stuff")
    public static class StuffResource {
        @GET
        public String hello() {
            throw new ClientErrorException(Response.Status.EXPECTATION_FAILED);
        }
    }
    @Provider
    public static class IllegalArgumentExceptionMapper implements ExceptionMapper<ClientErrorException> {
        @Override
        public Response toResponse(ClientErrorException e) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity( e.getMessage())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
    private JaxrsServer server;

    @Before
    public void init() {
        server = JaxrsUnit.newServer(new HelloApplication());
    }

    @Test
    public void should_return_stuff() {
        JaxrsResource resource = server.resource("/rest/stuff");

        JaxrsResponse response = resource.get();

        assertThat(response.status()).isEqualTo(Response.Status.BAD_REQUEST);
        assertThat(response.content()).isEqualTo("HTTP 417 Expectation Failed");
    }
}
