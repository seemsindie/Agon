package net.wachocki.agon.client.ui;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.common.network.Network;
import org.newdawn.slick.GameContainer;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 1:01 PM
 */
public class Loading {
    private GameClient game;
    private GameContainer gameContainer;
    private String message = "Loading...";

    public Loading(final GameClient game, final GameContainer gameContainer) {
        this.game = game;
        this.gameContainer = gameContainer;
    }

    public void render() {
        gameContainer.getGraphics().drawString(message, gameContainer.getWidth() / 2 - 50, gameContainer.getHeight() / 2);
    }

    public void request() {
        Network.MapRequest mapRequest = new Network.MapRequest();
        mapRequest.playerName = game.getPlayer().getName();
        game.getClient().sendTCP(mapRequest);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
