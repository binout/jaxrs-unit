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

import org.jaxrsunit.JaxrsResource;
import org.jaxrsunit.JaxrsResponse;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;

import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;

public class RestEasyResource implements JaxrsResource {

    private final Dispatcher dispatcher;
    private final String uri;

    RestEasyResource(Dispatcher dispatcher, String uri) {
       this.dispatcher = dispatcher;
       this.uri = uri;
    }

    private JaxrsResponse executeRequest(MockHttpRequest request) {
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        return new RestEasyResponse(response);
    }

    @Override
    public JaxrsResponse get() {
        return get(MediaType.MEDIA_TYPE_WILDCARD);
    }

    @Override
    public JaxrsResponse get(MediaType mediaType) {
        return get(mediaType.toString());
    }

    @Override
    public JaxrsResponse get(String mediaType) {
        try {
            MockHttpRequest request = MockHttpRequest.get(uri);
            request.accept(mediaType);
            return executeRequest(request);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JaxrsResponse post(String body) {
        return post(MediaType.MEDIA_TYPE_WILDCARD, body);
    }

    @Override
    public JaxrsResponse post(MediaType mediaType, String body) {
        return post(mediaType.toString(), body);
    }

    @Override
    public JaxrsResponse post(String mediaType, String body) {
        try {
            MockHttpRequest request = MockHttpRequest.post(uri);
            request.contentType(mediaType);
            request.content(body.getBytes());
            return executeRequest(request);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JaxrsResponse put(String body) {
        return put(MediaType.MEDIA_TYPE_WILDCARD, body);
    }

    @Override
    public JaxrsResponse put(MediaType mediaType, String body) {
        return put(mediaType.toString(), body);
    }

    @Override
    public JaxrsResponse put(String mediaType, String body) {
        try {
            MockHttpRequest request = MockHttpRequest.put(uri);
            request.contentType(mediaType);
            request.content(body.getBytes());
            return executeRequest(request);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JaxrsResponse delete() {
        try {
            return executeRequest(MockHttpRequest.delete(uri));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
