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

/**
 *
 * @author Giampaolo Melis
 */
public class IRSpotExtended extends IRSpot {

    private static final long serialVersionUID = -6296488676929905253L;
    
    public IRSpotExtended() {
        super();
    }
    
    public IRSpotExtended(int x, int y, int size) {
        super();
        init(x, y);
        init(size);
    }

    public IRSpotExtended(int x, int y, int size, boolean present) {
        this(x, y, size);
        setPresent(present);
    }

    public void init(int x, int y, int size, boolean present) {
        init(x, y);
        init(size);
        setPresent(present);
        //System.out.println("x: " + x + " y: " + y + " size: " + size + " present: " + present);
    }
}
