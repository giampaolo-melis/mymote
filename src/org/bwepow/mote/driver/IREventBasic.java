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
public class IREventBasic extends IREvent<IRSpotBasic> {

    private static final long serialVersionUID = -2054709525264526494L;

    @Override
    public IRSpotBasic[] getSpots() {
        byte[] report_0 = getReport_0();
        if (spots == null) {
            spots = new IRSpotBasic[DEFAULT_NUM_SPOTS];
            int j, k;
            for (k = 0, j = 0; k < 2; k++, j += 5) {
                int x = ((report_0[j + 2] & 0x30) << 4) + (report_0[j + 0] & 0xff);
                int y = ((report_0[j + 2] & 0xc0) << 2) + (report_0[j + 1] & 0xff);
                spots[2 * k] = new IRSpotBasic();
                if ((x & 0x3ff) != 1023) {
                    spots[2 * k].init(x, y, true);
                }
                x = ((report_0[j + 2] & 3) << 8) + (report_0[j + 3] & 0xff);
                y = ((report_0[j + 2] & 0xc) << 6) + (report_0[j + 4] & 0xff);
                spots[1 + 2 * k] = new IRSpotBasic();
                if ((x & 0x3ff) != 1023) {
                    spots[1 + 2 * k].init(x, y, true);
                }
            }
        }
        return spots;
    }
}
