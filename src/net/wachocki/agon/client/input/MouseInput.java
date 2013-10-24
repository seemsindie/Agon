package net.wachocki.agon.client.input;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.common.network.Network;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.MouseButtonControl;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 9/16/13
 * Time: 11:37 PM
 */
public class MouseInput extends Input {

    private static Command LEFT_CLICK = new BasicCommand("left_click");
    private static Command MIDDLE_MOUSE_CLICK = new BasicCommand("middle_mouse_click");
    private static Command RIGHT_CLICK = new BasicCommand("right_click");

    public MouseInput(GameClient game, GameContainer gameContainer) {
        super(game, gameContainer);
    }

    public void poll() {
        float zoomAmount = Mouse.getDWheel() / 120 * 0.02F;
        if (zoomAmount != 0) {
            float newZoom = game.getCamera().getZoom() + (zoomAmount);
            if (newZoom >= 0.1F && newZoom <= 0.5F) {
                game.getCamera().setZoom(newZoom);
            }
        }
        if (Mouse.getX() <= 0) {
            game.getCamera().setX(game.getCamera().getX() - game.getSettings().getZoomSpeed());
        }
        if (Mouse.getX() >= gameContainer.getWidth() - 1) {
            game.getCamera().setX(game.getCamera().getX() + game.getSettings().getZoomSpeed());
        }
        if (Mouse.getY() <= 0) {
            game.getCamera().setY(game.getCamera().getY() + game.getSettings().getZoomSpeed());
        }
        if (Mouse.getY() >= gameContainer.getHeight() - 1) {
            game.getCamera().setY(game.getCamera().getY() - game.getSettings().getZoomSpeed());
        }
    }

    public void bind() {
        InputProvider provider = new InputProvider(gameContainer.getInput());
        provider.addListener(this);

        provider.bindCommand(new MouseButtonControl(0), LEFT_CLICK);
        provider.bindCommand(new MouseButtonControl(1), RIGHT_CLICK);
        provider.bindCommand(new MouseButtonControl(2), MIDDLE_MOUSE_CLICK);
    }

    @Override
    public void controlPressed(Command command) {
        Vector2f mousePosition = getMousePosition(game, gameContainer);
        if (command == MIDDLE_MOUSE_CLICK) {
            //game.getCamera().setZoom(0.2F);
        }
        if (command == LEFT_CLICK) {
            int minimapX = gameContainer.getWidth() - game.getSettings().getMinimapSize();
            int minimapY = gameContainer.getHeight() - game.getSettings().getMinimapSize();
            if (Mouse.getX() >= minimapX && Mouse.getY() >= minimapY) {

            }
        }
        if (command == RIGHT_CLICK) {
            Network.UpdateDestination updateDestination = new Network.UpdateDestination();
            updateDestination.playerName = game.getPlayer().getName();
            updateDestination.destination = mousePosition;
            game.getClient().sendUDP(updateDestination);
        }
    }

    @Override
    public void controlReleased(Command command) {

    }

    public static Vector2f getMousePosition(GameClient game, GameContainer gameContainer) {
        return new Vector2f(game.getCamera().getX() + Mouse.getX(), game.getCamera().getY() + (gameContainer.getHeight() - Mouse.getY()));
    }

}
