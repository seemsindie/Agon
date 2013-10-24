package net.wachocki.agon.client.camera;

/**
 * User: Marty
 * Date: 9/16/13
 * Time: 11:36 PM
 */
public class Camera {

    private int x = 0;

    private int y = 0;

    private float zoom = 1F;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

}
