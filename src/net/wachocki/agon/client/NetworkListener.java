package net.wachocki.agon.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.wachocki.agon.client.entity.Player;
import net.wachocki.agon.common.network.Network;

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
    public void disconnected(Connection connection) {

    }

    public void received(Connection connection, Object object) {
        if (object instanceof Network.LoginResponse) {
            handleLoginResponse((Network.LoginResponse) object);
        }
        if (object instanceof Network.MapResponse) {
            handleMapResponse((Network.MapResponse) object);
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
                    System.out.println();
                    if (mapBytes.length == mapSize) {
                        game.setMapBytes(mapBytes);
                        game.setGameState(GameClient.GameState.INGAME);
                        connection.removeListener(this);
                        System.out.println("done?");
                    }
                }
            }
        });
    }


    public void handleLoginResponse(Network.LoginResponse loginResponse) {
        if (game.getLogin() != null) {
            if (loginResponse.success) {
                game.setPlayer(new Player("Marty"));
                game.setGameState(GameClient.GameState.LOADING);
                game.getLoading().request();
            } else {
                game.getLogin().setErrorMessage(loginResponse.message);
            }
        }
    }

}
