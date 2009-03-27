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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giampaolo Melis
 */
public class ReceiverThread extends Thread {

    private static final long SLEEP_INTERVAL = 10000000l;
    private static final int BUFFER_SIZE = 23;
    
    private static int count = 0;

    private WiiMote mote;
    private volatile boolean closed = false;
    private volatile byte[] buf;

    public ReceiverThread(WiiMote mote) {
        super("Mote-Receiver-Thread-" + (count++));
        this.mote = mote;
        buf = new byte[BUFFER_SIZE];
    }

    @Override
    public void run() {
        while (!closed) {
            try {
                long startTime = System.nanoTime();
                if (buf == null) {
                    buf = new byte[BUFFER_SIZE];
                } else {
                    for (int i = 0; i < BUFFER_SIZE; i++) {
                        buf[i]=(byte)0;
                    }
                }
                //System.out.println("Waiting for data.");
                mote.getRaw(buf);
                //System.out.println("Data received.");
                mote.parseData(buf);
                Thread.yield();
                long estimatedTime = (SLEEP_INTERVAL - (System.nanoTime() - startTime))/1000000l;
                if(estimatedTime > 0)
                    Thread.sleep(estimatedTime);
            } catch (IOException ex) {
                Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, "Problems reading mote data.", ex);
                close();
            } catch (InterruptedException ex) {
                Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, this.getName() + " interrupted.", ex);
                close();
            } catch (Exception ex) {
                Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, "Problem occurred in " + this.getName() + ".", ex);
            }
        }
    }

    public void close() {
        closed = true;
    }
}
