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
public abstract class IREvent<T extends IRSpot> extends MoteEvent {
    
    public static final int DEFAULT_NUM_SPOTS = 4;

    private byte[] report_0;
    private byte[] report_1;

    protected T[] spots;
    
    public IREvent() {
        super();
    }
    
    @Override
    public WiiMote getSource() {
        return (WiiMote)super.getSource();
    }

    public void setSource(WiiMote source) {
        //System.err.println("SRC: " + this.source);
        if (this.source != null)
            throw new IllegalStateException("Source can be set only one time");
        super.setSource((Object)source);
    }

    protected byte[] getReport_0() {
        return report_0;
    }

    public void setReport_0(byte[] report_0) {
        if (this.report_0 != null)
            throw new IllegalStateException("Report 0 can be set only one time");
        this.report_0 = report_0;
    }

    protected byte[] getReport_1() {
        return report_1;
    }

    public void setReport_1(byte[] report_1) {
        if (this.report_1 != null)
            throw new IllegalStateException("Report 1 can be set only one time");
        this.report_1 = report_1;
    }

    public abstract T[] getSpots();

}
