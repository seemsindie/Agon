package net.wachocki.agon.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import net.wachocki.agon.client.entity.Player;
import net.wachocki.agon.client.ui.Chat;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.types.GameState;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 12:41 PM
 */
public class NetworkListener extends Listener {

    private GameClient game;

    public NetworkListener(GameClient game) {
        this.game = game;
    }


    @Override
    public void connected(Connection connection) {
        game.getClient().updateReturnTripTime();
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Connection lost.");
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Network.LoginResponse) {
            handleLoginResponse((Network.LoginResponse) object);
        } else if (object instanceof Network.MapResponse) {
            handleMapResponse((Network.MapResponse) object);
        } else if (object instanceof Network.AddPlayer) {
            handleAddPlayer((Network.AddPlayer) object);
        } else if (object instanceof Network.RemovePlayer) {
            handleRemovePlayer((Network.RemovePlayer) object);
        } else if (object instanceof FrameworkMessage.Ping) {
            handlePing(connection, (FrameworkMessage.Ping) object);
        } else if (object instanceof Network.SendMessage) {
            handleSendMessage((Network.SendMessage) object);
        } else if (object instanceof Network.UpdatePosition) {
            handleUpdatePosition((Network.UpdatePosition) object);
        }
    }

    public void handleSendMessage(Network.SendMessage sendMessage) {
        Chat.Message message;
        if (!sendMessage.playerName.equals("SERVER") || !game.getPlayers().containsKey(sendMessage.playerName)) {
            message = new Chat.Message(sendMessage.playerName, sendMessage.message);
        } else {
            message = new Chat.Message(game.getPlayers().get(sendMessage.playerName), sendMessage.message);
        }
        game.getChat().newMessage(message);
    }

    public void handlePing(Connection connection, FrameworkMessage.Ping ping) {
        if (ping.isReply) {
            //System.out.println(connection.getReturnTripTime());
            game.getClient().updateReturnTripTime();
        }
    }

    public void handleAddPlayer(Network.AddPlayer addPlayer) {
        Player player = new Player(addPlayer.playerName);
        player.setPosition(addPlayer.position);
        player.setSpecialization(addPlayer.specialization);
        player.setHealth(player.getMaxHealth());
        game.getPlayers().put(addPlayer.playerName, player);
        if (addPlayer.playerName.equals(game.getPlayerName())) {
            game.setPlayer(player);
        }
    }

    public void handleRemovePlayer(Network.RemovePlayer removePlayer) {
        if (game.getPlayers().containsKey(removePlayer.playerName)) {
            game.getPlayers().remove(removePlayer.playerName);
        }
    }

    public void handleUpdatePosition(Network.UpdatePosition updatePosition) {
        if (game.getPlayers().containsKey(updatePosition.playerName)) {
            game.getPlayers().get(updatePosition.playerName).setDestination(updatePosition.destination);
        }
    }

    public void handleMapResponse(final Network.MapResponse mapResponse) {
        game.getClient().addListener(new Listener() {
            int mapSize = mapResponse.totalSize;
            byte[] mapBytes = new byte[0];

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Network.MapChunk) {
                    Network.MapChunk mapChunk = (Network.MapChunk) object;
                    byte[] newBytes = new byte[mapBytes.length + mapChunk.bytes.length];
                    System.arraycopy(mapBytes, 0, newBytes, 0, mapBytes.length);
                    System.arraycopy(mapChunk.bytes, 0, newBytes, mapBytes.length, mapChunk.bytes.length);
                    mapBytes = newBytes;
                    if (mapBytes.length == mapSize) {
                        game.setMapBytes(mapBytes);
                        game.setGameState(GameState.INGAME);
                        connection.removeListener(this);
                    }
                }
            }
        });
    }


    public void handleLoginResponse(Network.LoginResponse loginResponse) {
        if (game.getLogin() != null) {
            if (loginResponse.success) {
                game.getLogin().getTextField().setFocus(false);
                game.setGameState(GameState.LOADING);
                Network.UpdateGameState updateGameState = new Network.UpdateGameState();
                updateGameState.playerName = game.getPlayerName();
                updateGameState.gameState = GameState.LOADING;
                game.getClient().sendTCP(updateGameState);
                game.getLoading().request();
            } else {
                game.getLogin().setErrorMessage(loginResponse.message);
            }
        }
    }

}
