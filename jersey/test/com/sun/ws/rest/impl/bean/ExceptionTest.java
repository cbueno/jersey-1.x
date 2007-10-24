/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved. 
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License. 
 * 
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.ws.rest.impl.bean;

import com.sun.ws.rest.api.container.ContainerException;
import com.sun.ws.rest.impl.client.ResponseInBound;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.UriTemplate;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class ExceptionTest extends AbstractBeanTester {
    
    public ExceptionTest(String testName) {
        super(testName);
    }

    static public class CheckedException extends Exception {
        public CheckedException() {
            super();
        }
    }
    
    @UriTemplate("/exception/checked")
    static public class ExceptionCheckedResource { 
        @HttpMethod
        public String get() throws CheckedException {
            throw new CheckedException();
        }
    }

    public void testExceptionChecked() {
        initiateWebApplication(ExceptionCheckedResource.class);
        
        boolean caught = false;
        try {
            resourceProxy("/exception/checked").get(ResponseInBound.class);
        } catch (ContainerException e) {
            caught = true;
            assertEquals(CheckedException.class, e.getCause().getClass());
        }
    }
    
    @UriTemplate("/exception/runtime")
    static public class ExceptionRutimeResource { 
        @HttpMethod
        public String get() {
            throw new UnsupportedOperationException();
        }
    }
    
    public void testExceptionRuntime() {
        initiateWebApplication(ExceptionRutimeResource.class);
        
        boolean caught = false;
        try {
            resourceProxy("/exception/runtime").get(ResponseInBound.class);
        } catch (UnsupportedOperationException e) {
            caught = true;
        }
        assertEquals(true, caught);
    }
}
