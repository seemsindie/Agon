package net.wachocki.agon.client.entity;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.types.EntityType;
import net.wachocki.agon.common.types.Specialization;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class Player extends LivingEntity {

    private Specialization specialization = Specialization.ARCHER;

    public Player(String name) {
        super(name);
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }


    @Override
    public void walk(GameClient game, int delta) {
        if (game.getPlayer() == this) {
            if (!game.getWalkingQueue().isEmpty()) {
                setDestination(game.getWalkingQueue().getFirst());
                if (game.getWalkingQueue().getFirst().distance(getPosition()) < 1) {
                    game.getWalkingQueue().pop();
                    Network.UpdatePosition updatePosition = new Network.UpdatePosition();
                    updatePosition.playerName = getName();
                    updatePosition.position = getPosition();
                    updatePosition.destination = !game.getWalkingQueue().isEmpty() ? game.getWalkingQueue().getFirst() : null;
                    updatePosition.entityTargetType = EntityType.NONE;
                    if(game.getTargetEntity() != null) {
                        if(game.getTargetEntity() instanceof GroundItem) {
                            GroundItem groundItem = (GroundItem) game.getTargetEntity();
                            updatePosition.entityTargetId = groundItem.getGroundId();
                            updatePosition.entityTargetType = EntityType.GROUND_ITEM;
                            updatePosition.entityTargetAction = game.getTargetEntityActionIndex();
                        }
                    }
                    game.getClient().sendUDP(updatePosition);
                    return;
                }
            }
        }
        super.walk(game, delta);
    }
}
