/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.common.utils;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.config.annotation.DubboService;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.dubbo.common.utils.AnnotationUtils.*;
import static org.apache.dubbo.common.utils.MethodUtils.findMethod;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AnnotationUtils} Test
 *
 * @since 2.7.6
 */
public class AnnotationUtilsTest {

    @Test
    public void testIsType() throws NoSuchMethodException {
        // null checking
        assertFalse(isType(null));
        // Method checking
        assertFalse(isType(findMethod(A.class, "execute")));
        // Class checking
        assertTrue(isType(A.class));
    }


    @Test
    public void testGetValue() {
        Adaptive adaptive = A.class.getAnnotation(Adaptive.class);
        String[] value = getValue(adaptive);
        assertEquals(asList("a", "b", "c"), asList(value));
    }






    @Test
    public void testGetAllMetaAnnotations() {
        List<Annotation> metaAnnotations = getAllMetaAnnotations(Service5.class);
        int offset = 0;
        assertEquals(9, metaAnnotations.size());
        assertEquals(Inherited.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(Service4.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(Inherited.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(Service3.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(Inherited.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(Service2.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(Inherited.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(DubboService.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(Inherited.class, metaAnnotations.get(offset++).annotationType());

        metaAnnotations = getAllMetaAnnotations(MyAdaptive.class);
        offset = 0;
        assertEquals(2, metaAnnotations.size());
        assertEquals(Inherited.class, metaAnnotations.get(offset++).annotationType());
        assertEquals(Adaptive.class, metaAnnotations.get(offset++).annotationType());
    }






    @Test
    public void testFindMetaAnnotations() {
        List<DubboService> services = findMetaAnnotations(B.class, DubboService.class);
        assertEquals(1, services.size());

        DubboService service = services.get(0);
        assertEquals("", service.interfaceName());
        assertEquals(Cloneable.class, service.interfaceClass());

        services = findMetaAnnotations(Service5.class, DubboService.class);
        assertEquals(1, services.size());

        service = services.get(0);
        assertEquals("", service.interfaceName());
        assertEquals(Cloneable.class, service.interfaceClass());
    }

    @Test
    public void testFindMetaAnnotation() {
        DubboService service = findMetaAnnotation(B.class, DubboService.class);
        assertEquals(Cloneable.class, service.interfaceClass());

        service = findMetaAnnotation(B.class, "org.apache.dubbo.config.annotation.DubboService");
        assertEquals(Cloneable.class, service.interfaceClass());

        service = findMetaAnnotation(Service5.class, DubboService.class);
        assertEquals(Cloneable.class, service.interfaceClass());
    }



    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Inherited
    @DubboService(interfaceClass = Cloneable.class)
    @interface Service2 {


    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Inherited
    @Service2
    @interface Service3 {


    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Inherited
    @Service3
    @interface Service4 {


    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Inherited
    @Service4
    @interface Service5 {


    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Inherited
    @Adaptive
    @interface MyAdaptive {
        String[] value() default {};
    }



}
