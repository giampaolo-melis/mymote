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
public class IREventExtended extends IREvent<IRSpotExtended> {

    private static final long serialVersionUID = 6777659148006435060L;

    @Override
    public IRSpotExtended[] getSpots() {
        byte[] report_0 = getReport_0();
        if (spots == null) {
            spots = new IRSpotExtended[DEFAULT_NUM_SPOTS];
            int l, j;
            for (l = 0, j = 0; l < 4; l++, j += 3) {
                int x = ((report_0[j + 2] & 0x30) << 4) + (report_0[j + 0] & 0xff);
                int y = ((report_0[j + 2] & 0xc0) << 2) + (report_0[j + 1] & 0xff);
                int size = report_0[j + 2] & 0xf;
                spots[l] = new IRSpotExtended();
                if ((x & 0x3ff) != 1023) {
                    spots[l].init(x, y, size, true);
                }
            }
        }
        return spots;
    }
}
