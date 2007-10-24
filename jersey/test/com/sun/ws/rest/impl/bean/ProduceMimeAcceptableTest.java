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

import com.sun.ws.rest.impl.client.ResponseInBound;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.UriTemplate;
import javax.ws.rs.core.Response;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class ProduceMimeAcceptableTest extends AbstractBeanTester {
    
    public ProduceMimeAcceptableTest(String testName) {
        super(testName);
    }

    @UriTemplate("/")
    public static class WebResource {
        @ProduceMime("application/foo")
        @HttpMethod("GET")
        public Response doGet() {
            return Response.Builder.representation("content", "application/bar").build();
        }
    }
        
    public void testAcceptable() {
        initiateWebApplication(WebResource.class);
        
        resourceProxy("/").acceptable("application/foo", "application/bar").get(String.class);
    }
    
    public void testNotAcceptable() {
        initiateWebApplication(WebResource.class);
        
        ResponseInBound response = resourceProxy("/", false).
                acceptable("application/foo").get(ResponseInBound.class);
        assertEquals(500, response.getStatus());
    }

    
    @UriTemplate("/")
    public static class WebResourceProduceGeneric {
        @ProduceMime("*/*")
        @HttpMethod("GET")
        public Response doGet() {
            return Response.Builder.representation("content", "application/bar").build();
        }
    }
    
    public void testProduceGeneric() {
        initiateWebApplication(WebResourceProduceGeneric.class);
        
        resourceProxy("/").acceptable("application/bar").get(String.class);
    }
    
}
