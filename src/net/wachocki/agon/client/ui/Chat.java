package net.wachocki.agon.client.ui;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.client.entity.Player;
import net.wachocki.agon.common.network.Network;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 9:07 PM
 */
public class Chat {

    private GameClient game;
    private GameContainer gameContainer;
    private LinkedList<Message> messages;
    private long lastUpdated;
    private TextField textField;
    private boolean textFieldVisible = false;

    public Chat(GameClient game, GameContainer gameContainer) {
        this.game = game;
        this.gameContainer = gameContainer;
        this.messages = new LinkedList<Message>();
        textField = new TextField(gameContainer, gameContainer.getDefaultFont(), 10, gameContainer.getHeight() - 30, 200, 24);
        textField.addListener(new ComponentListener() {
            @Override
            public void componentActivated(AbstractComponent source) {
                toggleTextField();
            }
        });
    }

    public LinkedList<Message> getMessages() {
        return messages;
    }

    public void sendMessage(String message) {
        Network.SendMessage sendMessage = new Network.SendMessage();
        sendMessage.playerName = game.getPlayer().getName();
        sendMessage.message = message;
        game.getClient().sendTCP(sendMessage);
    }

    public void newMessage(Message message) {
        messages.add(message);
        lastUpdated = System.currentTimeMillis();
    }

    public void toggleTextField() {
        textFieldVisible = !textFieldVisible;
        if (textFieldVisible) {
            textField.setFocus(true);
        } else {
            if (textField.getText().trim().length() > 0) {
                sendMessage(textField.getText().trim());
                textField.setText("");
            }
            textField.setFocus(false);
        }
    }

    public void render() {
        if (textFieldVisible) {
            textField.render(gameContainer, gameContainer.getGraphics());
        }
        if (System.currentTimeMillis() - lastUpdated < game.getSettings().getChatIdleLimit()) {
            gameContainer.getGraphics().setColor(Color.white);
            int lines = 0;
            Font originalFont = gameContainer.getGraphics().getFont();
            gameContainer.getGraphics().setFont(game.getSettings().getChatFont());
            ListIterator iterator = ((LinkedList<Message>) messages.clone()).listIterator(messages.size());
            while (iterator.hasPrevious()) {
                Message message = (Message) iterator.previous();
                if (lines == game.getSettings().getMaxChatMessages()) {
                    break;
                }
                int parts = (int) Math.ceil((double) message.getMessage().length() / 30);
                String tempMessage = message.getMessage();
                String[] messageParts = new String[parts];
                for (int i = 0; i < messageParts.length; i++) {
                    if (tempMessage.length() >= 30) {
                        messageParts[i] = tempMessage.substring(0, 30);
                        tempMessage = tempMessage.substring(30, tempMessage.length());
                    } else {
                        messageParts[i] = tempMessage.substring(0, tempMessage.length());
                        tempMessage = "";
                    }
                }
                for (int i = messageParts.length - 1; i >= 0 && lines != game.getSettings().getMaxChatMessages(); i--) {
                    gameContainer.getGraphics().drawString((i == 0 ? "[" + message.getSenderName() + "] " : "") + messageParts[i], 10,
                            gameContainer.getHeight() - 30 - ((lines + 1) * 20));
                    lines++;
                }
            }
            gameContainer.getGraphics().setFont(originalFont);
        }
    }

    public static class Message {

        private Player sender;
        private String senderName;
        private String message;

        public Message(Player sender, String message) {
            this.sender = sender;
            this.senderName = sender.getName();
            this.message = message;
        }

        public Message(String senderName, String message) {
            this.senderName = senderName;
            this.message = message;
        }

        public Player getSender() {
            return sender;
        }

        public String getMessage() {
            return message;
        }

        public String getSenderName() {
            return senderName;
        }
    }

}
