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

package com.sun.ws.rest.samples.bookstore.resources;

import com.sun.ws.rest.api.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class CD extends Item {
    
    private final Track[] tracks;
    
    public CD(final String title, final String author, final Track[] tracks) {
        super(title, author);
        this.tracks = tracks;
    }
    
    public Track[] getTracks() {
        return tracks;
    }
    
    @Path("tracks/{num}/")
    public Track getTrack(@PathParam("num") int num) {
        if (num >= tracks.length)
            throw new NotFoundException("Track, " + num + ", of CD, " + getTitle() + ", is not found");
        return tracks[num];
    }    
}
