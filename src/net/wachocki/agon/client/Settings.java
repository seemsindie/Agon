package net.wachocki.agon.client;

/**
 * User: Marty
 * Date: 9/17/13
 * Time: 12:48 AM
 */
public class Settings {

    private int zoomSpeed = 15;

    private int minimapSize = 175;

    public int getZoomSpeed() {
        return zoomSpeed;
    }

    public void setZoomSpeed(int zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }

    public int getMinimapSize() {
        return minimapSize;
    }

    public void setMinimapSize(int minimapSize) {
        this.minimapSize = minimapSize;
    }
}
