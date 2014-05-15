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
package org.jaxrsunit.internal;

import org.jaxrsunit.JaxrsResponse;

import javax.ws.rs.core.Response;

public abstract class AbstractJaxrsResponse implements JaxrsResponse {

    protected abstract boolean isStatus(Response.Status status);

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
    public boolean unsupportedMediaType() {
        return isStatus(Response.Status.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    public String contentType() {
        return mediaType().toString();
    }
}
