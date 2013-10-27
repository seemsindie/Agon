package net.wachocki.agon.client.entity;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.common.network.Network;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class LivingEntity extends Entity {

    private Vector2f destination;

    public LivingEntity(String name) {
        super(name);
    }

    public Vector2f getDestination() {
        return destination;
    }

    public void setDestination(Vector2f destination) {
        this.destination = destination;
    }

    public void walk(GameClient game, int delta) {
        if (game.getPlayer() == this) {
            if (!game.getWalkingQueue().isEmpty()) {
                destination = game.getWalkingQueue().getFirst();
                if (game.getWalkingQueue().getFirst().distance(getPosition()) < 1) {
                    game.getWalkingQueue().pop();
                    Network.UpdatePosition updatePosition = new Network.UpdatePosition();
                    updatePosition.playerName = getName();
                    updatePosition.position = getPosition();
                    updatePosition.destination = !game.getWalkingQueue().isEmpty() ? game.getWalkingQueue().getFirst() : null;
                    game.getClient().sendUDP(updatePosition);
                    return;
                }
            }
        }
        if (getDestination() != null) {
            if (getPosition().distance(getDestination()) > 0.1) {
                Vector2f direction = new Vector2f(getDestination().getX() - getPosition().getX(), getDestination().getY() - getPosition().getY());
                float speed = 0.005f * delta;
                double x = getPosition().getX() + (speed * Math.cos(Math.toRadians(direction.getTheta())));
                double y = getPosition().getY() + (speed * Math.sin(Math.toRadians(direction.getTheta())));
                setPosition(new Vector2f((float) x, (float) y));
            }
        }
    }


}
