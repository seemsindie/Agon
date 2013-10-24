package net.wachocki.agon.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.util.InputStreamSender;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.util.FileUtils;
import net.wachocki.agon.server.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

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
        for (Player player : gameServer.getPlayers().values()) {
            if (player.getConnection() == connection) {
                gameServer.getPlayers().remove(player.getName());
            }
        }
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Network.LoginRequest) {
            handleLoginRequest(connection, (Network.LoginRequest) object);
        }
        if (object instanceof Network.UpdateDestination) {
            handleUpdateDestination(connection, (Network.UpdateDestination) object);
        }
        if (object instanceof Network.MapRequest) {
            try {
                handleMapRequest(connection, (Network.MapRequest) object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleMapRequest(final Connection connection, Network.MapRequest mapRequest) throws IOException {
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

    public void handleUpdateDestination(Connection connection, Network.UpdateDestination updateDestination) {
        Player player = gameServer.getPlayers().get(updateDestination.playerName);
        if (player.getConnection() == connection) {
            player.setDestination(updateDestination.destination);
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
        if (!error) {
            Player player = new Player(loginRequest.name, connection);
            gameServer.getPlayers().put(player.getName(), player);
            loginResponse.success = true;
            loginResponse.message = "";
        }
        connection.sendUDP(loginResponse);
    }

}
