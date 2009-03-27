/*******************************************************************************
 *
 * MyMote - simple interface to the WiiMote IR sensor
 * =============================================================================
 *
 * Copyright (C) 2009 by Giampaolo Melis
 * Project home page: http://code.google.com/p/mymote/
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.bwepow.mote.driver;

import java.util.EventObject;

/**
 *
 * @author Giampaolo Melis
 */
public abstract class MoteEvent extends EventObject {

    private static final Object DUMMY_SOURCE = new Object();

    public MoteEvent() {
        // Workaround to avoid IllegalArgumentException thrown from the parent
        // Combined with source initialized to null and setSource method turns
        // the event in a JavaBean
        super(DUMMY_SOURCE);
        this.source = null;
    }

    protected void setSource(Object source) {
        //System.err.println("SRC PARENT: " + source);
        this.source = source;
    }
}
