package net.wachocki.agon.client.input;

import net.wachocki.agon.client.GameClient;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.MouseButtonControl;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

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
        Vector2f mousePositionWorld = getMousePositionWorld(game, gameContainer);
        if (command == MIDDLE_MOUSE_CLICK) {
            //game.getCamera().setZoom(0.2F);
        }
        if (command == LEFT_CLICK) {
            minimapView(game, gameContainer);
        }
        if (command == RIGHT_CLICK) {
            game.getWalkingQueue().clear();
            AStarPathFinder pathFinder = new AStarPathFinder(game.getCollisionMap(), 500, true);
            Path path = pathFinder.findPath(null, (int) game.getPlayer().getPosition().getX(), (int) game.getPlayer().getPosition().getY(), (int) mousePositionWorld.getX(),
                    (int) mousePositionWorld.getY());
            if (path != null) {
                int length = path.getLength();
                for (int i = 0; i < length; i++) {
                    game.getWalkingQueue().add(new Vector2f(path.getX(i), path.getY(i)));
                }
                game.getWalkingQueue().set(game.getWalkingQueue().size() - 1, new Vector2f(mousePositionWorld.getX(), mousePositionWorld.getY()));
            }
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
