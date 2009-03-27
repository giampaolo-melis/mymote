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
public class IREventFull extends IREvent<IRSpotFull> {

    private static final long serialVersionUID = 8392343242074747098L;

    @Override
    public IRSpotFull[] getSpots() {
        byte[] report_0 = getReport_0();
        byte[] report_1 = getReport_1();
        if (spots == null) {
            spots = new IRSpotFull[DEFAULT_NUM_SPOTS];
            int j, k;
            for (k = 0, j = 5; k < 2; k++, j += 9) {
                int x = ((report_0[j + 2] & 0x30) << 4) + (report_0[j + 0] & 0xff);
                int y = ((report_0[j + 2] & 0xc0) << 2) + (report_0[j + 1] & 0xff);
                int size = report_0[j + 2] & 0xf;
                int xMin = report_0[j + 3] & 0xff;
                int yMin = report_0[j + 4] & 0xff;
                int xMax = report_0[j + 5] & 0xff;
                int yMax = report_0[j + 6] & 0xff;
                int intensity = report_0[j + 8] & 0xff;
                spots[k] = new IRSpotFull();
                if ((x & 0x3ff) != 1023) {
                    spots[k].init(x, y, size, xMin, yMin, xMax, yMax, intensity, true);
                }
            }

            for (k = 0, j = 5; k < 2; k++, j += 9) {
                int x = ((report_1[j + 2] & 0x30) << 4) + (report_1[j + 0] & 0xff);
                int y = ((report_1[j + 2] & 0xc0) << 2) + (report_1[j + 1] & 0xff);
                int size = report_1[j + 2] & 0xf;
                int xMin = report_1[j + 3] & 0xff;
                int yMin = report_1[j + 4] & 0xff;
                int xMax = report_1[j + 5] & 0xff;
                int yMax = report_1[j + 6] & 0xff;
                int intensity = report_1[j + 8] & 0xff;
                spots[k + 2] = new IRSpotFull();
                if ((x & 0x3ff) != 1023) {
                    spots[k + 2].init(x, y, size, xMin, yMin, xMax, yMax, intensity, true);
                }
            }
        }
        return spots;
    }
}
