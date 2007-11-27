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

package com.sun.ws.rest.impl.uri.rules;

import com.sun.ws.rest.api.core.HttpRequestContext;
import com.sun.ws.rest.api.core.HttpResponseContext;
import com.sun.ws.rest.impl.ResponseBuilderImpl;
import com.sun.ws.rest.spi.uri.rules.UriRule;
import com.sun.ws.rest.spi.uri.rules.UriRuleContext;
import java.util.List;
import javax.ws.rs.core.UriBuilder;

/**
 * Adapter to an existing rule that accepts the rule using the last capturing 
 * group value as the URI path.
 * <p>
 * This rule assumes that the pattern used to match this rule has certain
 * contraints. If a pattern contains one or more capturing groups then the
 * last capturing group MUST occur at the end of the pattern and MUST be
 * '(/.*)?' or '(/)?'.
 * <p>
 * If the source from which the pattern was derived ends in a '/'
 * and the matched path does not end in a '/' then a temporary redirect 
 * response is returned with a path that is the matched path appened with '/'.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public class RightHandPathRule implements UriRule {
    private final boolean patternEndsInSlash;

    private final UriRule rule;
    
    /**
     * @param patternEndsInSlash true if the pattern used to match with rule
     *        end in a '/', otherwise false.
     * @param rule the URI rule that is adapted.
     */
    public RightHandPathRule(boolean patternEndsInSlash, UriRule rule) {
        assert rule != null;
        
        this.patternEndsInSlash = patternEndsInSlash;
        this.rule = rule;
    }
    
    public final boolean accept(CharSequence path, Object resource, UriRuleContext context) {
        // Extract right hand path
        path = getRightHandPath(context.getGroupValues());
        if (path.length() == 0) {
            // Redirect to path ending with a '/' if pattern
            // ends in '/'
            if (patternEndsInSlash)
                return redirect(context);
        } else if (path.length() == 1) {
            // Path is '/', no match if pattern does not end in a '/'
            if (!patternEndsInSlash)
                return false;

            // Consume the '/'
            path = "";
        }
        
        // Accept using the right hand path
        return rule.accept(path, resource, context);
    }
    
    /**
     * Get the right hand path from the list of captured group
     * values. The right hand path is the last group value (if present)
     * 
     * @param groupValues the list of captured group values.
     * @return the right hand path or the empty string if the list of group
     *         values is empty.
     */
    private String getRightHandPath(List<String> groupValues) {        
        final String rhp = (!groupValues.isEmpty()) ? 
            groupValues.get(groupValues.size() - 1) :
            "";
        return (rhp != null) ? rhp : "";
    }

    /**
     * Redirect to a URI that ends in a slash.
     * 
     * TODO use the complete URI.
     */
    private boolean redirect(UriRuleContext context) {
        final HttpRequestContext request = context.getHttpContext().
                getHttpRequestContext();
        final HttpResponseContext response = context.getHttpContext().
                getHttpResponseContext();
        
        response.setResponse(
                ResponseBuilderImpl.temporaryRedirect(
                    UriBuilder.fromUri(request.getAbsolutePath()).path("/").build()
                ).build()
                );
        return true;
    }
}