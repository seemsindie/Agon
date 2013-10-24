package net.wachocki.agon.client.input;

import net.wachocki.agon.client.GameClient;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.KeyControl;

/**
 * User: Marty
 * Date: 9/16/13
 * Time: 11:37 PM
 */
public class KeyboardInput extends Input {

    private static Command SPELL_Q = new BasicCommand("spell_q");
    private static Command SPELL_W = new BasicCommand("spell_w");
    private static Command SPELL_E = new BasicCommand("spell_e");
    private static Command SPELL_R = new BasicCommand("spell_r");
    private static Command SPELL_D = new BasicCommand("spell_d");
    private static Command SPELL_F = new BasicCommand("spell_f");

    private static Command ENTER = new BasicCommand("enter");

    public KeyboardInput(GameClient game, GameContainer gameContainer) {
        super(game, gameContainer);
    }

    public void poll() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            game.getCamera().setX(game.getCamera().getX() - game.getSettings().getZoomSpeed());
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            game.getCamera().setX(game.getCamera().getX() + game.getSettings().getZoomSpeed());
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            game.getCamera().setY(game.getCamera().getY() + game.getSettings().getZoomSpeed());
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            game.getCamera().setY(game.getCamera().getY() - game.getSettings().getZoomSpeed());
        }
    }

    public void bind() {
        InputProvider provider = new InputProvider(gameContainer.getInput());
        provider.addListener(this);

        provider.bindCommand(new KeyControl(Keyboard.KEY_Q), SPELL_Q);
        provider.bindCommand(new KeyControl(Keyboard.KEY_W), SPELL_W);
        provider.bindCommand(new KeyControl(Keyboard.KEY_E), SPELL_E);
        provider.bindCommand(new KeyControl(Keyboard.KEY_R), SPELL_R);
        provider.bindCommand(new KeyControl(Keyboard.KEY_D), SPELL_D);
        provider.bindCommand(new KeyControl(Keyboard.KEY_F), SPELL_F);

        provider.bindCommand(new KeyControl(Keyboard.KEY_RETURN), ENTER);
    }

    @Override
    public void controlPressed(Command command) {

    }

    @Override
    public void controlReleased(Command command) {

    }

}