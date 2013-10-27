package net.wachocki.agon.client.ui;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/25/13
 * Time: 7:57 PM
 */
public class GameMap {

    private float mapWidth;
    private float mapHeight;
    private GameClient game;
    private GameContainer gameContainer;
    private Image img;
    private Color filterColor;
    private boolean visible = false;

    public GameMap(GameClient game, GameContainer gameContainer) {
        this.mapWidth = game.getTileMap().getTileWidth() * game.getTileMap().getWidth();
        this.mapHeight = game.getTileMap().getTileHeight() * game.getTileMap().getHeight();
        this.filterColor = new Color(1, 1, 1, 1f);
        this.game = game;
        this.gameContainer = gameContainer;
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void render() throws SlickException {
        gameContainer.getGraphics().setColor(Color.black);
        gameContainer.getGraphics().pushTransform();

        gameContainer.getGraphics().translate(0, 0);
        if (img != null) {
            img.draw(0, 0, filterColor);
        }

        float scaleX = (float) gameContainer.getWidth() / this.mapWidth;
        float scaleY = (float) gameContainer.getHeight() / this.mapHeight;
        gameContainer.getGraphics().scale(scaleX, scaleY);
        if (img == null) {
            game.getTileMap().render(0, 0);
            img = new Image(gameContainer.getWidth(), gameContainer.getHeight());
            gameContainer.getGraphics().copyArea(img, 0, 0);
        }

        float mapPlayerWidth = 6 / scaleX;
        float mapPlayerHeight = 6 / scaleY;
        gameContainer.getGraphics().setColor(Color.red);
        Vector2f playerPosition = game.getCamera().worldToScreen(game.getPlayer().getPosition());
        gameContainer.getGraphics().fillOval(playerPosition.getX() - (mapPlayerWidth / 2), playerPosition.getY() - (mapPlayerHeight / 2), mapPlayerWidth,  mapPlayerHeight);
        gameContainer.getGraphics().popTransform();

    }

}
