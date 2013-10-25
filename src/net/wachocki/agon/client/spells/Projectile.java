package net.wachocki.agon.client.spells;

import net.wachocki.agon.client.camera.Camera;
import net.wachocki.agon.client.entity.LivingEntity;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/19/13
 * Time: 11:43 PM
 */
public class Projectile {

    private Vector2f startPosition;
    private Vector2f position;
    private LivingEntity livingEntityTarget;
    private Vector2f positionTarget;
    private Image image;
    private double speed;
    private int maxDistance;
    private int distanceTraveled;

    public Projectile(Vector2f startPosition, LivingEntity livingEntityTarget, Image image, double speed, int maxDistance) {
        this.startPosition = startPosition;
        this.position = startPosition;
        this.livingEntityTarget = livingEntityTarget;
        this.image = image;
        this.speed = speed;
        this.maxDistance = maxDistance;
    }

    public Projectile(Vector2f startPosition, Vector2f positionTarget, Image image, double speed, int maxDistance) {
        this.startPosition = startPosition;
        this.position = startPosition;
        this.positionTarget = positionTarget;
        this.image = image;
        float deltaX = startPosition.getX() - positionTarget.getX();
        float deltaY = startPosition.getY() - positionTarget.getY();
        image.rotate((int) (180 + Math.toDegrees(Math.atan2(deltaY, deltaX))) % 360);
        this.speed = speed;
        this.maxDistance = maxDistance;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Vector2f startPosition) {
        this.startPosition = startPosition;
    }

    public Vector2f getPositionTarget() {
        return positionTarget;
    }

    public void setPositionTarget(Vector2f positionTarget) {
        this.positionTarget = positionTarget;
    }

    public LivingEntity getLivingEntityTarget() {
        return livingEntityTarget;
    }

    public void setLivingEntityTarget(LivingEntity livingEntityTarget) {
        this.livingEntityTarget = livingEntityTarget;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(int distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void render(Camera camera) {
        image.draw(position.getX() - camera.getX(), position.getY() - camera.getY());
    }

}
