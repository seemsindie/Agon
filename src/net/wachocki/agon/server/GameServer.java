package net.wachocki.agon.server;

import com.esotericsoftware.kryonet.Server;
import net.wachocki.agon.server.entity.Player;
import net.wachocki.agon.common.network.Network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:37 AM
 */
public class GameServer {

    private Server server;
    private Map<String, Player> players;

    public static void main(String[] args) {
        new GameServer().start();
    }

    public GameServer() {
        server = new Server();
        players = new HashMap<String, Player>();
    }

    public void start() {
        try {
            Network.register(server);
            server.bind(Network.PORT, Network.PORT);
            server.addListener(new NetworkListener(this));
            server.start();
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        while (true) {

        }
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}