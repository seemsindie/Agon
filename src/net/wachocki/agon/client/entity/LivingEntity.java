package net.wachocki.agon.client.entity;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.geom.Vector2f;

import java.util.LinkedList;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class LivingEntity extends Entity{

    private LinkedList<Vector2f> walkingQueue = new LinkedList<Vector2f>();

    public LivingEntity(String name) {
        super(name);
    }

    public LinkedList<Vector2f> getWalkingQueue() {
        return walkingQueue;
    }

    public void setWalkingQueue(LinkedList<Vector2f> walkingQueue) {
        this.walkingQueue = walkingQueue;
    }

    public void render(GameClient game) {
        if(this instanceof Player) {
            Player player = (Player) this;
            game.getSpritesSheets().get(player.getSpecialization()).getSprite(0, 0).draw(getPosition().getX() - game.getCamera().getX(), getPosition().getY() - game.getCamera().getY());
        }
    }

}
