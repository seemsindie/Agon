package net.wachocki.agon.client.ui;

import net.wachocki.agon.client.GameClient;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

/**
 * User: Marty
 * Date: 10/19/13
 * Time: 4:04 PM
 */
public class Cursor {

    private Image image;

    public Cursor(Image image) {
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void render(GameClient game, GameContainer gameContainer) {
        image.draw(Mouse.getX() / game.getCamera().getZoom(), (gameContainer.getHeight() - Mouse.getY()) / game.getCamera().getZoom(), image.getWidth() / 2,
                image.getHeight() / 2);
    }

}
