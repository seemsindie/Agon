package net.wachocki.agon.server.entity;

import org.newdawn.slick.geom.Vector2f;

import java.util.LinkedList;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class LivingEntity extends Entity {

    private Vector2f destination;
    private LinkedList<Vector2f> walkingQueue = new LinkedList<Vector2f>();

    public LivingEntity(String name) {
        super(name);
    }

    public Vector2f getDestination() {
        return destination;
    }

    public void setDestination(Vector2f destination) {
        this.destination = destination;
    }

    public LinkedList<Vector2f> getWalkingQueue() {
        return walkingQueue;
    }
}
