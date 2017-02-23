package io.github.binout.jaxrsunit.tck.scan;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("r3")
public class Resource3 {

    @GET
    public String get() {
        throw new IllegalArgumentException("illegal argument");
    }
}
