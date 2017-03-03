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
package io.github.binout.jaxrsunit;

import eu.infomas.annotation.AnnotationDetector;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public interface JaxrsServer {

    void configure(JaxrsServerConfig config);

    JaxrsResource resource(String uri);

    class JaxrsServerConfig {

        private String baseUrl;
        // resource, provider classes
        private Collection<Class<?>> classes;

        private JaxrsServerConfig(String baseUrl, Collection<Class<?>> classes) {
            this.baseUrl = baseUrl;
            this.classes = classes;
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
            this.classes.addAll(Arrays.asList(classes));
            return this;
        }
        public JaxrsServerConfig withProviders(Class<?>... classes) {
            this.classes.addAll(Arrays.asList(classes));
            return this;
        }

        public JaxrsServerConfig withScanResources(String baseResourcePackage) {
            return withScanClasses(baseResourcePackage, new Class[]{Path.class});
        }
        public JaxrsServerConfig withScanProviders(String baseResourcePackage) {
            return withScanClasses(baseResourcePackage, new Class[]{Provider.class});
        }
        public JaxrsServerConfig withScanClasses(String baseResourcePackage) {
            return withScanClasses(baseResourcePackage, new Class[]{Path.class, Provider.class});
        }
        private JaxrsServerConfig withScanClasses(String baseResourcePackage, final Class[] annotations) {
            AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter() {
                @Override
                public void reportTypeAnnotation(Class<? extends Annotation> aClass, String s) {
                    try {
                        classes.add(Class.forName(s));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                @Override
                public Class<? extends Annotation>[] annotations() {
                    return annotations;
                }
            };
            final AnnotationDetector cf = new AnnotationDetector(reporter);
            try {
                cf.detect(baseResourcePackage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;

        }

        public Collection<Class<?>> getResources() {
            List<Class<?>> resources = new ArrayList<>();
            for (Class<?> classObject : classes) {
                if (classObject.isAnnotationPresent(Path.class)) {
                    resources.add(classObject);
                }
            }
            return resources;
        }

        public Collection<Class<?>> getProviders() {
            List<Class<?>> providers = new ArrayList<>();
            for (Class<?> classObject : classes) {
                if (classObject.isAnnotationPresent(Provider.class)) {
                    providers.add(classObject);
                }
            }
            return providers;
        }

        public Collection<Class<?>> getClasses() {
            return this.classes;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

    }
}
