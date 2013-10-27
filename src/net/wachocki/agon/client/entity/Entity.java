package net.wachocki.agon.client.entity;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/25/13
 * Time: 12:14 PM
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

    public void render(GameClient game, GameContainer gameContainer) {
        float x = 0;
        float y = 0;
        int hpBarWidth = 35;
        int hpBarHeight = 6;
        if (this instanceof Player) {
            Player player = (Player) this;
            Vector2f position = game.getCamera().worldToScreen(player.getPosition());
            x = position.getX() - game.getCamera().getX();
            y = position.getY() - game.getCamera().getY();
            Image sprite = game.getSpritesSheets().get(player.getSpecialization()).getSprite(0, 0);
            hpBarWidth = sprite.getWidth();
            sprite.draw(x, y);
        }
        boolean displayHealthBar = System.currentTimeMillis() - lastHit < 10000;
        if (displayHealthBar) {
            double healthPercentage = getHealthPercentage() / 100;
            gameContainer.getGraphics().setColor(Color.green);
            gameContainer.getGraphics().fillRect(x, y - 15, (int) Math.ceil(hpBarWidth * healthPercentage), hpBarHeight);
            gameContainer.getGraphics().setColor(Color.red);
            gameContainer.getGraphics().fillRect(x + (int) Math.ceil(hpBarWidth * healthPercentage), y - 15, hpBarWidth - (int) Math.ceil(hpBarWidth * healthPercentage),
                    hpBarHeight);
        }
        if (game.isDisplayNames()) {
            gameContainer.getGraphics().drawString(getName(), x, y - 15);
        }
    }
}
