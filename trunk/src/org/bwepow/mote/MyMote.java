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

package org.bwepow.mote;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bwepow.mote.driver.IRListener;
import org.bwepow.mote.driver.IRMode;
import org.bwepow.mote.driver.IRSensitivity;
import org.bwepow.mote.driver.MoteDiscoveredEvent;
import org.bwepow.mote.driver.MoteDiscoverer;
import org.bwepow.mote.driver.MoteDiscoveryListener;
import org.bwepow.mote.driver.ReportMode;
import org.bwepow.mote.driver.WiiMote;

/**
 *
 * @author Giampaolo Melis
 */
public class MyMote implements MoteDiscoveryListener {
    
    private WiiMote mote;
    private Class<? extends IRListener> irListenerClass;
    private IRListener irListenerInstance;

    public MyMote(Class<? extends IRListener> irListenerClass) {
        this.irListenerClass = irListenerClass;
    }

    public void connect() {
        try {
            MoteDiscoverer discoverer = new MoteDiscoverer();
            discoverer.addMoteDiscoveryListener(this);
            discoverer.startDiscover();
        } catch (IOException ex) {
            Logger.getLogger(MyMote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disconnect() {
        if (mote == null || !mote.isConnected())
            return;
        if (irListenerInstance != null)
            mote.removeIRListener(irListenerInstance);
        irListenerInstance = null;
        try {
            mote.disableIR();    
        } catch (IOException ex) {
            Logger.getLogger(MyMote.class.getName()).log(Level.WARNING, "Unable to disable IR camera.", ex);
        }
        mote.stopReceiver();
        mote.disconnect();
    }

    @Override
    public void failed(MoteDiscoveredEvent event) {
        Logger.getLogger(MyMote.class.getName()).log(Level.WARNING, "Discovery failed.");
    }

    @Override
    public void notFound(MoteDiscoveredEvent event) {
        Logger.getLogger(MyMote.class.getName()).log(Level.WARNING, "No WiiMote found.");
    }

    @Override
    public void received(MoteDiscoveredEvent event) {
        try {
            MoteDiscoverer discoverer = event.getSource();
            WiiMote[] motes = discoverer.getDiscovered();
            if (motes.length > 0) {
                mote = motes[0];
                if (mote != null && !mote.isConnected()) {
                    mote.connect();
                    mote.startReceiver();
                    mote.setLED(1);
                    mote.enableIR(ReportMode.DATA_REPORT_0x33, IRMode.EXTENDED, IRSensitivity.MARCAN);
                    irListenerInstance = irListenerClass.newInstance();
                    mote.addIRListener(irListenerInstance);
                    discoverer.removeMoteDiscoveryListener(this);
                }
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(MyMote.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MyMote.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyMote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
