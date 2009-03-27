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

import java.io.Serializable;

/**
 *
 * @author Giampaolo Melis
 */
public abstract class IRSpot implements Serializable {

    private static final double MAX_X = 1023.0;
    private static final double MAX_Y = 767.0;
    private static final double MAX_SIZE = 15.0;
    private static final double MAX_INTENSITY = 255.0;
    
    private double x;
    private double y;
    private double size;
    private double xMin;
    private double yMin;
    private double xMax;
    private double yMax;
    private double intensity;
    private boolean present;

    public IRSpot() {
        this.x = -1.0;
        this.y = -1.0;
        this.size = -1.0;
        this.xMin = -1.0;
        this.yMin = -1.0;
        this.xMax = -1.0;
        this.yMax = -1.0;
        this.intensity = -1.0;
        this.present = false;
    }
    
    protected void init(int x, int y) {
        this.x = (double)x / MAX_X;
        this.y = (double)y / MAX_Y;
    }

    protected void init(int size) {
        this.size = (double)size / MAX_SIZE;
    }

    protected void init(int xMin, int yMin, int xMax, int yMax, int intensity) {
        this.xMin = (double)xMin / MAX_X;
        this.yMin = (double)yMin / MAX_Y;
        this.xMax = (double)xMax / MAX_X;
        this.yMax = (double)yMax / MAX_Y;
        this.intensity = (double)intensity / MAX_INTENSITY;
    }
    
    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getXMax() {
        return xMax;
    }

    public void setXMax(double xMax) {
        this.xMax = xMax;
    }

    public double getXMin() {
        return xMin;
    }

    public void setXMin(double xMin) {
        this.xMin = xMin;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getYMax() {
        return yMax;
    }

    public void setYMax(double yMax) {
        this.yMax = yMax;
    }

    public double getYMin() {
        return yMin;
    }

    public void setYMin(double yMin) {
        this.yMin = yMin;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IRSpot other = (IRSpot) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (this.xMin != other.xMin) {
            return false;
        }
        if (this.yMin != other.yMin) {
            return false;
        }
        if (this.xMax != other.xMax) {
            return false;
        }
        if (this.yMax != other.yMax) {
            return false;
        }
        if (this.intensity != other.intensity) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.size) ^ (Double.doubleToLongBits(this.size) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.xMin) ^ (Double.doubleToLongBits(this.xMin) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.yMin) ^ (Double.doubleToLongBits(this.yMin) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.xMax) ^ (Double.doubleToLongBits(this.xMax) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.yMax) ^ (Double.doubleToLongBits(this.yMax) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.intensity) ^ (Double.doubleToLongBits(this.intensity) >>> 32));
        return hash;
    }
}
