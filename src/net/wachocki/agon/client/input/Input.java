package net.wachocki.agon.client.input;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.InputProviderListener;

/**
 * User: Marty
 * Date: 9/16/13
 * Time: 11:37 PM
 */
public abstract class Input implements InputProviderListener {

    public GameContainer gameContainer;

    public GameClient game;

    public Input(GameClient game, GameContainer gameContainer) {
        this.game = game;
        this.gameContainer = gameContainer;
    }

    public abstract void poll();

    public abstract void bind();

}
