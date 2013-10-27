package net.wachocki.agon.server;

import com.esotericsoftware.kryonet.Server;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.server.entity.Player;

import java.io.IOException;
import java.util.HashMap;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:37 AM
 */
public class GameServer {

    private Server server;
    private HashMap<String, Player> players;


    public static void main(String[] args) {
        new GameServer().start();
    }

    public GameServer() {
        //new MapGenerator().writeTMX("resources/random.tmx", 300, 300);

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() throws InterruptedException {
        while (true) {
            for (Player player : players.values()) {
                for (Player update : players.values()) {
                    if (player != update) {
                        Network.UpdatePosition updatePosition = new Network.UpdatePosition();
                        updatePosition.playerName = update.getName();
                        updatePosition.position = update.getPosition();
                        updatePosition.destination = update.getDestination();
                        player.getConnection().sendUDP(updatePosition);
                    }
                }
            }
            Thread.sleep(50);
        }
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}