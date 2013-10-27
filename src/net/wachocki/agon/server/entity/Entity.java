package net.wachocki.agon.server.entity;

import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/25/13
 * Time: 12:16 PM
 */
public class Entity {

    private String name;
    private Vector2f position;
    private int maxHealth = 100;
    private int health;
    private long lastHit;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getHealthPercentage() {
        return (double) health / (double) maxHealth * 100;
    }

    public void inflictDamage(int damage) {
        health = (health - damage) < 0 ? 0 : (health - damage);
    }

    public long getLastHit() {
        return lastHit;
    }

    public void setLastHit(long lastHit) {
        this.lastHit = lastHit;
    }

}

