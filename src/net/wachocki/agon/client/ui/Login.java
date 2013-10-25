package net.wachocki.agon.client.ui;

import com.esotericsoftware.kryonet.Client;
import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.types.GameState;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;

import java.io.IOException;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:44 AM
 */
public class Login {

    private TextField textField;
    private GameClient game;
    private GameContainer gameContainer;
    private String errorMessage;

    public Login(final GameClient game, final GameContainer gameContainer) {
        this.game = game;
        this.gameContainer = gameContainer;
        this.textField = new TextField(gameContainer, gameContainer.getDefaultFont(), gameContainer.getWidth() / 2 - 100, gameContainer.getHeight() / 2 - 50, 200, 24);
        this.textField.setBackgroundColor(Color.white);
        this.textField.setBorderColor(Color.black);
        this.textField.setTextColor(Color.black);
        this.textField.setMaxLength(20);
        this.textField.addListener(new ComponentListener() {
            @Override
            public void componentActivated(AbstractComponent source) {
                if (game.getGameState() == GameState.LOGIN) {
                    login();
                }
            }
        });

        this.textField.setText("Marty");
        this.textField.setCursorPos(this.textField.getText().length());
    }

    public void render() {
        if (errorMessage == null) {
            gameContainer.getGraphics().drawString("Please login.", textField.getX(), textField.getY() - 20);
        } else {
            gameContainer.getGraphics().drawString(errorMessage, textField.getX(), textField.getY() - 20);
        }
        if (!textField.hasFocus()) {
            textField.setFocus(true);
        }
        textField.render(gameContainer, gameContainer.getGraphics());
    }

    public void login() {
        final String name = textField.getText().trim();
        if (name.length() > 0 && name.matches("^[A-Za-z0-9_]+$")) {
            try {
                game.setClient(new Client());
                game.getClient().start();
                game.getClient().addListener(game.getNetworkListener());
                Network.register(game.getClient());
                game.getClient().connect(5000, Network.HOST, Network.PORT, Network.PORT);
                if (game.getClient().isConnected()) {
                    Network.LoginRequest loginRequest = new Network.LoginRequest();
                    loginRequest.name = name;
                    game.getClient().sendUDP(loginRequest);
                    game.setPlayerName(loginRequest.name);
                } else {
                    errorMessage = "Error connecting to server.";
                }
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = "Error connecting to server.";
            }
        } else {
            errorMessage = "Invalid name.";
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TextField getTextField() {
        return textField;
    }
}
