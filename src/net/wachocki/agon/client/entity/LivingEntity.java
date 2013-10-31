package net.wachocki.agon.client.entity;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.client.interfaces.Interactable;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class LivingEntity extends Entity implements Interactable {

    private Vector2f destination;
    private int maxHealth = 100;
    private int health;
    private long lastHit;

    public LivingEntity(String name) {
        super(name);
    }

    public Vector2f getDestination() {
        return destination;
    }

    public void setDestination(Vector2f destination) {
        this.destination = destination;
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

    public void requestWalk(GameClient game, Vector2f position) {
        game.setTargetEntity(null);
        game.setTargetEntityActionIndex(-1);
        game.getWalkingQueue().clear();
        AStarPathFinder pathFinder = new AStarPathFinder(game.getCollisionMap(), 500, true);
        Path path = pathFinder.findPath(null, (int) game.getPlayer().getPosition().getX(), (int) game.getPlayer().getPosition().getY(), (int) position.getX(),
                (int) position.getY());
        if (path != null) {
            int length = path.getLength();
            for (int i = 0; i < length; i++) {
                game.getWalkingQueue().add(new Vector2f(path.getX(i), path.getY(i)));
            }
            game.getWalkingQueue().set(game.getWalkingQueue().size() - 1, new Vector2f(position.getX(), position.getY()));
        }
    }

    public void walk(GameClient game, int delta) {
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

    @Override
    public void render(GameClient game, GameContainer gameContainer) {
        super.render(game, gameContainer);
        Vector2f screenPosition = getScreenPosition(game);
        int hpBarWidth = getImage().getWidth();
        int hpBarHeight = 6;
        boolean displayHealthBar = System.currentTimeMillis() - lastHit < 10000;
        if (displayHealthBar) {
            double healthPercentage = getHealthPercentage() / 100;
            gameContainer.getGraphics().setColor(Color.green);
            gameContainer.getGraphics().fillRect(screenPosition.getX(), screenPosition.getY() - 15, (int) Math.ceil(hpBarWidth * healthPercentage), hpBarHeight);
            gameContainer.getGraphics().setColor(Color.red);
            gameContainer.getGraphics().fillRect(+(int) Math.ceil(hpBarWidth * healthPercentage), screenPosition.getY() - 15,
                    hpBarWidth - (int) Math.ceil(hpBarWidth * healthPercentage), hpBarHeight);
        }
        if (game.isDisplayNames()) {
            gameContainer.getGraphics().setColor(Color.white);
            gameContainer.getGraphics().drawString(getName(), screenPosition.getX(), screenPosition.getY() - 15);
        }
    }

    @Override
    public String[] getActions() {
        return new String[] { "Attack " + getName() };
    }

    @Override
    public void doAction(GameClient game, int action) {
    }
}
