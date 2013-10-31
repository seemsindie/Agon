package net.wachocki.agon.client.input;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.client.entity.GroundItem;
import net.wachocki.agon.client.interfaces.Interactable;
import net.wachocki.agon.client.ui.ContextMenu;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.MouseButtonControl;
import org.newdawn.slick.geom.Rectangle;
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
            game.getCamera().setX(game.getCamera().getX() - game.getSettings().getScrollSpeed());
        }
        if (Mouse.getX() >= gameContainer.getWidth() - 1) {
            game.getCamera().setX(game.getCamera().getX() + game.getSettings().getScrollSpeed());
        }
        if (Mouse.getY() <= 0) {
            game.getCamera().setY(game.getCamera().getY() + game.getSettings().getScrollSpeed());
        }
        if (Mouse.getY() >= gameContainer.getHeight() - 1) {
            game.getCamera().setY(game.getCamera().getY() - game.getSettings().getScrollSpeed());
        }
        if (Mouse.isButtonDown(0)) {
            minimapView(game, gameContainer);
        }
        ContextMenu contextMenu = game.getContextMenu();
        if (contextMenu != null) {
            Vector2f mousePositionScreen = new Vector2f(Mouse.getX(), (gameContainer.getHeight() - Mouse.getY()));
            if (!contextMenu.getBounds().contains(mousePositionScreen.getX(), mousePositionScreen.getY())) {
                game.setContextMenu(null);
            } else {
                float optionHeight = contextMenu.getHeight() / contextMenu.getActions().length;
                float relativeY = mousePositionScreen.getY() - contextMenu.getPosition().getY();
                contextMenu.setSelectedIndex((int) (relativeY / optionHeight));
            }
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
        Vector2f mousePositionScreen = new Vector2f(Mouse.getX(), (gameContainer.getHeight() - Mouse.getY()));
        Vector2f mousePositionWorld = getMousePositionWorld(game, gameContainer);
        if (command == MIDDLE_MOUSE_CLICK) {
            //game.getCamera().setZoom(0.2F);
        }
        if (command == LEFT_CLICK) {
            minimapView(game, gameContainer);

            ContextMenu contextMenu = game.getContextMenu();
            if (contextMenu != null) {
                if (contextMenu.getBounds().contains(mousePositionScreen.getX(), mousePositionScreen.getY())) {
                    contextMenu.performAction(contextMenu.getSelectedIndex());
                    game.setContextMenu(null);
                }
            } else {
                for (GroundItem groundItem : game.getGroundItems().values()) {
                    if (groundItem instanceof Interactable) {
                        Rectangle bounds = groundItem.getScreenBounds(game);
                        if (bounds != null && bounds.contains(mousePositionScreen.getX(), mousePositionScreen.getY())) {
                            contextMenu = new ContextMenu(game, gameContainer, new Vector2f(mousePositionScreen.getX() - 2, mousePositionScreen.getY() - 2), groundItem,
                                    groundItem.getActions());
                            game.setContextMenu(contextMenu);
                            break;
                        }
                    }
                }
            }

        }
        if (command == RIGHT_CLICK) {
            game.getPlayer().requestWalk(game, mousePositionWorld);
        }
    }

    @Override
    public void controlReleased(Command command) {

    }

    public static void minimapView(GameClient game, GameContainer gameContainer) {
        int minimapX = gameContainer.getWidth() - game.getSettings().getMinimapSize();
        int minimapY = gameContainer.getHeight() - game.getSettings().getMinimapSize();
        if (Mouse.getX() >= minimapX && Mouse.getY() >= minimapY) {
            int mapWidth = game.getTileMap().getTileWidth() * game.getTileMap().getWidth();
            int mapHeight = game.getTileMap().getTileHeight() * game.getTileMap().getHeight();
            int offsetX = Math.min(mapWidth, gameContainer.getWidth()) / 2;
            int offsetY = Math.min(mapHeight, gameContainer.getHeight()) / 2;
            float scaleX = (float) game.getSettings().getMinimapSize() / mapWidth;
            float scaleY = (float) game.getSettings().getMinimapSize() / mapHeight;
            game.getCamera().setX((int) ((Mouse.getX() - minimapX) / scaleX - offsetX));
            game.getCamera().setY((int) ((gameContainer.getHeight() - Mouse.getY()) / scaleY - offsetY));
        }
    }

    public static Vector2f getMousePositionWorld(GameClient game, GameContainer gameContainer) {
        return game.getCamera().screenToWorld(new Vector2f(game.getCamera().getX() + Mouse.getX(), game.getCamera().getY() + (gameContainer.getHeight() - Mouse.getY())));
    }

}
