package net.wachocki.agon.client.ui;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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
        this.mapWidth = game.getTileMap().getTileWidth() * game.getTileMap().getWidth();
        this.mapHeight = game.getTileMap().getTileHeight() * game.getTileMap().getHeight();
        this.camRect = new Rectangle(0, 0, 0, 0);
        this.filterColor = new Color(1, 1, 1, 1f);
        this.game = game;
        this.gameContainer = gameContainer;
    }

    public void render() throws SlickException {
        float xPos = gameContainer.getWidth() - game.getSettings().getMinimapSize();
        float yPos = 0; //gameContainer.getHeight() - game.getSettings().getMinimapSize();

        gameContainer.getGraphics().setColor(Color.black);
        gameContainer.getGraphics().fillRect(xPos - 2, yPos, game.getSettings().getMinimapSize() + 2, game.getSettings().getMinimapSize() + 2);

        camRect.setWidth(Math.min(mapWidth, gameContainer.getWidth()));
        camRect.setHeight(Math.min(mapHeight, gameContainer.getHeight()));
        gameContainer.getGraphics().pushTransform();

        gameContainer.getGraphics().translate(xPos, yPos);
        if (img != null) {
            img.draw(0, 0, filterColor);
        }

        float scaleX = (float) game.getSettings().getMinimapSize() / this.mapWidth;
        float scaleY = (float) game.getSettings().getMinimapSize() / this.mapHeight;
        gameContainer.getGraphics().scale(scaleX, scaleY);
        if (img == null) {
            game.getTileMap().render(0, 0);
            img = new Image(game.getSettings().getMinimapSize(), game.getSettings().getMinimapSize());
            gameContainer.getGraphics().copyArea(img, (int) xPos, (int) yPos);
        }
        camRect.setLocation(game.getCamera().getX(), game.getCamera().getY());
        gameContainer.getGraphics().draw(camRect);

        gameContainer.getGraphics().popTransform();

    }

}
