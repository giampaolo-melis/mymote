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
import javax.bluetooth.L2CAPConnection;
import javax.microedition.io.Connector;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Giampaolo Melis
 */
public class WiiMote {

    public static final byte CMD_SET_REPORT = 0x52;

    private EventListenerList listenerList = new MoteListenerList();
    
    private String address;
    // control connection, send commands to wiimote
    private L2CAPConnection controlCon;
    // receive connection, receive answers from wiimote
    private L2CAPConnection receiveCon;

    private boolean connected;

    public WiiMote(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public boolean isConnected() {
        return connected;
    }

    /** 
     * Creates the two needed connections to send and receive commands
     * to and from the wiimote-device.
     * 
     */
    public synchronized void connect() throws IOException {
        this.controlCon = (L2CAPConnection) Connector.open("btl2cap://" +
                this.address + ":11;authenticate=false;encrypt=false;master=false",
                Connector.WRITE); // 11
        this.receiveCon = (L2CAPConnection) Connector.open("btl2cap://" +
                this.address + ":13;authenticate=false;encrypt=false;master=false",
                Connector.READ); // 13
        connected = true;
    }

    /**
     * Disconnects the wiimote and closes the two connections.
     */
    public synchronized void disconnect() {
        try {
            this.controlCon.close();
            this.receiveCon.close();
            System.err.println("Connection to wiimote closed.");
        } catch (Exception e) {
            System.err.println("Lost connection to wiimote.");
            e.printStackTrace(System.err);
        }
        connected = false;
    }

    public void enableIR(ReportMode reportMode, IRMode iRMode, IRSensitivity sensitivity) throws IOException {
        readMemory(new byte[]{0x00, 0x00, 0x20}, new byte[]{0x00, 0x07});
        // 1. Enable IR Camera (Send 0x04 to Output Report 0x13)
        sendRaw(new byte[]{CMD_SET_REPORT, 0x13, 0x04});
        // 2. Enable IR Camera 2 (Send 0x04 to Output Report 0x1a)
        sendRaw(new byte[]{CMD_SET_REPORT, 0x1a, 0x04});
        // 3. Write 0x08 to register 0xb00030
        writeRegister(new byte[]{(byte) 0xb0, 0x00, 0x30}, new byte[]{0x08});
        // 4. Write Sensitivity Block 1 to registers at 0xb00000
        writeRegister(new byte[]{(byte) 0xb0, 0x00, 0x00}, sensitivity.getBlock1());
        // 5. Write Sensitivity Block 2 to registers at 0xb0001a
        writeRegister(new byte[]{(byte) 0xb0, 0x00, (byte) 0x1a}, sensitivity.getBlock2());
        // 6. Write Mode Number to register 0xb00033
        writeRegister(new byte[]{(byte) 0xb0, 0x00, 0x33}, new byte[]{iRMode.getCode()});
        // 7. Write 0x08 to register 0xb00030 again
        writeRegister(new byte[]{(byte) 0xb0, 0x00, 0x30}, new byte[]{0x08});
        // enable continuous acceleration and IR cam on channel 33
        sendRaw(new byte[]{CMD_SET_REPORT, 0x12, 0x00, reportMode.getCode()});
    }

    public void disableIR() throws IOException {
        // 1. Disable IR Camera
        sendRaw(new byte[]{CMD_SET_REPORT, 0x13, 0x00});
        // 2. Disable IR Camera 2
        sendRaw(new byte[]{CMD_SET_REPORT, 0x1a, 0x00});
    }

    public void getRaw(byte[] buffer) throws IOException {
        while (!this.receiveCon.ready()) {
            try {
                Thread.sleep(50);
                //sleep a bit to allow the connection to get ready
            } catch (Exception e) {}
        }
        this.receiveCon.receive(buffer);
    }

    public void sendRaw(byte[] raw) throws IOException {
        this.controlCon.send(raw);
        try {
            Thread.sleep(50l);
        } catch (InterruptedException e) {
        }
    }

    public void writeRegister(byte[] offset, byte[] payload) throws IOException {
        byte[] data = new byte[23];
        data[0] = CMD_SET_REPORT;
        data[1] = 0x16; // Write channel
        data[2] = 0x04; // Register
        System.arraycopy(offset, 0, data, 3, 3);
        data[6] = (byte) payload.length;
        System.arraycopy(payload, 0, data, 7, payload.length);
        this.sendRaw(data);
    }

    public void readMemory(byte[] offset, byte[] size) throws IOException {
        byte[] raw = new byte[8];
        raw[0] = CMD_SET_REPORT;
        raw[1] = 0x17; // Read channel
        raw[2] = 0x00; // EEPROM
        System.arraycopy(offset, 0, raw, 3, offset.length);
        System.arraycopy(size, 0, raw, 6, size.length);
        this.sendRaw(raw);
    }

    public void setLED(int value) throws IOException {
        if (value < 16 && value > 0) {
            byte tmp = (byte) value;
            byte ledencoding = (byte) (tmp << 4);
            this.sendRaw(new byte[]{CMD_SET_REPORT, 0x11, ledencoding});
        }
    }

    public <T extends IRListener> void addIRListener(T listener) {
        if (listener == null) {
            return;
        }
        listenerList.add((Class<T>)listener.getClass(), listener);
    }

    public <T extends IRListener> void removeIRListener(T listener) {
        listenerList.remove((Class<T>) listener.getClass(), listener);
    }

    protected <T extends IRListener> void fireIREvent(Class<T> c, IREvent event) {
        IRListener[] listeners = listenerList.getListeners(c);
        //System.out.println("Listeners: " + listeners.length);
        for (IRListener listener : listeners) {
            //System.out.println("list: " + listener);
            listener.received(event);
        }
    }

    public void parseData(byte[] buf) {
        ReportMode mode = ReportMode.getReportMode(buf[1]);
        if (mode == null) {
            return;
        }
        parseIR(mode.getListener(), mode.getEvent(), buf, null, mode.getOffset());
    }

    private void parseIR(Class<? extends IRListener> mode, Class<? extends IREvent> event, byte[] block_1, byte[] block_2, int offset) {
        try {
            byte[] trimmed = trim(block_1, offset);
            IREvent eventInstance = event.newInstance();
            //System.err.println("SRC INIT: " + eventInstance.getSource());
            eventInstance.setSource(this);
            eventInstance.setReport_0(trimmed);
            eventInstance.setReport_1(block_2);
            fireIREvent(mode, eventInstance);
        } catch (InstantiationException ex) {
            Logger.getLogger(WiiMote.class.getName()).log(Level.SEVERE, "Cannot parse IR event.", ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(WiiMote.class.getName()).log(Level.SEVERE, "Cannot parse IR event.", ex);
        }
    }

    private byte[] trim(byte[] data, int offset) {
        byte[] report = new byte[data.length - offset];
        System.arraycopy(data, offset, report, 0, report.length);
        //System.out.println("data: " + Arrays.toString(byte2hex(data)) + " --> " + Arrays.toString(byte2hex(report)));
        return report;
    }
    private ReceiverThread receiver;

    public void startReceiver() {
        if (receiver == null || !receiver.isAlive()) {
            receiver = new ReceiverThread(this);
        }
        if (!receiver.isAlive()) {
            receiver.start();
        }
    }

    public void stopReceiver() {
        if (receiver != null && receiver.isAlive()) {
            receiver.close();
            receiver = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WiiMote other = (WiiMote) obj;
        if (this.address != other.address && (this.address == null || !this.address.equals(other.address))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.address != null ? this.address.hashCode() : 0);
        return hash;
    }
}
