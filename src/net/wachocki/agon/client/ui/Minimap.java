package net.wachocki.agon.client.ui;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: Marty
 * Date: 9/17/13
 * Time: 12:52 AM
 */
public class Minimap {

    private float mapWidth;
    private float mapHeight;
    private GameClient game;
    private GameContainer gameContainer;
    private Image img;
    private Rectangle camRect;
    private Color filterColor;

    public Minimap(GameClient game, GameContainer gameContainer) {
        this.mapWidth = game.getMap().getTileWidth() * game.getMap().getWidth();
        this.mapHeight = game.getMap().getTileHeight() * game.getMap().getHeight();
        this.camRect = new Rectangle(0, 0, 0, 0);
        this.filterColor = new Color(1, 1, 1, 1f);
        this.game = game;
        this.gameContainer = gameContainer;
    }

    public void render() throws SlickException {
        camRect.setWidth(Math.min(mapWidth, gameContainer.getWidth()));
        camRect.setHeight(Math.min(mapHeight, gameContainer.getHeight()));
        gameContainer.getGraphics().pushTransform();
        float xPos = gameContainer.getWidth() - game.getSettings().getMinimapSize();
        float yPos = gameContainer.getHeight() - game.getSettings().getMinimapSize();
        gameContainer.getGraphics().translate(xPos, yPos);
        if (img != null) {
            img.draw(0, 0, filterColor);
        }

        float scaleX = (float) game.getSettings().getMinimapSize() / this.mapWidth;
        float scaleY = (float) game.getSettings().getMinimapSize() / this.mapHeight;
        gameContainer.getGraphics().scale(scaleX, scaleY);

        if (img == null) {
            game.getMap().render(0, 0, 0);
            img = new Image(game.getSettings().getMinimapSize(), game.getSettings().getMinimapSize());
            gameContainer.getGraphics().copyArea(img, (int) xPos, (int) yPos);
        }
        camRect.setLocation(game.getCamera().getX(), game.getCamera().getY());
        gameContainer.getGraphics().draw(camRect);

        gameContainer.getGraphics().popTransform();
    }

}
