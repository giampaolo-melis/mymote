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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Giampaolo Melis
 */
public class MoteDiscoverer implements DiscoveryListener {

    private static final int DISCOVERY_RETRIES = 6;

    private static final long RETRIES_INTERVAL = 2000l;

    private EventListenerList listenerList = new EventListenerList();

    private Vector<RemoteDevice> devices =  new Vector<RemoteDevice>();

    private DiscoveryThread discoveryThread;

    public void startDiscover() throws IOException {

        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.err.println("My Bluetooth MAC: " + localDevice.getBluetoothAddress());
        DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
        System.err.println("Starting device inquiry...");
        discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);

    }

    public void addMoteDiscoveryListener(MoteDiscoveryListener listener) {
        listenerList.add(MoteDiscoveryListener.class, listener);
    }
    
    public void removeMoteDiscoveryListener(MoteDiscoveryListener listener) {
        listenerList.remove(MoteDiscoveryListener.class, listener);
    }

    @Override
    public void deviceDiscovered(RemoteDevice device, DeviceClass devclass) {
        try {
            System.err.print("Device discovered: " + 
                    device.getFriendlyName(true) + " - " + 
                    device.getBluetoothAddress() + " - " + 
                    devclass.getMajorDeviceClass() + ":" + 
                    devclass.getMinorDeviceClass() + " - " + 
                    devclass.getServiceClasses() + " - ");
        } catch (IOException ex) {
            Logger.getLogger(MoteDiscoverer.class.getName()).log(Level.SEVERE, null, ex);
        }
                //device.getBluetoothAddress() + " - ");
        // add the device to the vector
        if (!devices.contains(device) &&
                devclass.getMajorDeviceClass() == 1280 &&
                devclass.getMinorDeviceClass() == 4) {
            System.err.println("Is a Wiimote!");
            devices.addElement(device);
            // FAST CONNECT
            //fireDiscoveryCompleted();
        } else {
            System.err.println("Is NOT a Wiimote!");
        }
    }

    @Override
    public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
    }

    @Override
    public void serviceSearchCompleted(int arg0, int arg1) {
    }

    @Override
    public void inquiryCompleted(int discType) {
        switch (discType) {
            case DiscoveryListener.INQUIRY_COMPLETED:
                System.err.println("Inquiry completed.");
                fireDiscoveryCompleted();
                break;

            case DiscoveryListener.INQUIRY_ERROR:
                System.err.println("Inquiry error.");
                fireDiscoveryFailed();
                break;

            case DiscoveryListener.INQUIRY_TERMINATED:
                System.err.println("Inquiry terminated.");
                fireDiscoveryNotFound();
                break;
        }
    }

    protected void fireDiscoveryCompleted() {
        MoteDiscoveryListener[] listeners = listenerList.getListeners(MoteDiscoveryListener.class);
        MoteDiscoveredEvent evt = new MoteDiscoveredEvent(this);
        for (MoteDiscoveryListener listener : listeners) {
            listener.received(evt);
        }
    }

    protected void fireDiscoveryFailed() {
        MoteDiscoveryListener[] listeners = listenerList.getListeners(MoteDiscoveryListener.class);
        MoteDiscoveredEvent evt = new MoteDiscoveredEvent(this);
        for (MoteDiscoveryListener listener : listeners) {
            listener.failed(evt);
        }
    }
    
    protected void fireDiscoveryNotFound() {
        MoteDiscoveryListener[] listeners = listenerList.getListeners(MoteDiscoveryListener.class);
        MoteDiscoveredEvent evt = new MoteDiscoveredEvent(this);
        for (MoteDiscoveryListener listener : listeners) {
            listener.notFound(evt);
        }
    }

    public WiiMote[] getDiscovered() throws IOException {
        WiiMote[] wiimotes = new WiiMote[devices.size()];

        for (int i = 0; i < devices.size(); i++) {
            wiimotes[i] = new WiiMote(devices.elementAt(i).getBluetoothAddress());
        }

        return wiimotes;
    }
    
    class DiscoveryThread extends Thread {

        public DiscoveryThread() {
            super("BT-Discovery-Thread");
            setPriority(MIN_PRIORITY);
        }

        @Override
        public void run() {
            super.run();
        }
    }
    
    
}
