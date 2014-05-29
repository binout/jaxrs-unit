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
package org.jaxrsunit;

import eu.infomas.annotation.AnnotationDetector;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public interface JaxrsServer {

    void configure(JaxrsServerConfig config);

    JaxrsResource resource(String uri);

    class JaxrsServerConfig {

        private String baseUrl;
        private Collection<Class<?>> resourceClasses;

        private JaxrsServerConfig(String baseUrl, Collection<Class<?>> resourceClasses) {
            this.baseUrl = baseUrl;
            this.resourceClasses = resourceClasses;
        }

        public static JaxrsServerConfig empty() {
            return  new JaxrsServerConfig("", new LinkedList<Class<?>>());
        }

        public static JaxrsServerConfig fromApplication(Application application) {
            return  new JaxrsServerConfig(getApplicationPath(application), application.getClasses());
        }

        private static String getApplicationPath(Application application) {
            ApplicationPath applicationPath = application.getClass().getAnnotation(ApplicationPath.class);
            if (applicationPath == null) {
                throw new RuntimeException(application.getClass() + " must have an ApplicationPath annotation");
            }
            return "/" + applicationPath.value();
        }

        public JaxrsServerConfig withResources(Class<?>... classes) {
            resourceClasses.addAll(Arrays.asList(classes));
            return this;
        }

        public JaxrsServerConfig withScanResources(String basePack) {
            AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter() {
                @Override
                public void reportTypeAnnotation(Class<? extends Annotation> aClass, String s) {
                    try {
                        resourceClasses.add(Class.forName(s));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                @Override
                public Class<? extends Annotation>[] annotations() {
                    return new Class[]{Path.class};
                }
            };
            final AnnotationDetector cf = new AnnotationDetector(reporter);
            try {
                cf.detect(basePack);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public Collection<Class<?>> getResources() {
            return resourceClasses;
        }

        public String getBaseUrl() {
            return baseUrl;
        }
    }
}
