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
public enum IRSensitivity {

    MARCAN( new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, (byte) 0xc0 },
            new byte[] { 0x40, 0}),
    CLIFF(  new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0xaa, 0x00, 0x64 },
            new byte[] { 0x63, 0x03}),
    INIO(   new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x41 },
            new byte[] { 0x40, 0x00}),
    LEVEL_1(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0x64, 0x00, (byte)0xfe },
            new byte[] { (byte) 0xfd, 0x05}),
    LEVEL_2(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0x96, 0x00, (byte)0xb4 },
            new byte[] { (byte) 0xb3, 0x04}),
    LEVEL_3(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0xaa, 0x00, 0x64 },
            new byte[] { 0x63, 0x03}),
    LEVEL_4(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0xc8, 0x00, 0x36 },
            new byte[] { 0x35, 0x03}),
    LEVEL_5(new byte[] { 0x07, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0x72, 0x00, 0x20 },
            new byte[] { 0x1f, 0x03});

    
    private final byte[] block1;
    private final byte[] block2;

    private IRSensitivity(byte[] block1, byte[] block2) {
        this.block1 = block1;
        this.block2 = block2;
    }

    public byte[] getBlock1() {
        return block1;
    }

    public byte[] getBlock2() {
        return block2;
    }
}
