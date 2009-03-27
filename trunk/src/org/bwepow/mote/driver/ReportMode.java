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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Giampaolo Melis
 */
public enum ReportMode {

    /**
     * Core Buttons and Accelerometer with 12 IR bytes
     */
    DATA_REPORT_0x33((byte) 0x33, IRListenerExtended.class, IREventExtended.class, 7),
    /**
     * Core Buttons with 19 Extension bytes
     */
    DATA_REPORT_0x34((byte) 0x34, null, null, -1),
    /**
     * Core Buttons and Accelerometer with 16 Extension Bytes
     */
    DATA_REPORT_0x35((byte) 0x35, null, null, -1),
    /**
     * Core Buttons with 10 IR bytes and 9 Extension Bytes
     */
    DATA_REPORT_0x36((byte) 0x36, IRListenerBasic.class, IREventBasic.class, 4),
    /**
     * Core Buttons and Accelerometer with 10 IR bytes and 6 Extension Bytes
     */
    DATA_REPORT_0x37((byte) 0x37, IRListenerBasic.class, IREventBasic.class, 7);
    
    private static final Map<Byte, ReportMode> MODE_MAP = (Map<Byte, ReportMode>)
		new HashMap<Byte, ReportMode>() {
            @Override
			public Object clone() {
				for(ReportMode mode: ReportMode.values()) {
                    put(mode.getCode(), mode);
                }
				return super.clone();
			}
		}.clone();
    
    private final byte mode;
    private final int offset;
    private final Class<? extends IRListener> listener;
    private final Class<? extends IREvent> event;

    private ReportMode(byte mode, Class<? extends IRListener> listener, Class<? extends IREvent> event, int offset) {
        this.mode = mode;
        this.listener = listener;
        this.event = event;
        this.offset = offset;
    }

    public byte getCode() {
        return mode;
    }

    public Class<? extends IRListener> getListener() {
        return listener;
    }

    public Class<? extends IREvent> getEvent() {
        return event;
    }

    public int getOffset() {
        return offset;
    }

    public static ReportMode getReportMode(byte code) {
        return MODE_MAP.get(code);
    }
}
