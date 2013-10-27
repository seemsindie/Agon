package net.wachocki.agon.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.util.InputStreamSender;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.types.Specialization;
import net.wachocki.agon.common.util.FileUtils;
import net.wachocki.agon.server.entity.Player;
import org.newdawn.slick.geom.Vector2f;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:50 AM
 */
public class NetworkListener extends Listener {

    private GameServer gameServer;

    public NetworkListener(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void disconnected(Connection connection) {
        synchronized (gameServer.getPlayers()) {
            Iterator playerIterator = gameServer.getPlayers().values().iterator();
            while (playerIterator.hasNext()) {
                Player player = (Player) playerIterator.next();
                if (player.getConnection() == connection) {
                    playerIterator.remove();
                    for (Player update : gameServer.getPlayers().values()) {
                        if (!update.getName().equals(player.getName())) {
                            Network.RemovePlayer removePlayer = new Network.RemovePlayer();
                            removePlayer.playerName = player.getName();
                            update.getConnection().sendTCP(removePlayer);
                        }
                    }
                }
            }
        }
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Network.LoginRequest) {
            handleLoginRequest(connection, (Network.LoginRequest) object);
        }
        if (object instanceof Network.MapRequest) {
            try {
                handleMapRequest(connection, (Network.MapRequest) object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (object instanceof Network.UpdateGameState) {
            handleUpdateGameState(connection, (Network.UpdateGameState) object);
        }
        if (object instanceof Network.UpdatePosition) {
            handleUpdatePosition(connection, (Network.UpdatePosition) object);
        }
        if (object instanceof Network.SendMessage) {
            handleSendMessage(connection, (Network.SendMessage) object);
        }
    }

    public void handleSendMessage(Connection connection, Network.SendMessage sendMessage) {
        if (gameServer.getPlayers().containsKey(sendMessage.playerName)) {
            Player player = gameServer.getPlayers().get(sendMessage.playerName);
            if (player.getConnection() == connection) {
                for (Player update : gameServer.getPlayers().values()) {
                    Network.SendMessage updateMessage = new Network.SendMessage();
                    updateMessage.playerName = sendMessage.playerName;
                    updateMessage.message = sendMessage.message.length() > 150 ? sendMessage.message.substring(0, 150) : sendMessage.message;
                    update.getConnection().sendTCP(updateMessage);
                }
            }
        }
    }

    public void handleUpdatePosition(Connection connection, Network.UpdatePosition updatePosition) {
        if (gameServer.getPlayers().containsKey(updatePosition.playerName)) {
            Player player = gameServer.getPlayers().get(updatePosition.playerName);
            if (player.getConnection() == connection) {
                player.setPosition(updatePosition.position);
                player.setDestination(updatePosition.destination);
            }
        }
    }

    public void handleUpdateGameState(Connection connection, Network.UpdateGameState updateGameState) {
        if (gameServer.getPlayers().containsKey(updateGameState.playerName)) {
            Player player = gameServer.getPlayers().get(updateGameState.playerName);
            if (player.getConnection() == connection) {
                player.setGameState(updateGameState.gameState);
            }
        }
    }

    public void handleMapRequest(final Connection connection, Network.MapRequest mapRequest) throws IOException {
        if (gameServer.getPlayers().containsKey(mapRequest.playerName)) {
            Player player = gameServer.getPlayers().get(mapRequest.playerName);
            if (player.getConnection() == connection) {
                final byte[] fileBytes = FileUtils.getFileByteArray(new File("resources/map.tmx"));
                ByteArrayInputStream input = new ByteArrayInputStream(fileBytes);
                connection.addListener(new InputStreamSender(input, 1438) {
                    @Override
                    protected void start() {
                        Network.MapResponse mapResponse = new Network.MapResponse();
                        mapResponse.totalSize = fileBytes.length;
                        connection.sendTCP(mapResponse);
                    }

                    @Override
                    protected Object next(byte[] bytes) {
                        Network.MapChunk mapChunk = new Network.MapChunk();
                        mapChunk.bytes = bytes;
                        return mapChunk;
                    }

                });
            }
        }
    }

    public void handleLoginRequest(Connection connection, Network.LoginRequest loginRequest) {
        System.out.println(loginRequest.name + " @ " + connection.getRemoteAddressUDP().getHostString() + " connected.");
        Network.LoginResponse loginResponse = new Network.LoginResponse();
        boolean error = false;
        if (!loginRequest.name.matches("^[A-Za-z0-9_]+$")) {
            loginResponse.success = false;
            loginResponse.message = "Invalid name.";
            error = true;
        }
        if (!error) {
            for (Player player : gameServer.getPlayers().values()) {
                if (player != null) {
                    if (player.getName().equals(loginRequest.name)) {
                        loginResponse.success = false;
                        loginResponse.message = "A player with that name already exists.";
                        error = true;
                    }
                }
            }
        }
        Player player = null;
        if (!error) {
            player = new Player(loginRequest.name, connection);
            player.setSpecialization(Specialization.ARCHER);
            player.setPosition(new Vector2f(150, 150));
            synchronized (gameServer.getPlayers()) {
                gameServer.getPlayers().put(player.getName(), player);
            }
            loginResponse.success = true;
            loginResponse.message = "";
        }
        connection.sendTCP(loginResponse);
        if (!error && player != null) {
            for (Player update : gameServer.getPlayers().values()) {
                Network.AddPlayer addPlayer = new Network.AddPlayer();
                addPlayer.playerName = player.getName();
                addPlayer.specialization = player.getSpecialization();
                addPlayer.position = player.getPosition();
                update.getConnection().sendTCP(addPlayer);
                if (!player.getName().equals(update.getName())) {
                    addPlayer = new Network.AddPlayer();
                    addPlayer.playerName = update.getName();
                    addPlayer.specialization = update.getSpecialization();
                    addPlayer.position = update.getPosition();
                    player.getConnection().sendTCP(addPlayer);
                }
            }
        }
    }

}
