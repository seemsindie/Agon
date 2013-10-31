package net.wachocki.agon.client.ui;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.client.entity.GroundItem;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 5:01 PM
 */
public class ContextMenu {

    private GameClient game;
    private GameContainer gameContainer;
    private Vector2f position;
    private String[] actions;
    private Object object;
    private int selectedIndex;
    private int width;
    private int height;

    public ContextMenu(GameClient game, GameContainer gameContainer, Vector2f position, Object object, String[] actions) {
        this.game = game;
        this.gameContainer = gameContainer;
        this.position = position;
        this.object = object;
        this.actions = actions;
        for(String option : actions) {
            int width = gameContainer.getGraphics().getFont().getWidth(option);
            if(width > this.width) {
                this.width = width;
            }
        }
        this.width += 10;
        this.height = 22 * actions.length;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

    public Rectangle getBounds() {
        return new Rectangle(position.getX(), position.getY(), width, height);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void performAction(int index) {
        if(object instanceof GroundItem) {
            GroundItem groundItem = (GroundItem) object;
            groundItem.doAction(game, index);
        }
    }

    public void render() {
        float x = position.getX();
        float y = position.getY();
        gameContainer.getGraphics().setColor(Color.brown);
        gameContainer.getGraphics().fillRect(x, y, width, height);
        for(int i = 0; i < actions.length; i++) {
            if(i == selectedIndex) {
                gameContainer.getGraphics().setColor(Color.lightBrown);
                gameContainer.getGraphics().fillRect(x, y, width, 22);
            }
            gameContainer.getGraphics().setColor(Color.black);
            gameContainer.getGraphics().drawRect(x, y, width, 22);
            gameContainer.getGraphics().setColor(Color.white);
            gameContainer.getGraphics().drawString(actions[i], x + 4, y + 2);
            y += 22;
        }
    }

}
