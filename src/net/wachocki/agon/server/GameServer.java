package net.wachocki.agon.server;

import com.esotericsoftware.kryonet.Server;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.types.GameState;
import net.wachocki.agon.server.entity.Player;
import net.wachocki.agon.server.items.GroundItem;
import net.wachocki.agon.server.items.Item;
import org.newdawn.slick.geom.Vector2f;

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
    private HashMap<Integer, GroundItem> groundItems;
    private int currentGroundId;
    private NetworkListener networkListener;

    public static void main(String[] args) {
        new GameServer().start();
    }

    public GameServer() {
        //new MapGenerator().writeTMX("resources/random.tmx", 300, 300);

        server = new Server();
        networkListener = new NetworkListener(this);
        players = new HashMap<String, Player>();
        groundItems = new HashMap<Integer, GroundItem>();
        groundItems.put(0, new GroundItem(new Item(0, 1), 0, new Vector2f(140, 145)));
        groundItems.put(1, new GroundItem(new Item(0, 1), 1, new Vector2f(142, 145)));
        groundItems.put(2, new GroundItem(new Item(0, 1), 2, new Vector2f(144, 145)));
        groundItems.put(3, new GroundItem(new Item(0, 1), 3, new Vector2f(146, 145)));
        groundItems.put(4, new GroundItem(new Item(0, 1), 4, new Vector2f(148, 145)));
        groundItems.put(5, new GroundItem(new Item(0, 1), 5, new Vector2f(160, 145)));


    }

    public void start() {
        try {
            Network.register(server);
            server.bind(Network.PORT, Network.PORT);
            server.addListener(networkListener);
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
                if (player.getGameState() == GameState.INGAME) {
                    // Handle target entities
                    if (player.getTargetEntity() != null) {
                        if (player.getTargetEntity() instanceof GroundItem) {
                            GroundItem groundItem = (GroundItem) player.getTargetEntity();
                            if (player.getPosition().distance(groundItem.getPosition()) < 0.5) {
                                if (player.getTargetEntityAction() == 0) {
                                    Network.RemoveGroundItem removeGroundItem = new Network.RemoveGroundItem();
                                    removeGroundItem.groundId = groundItem.getGroundId();
                                    removeGroundItem.playerName = player.getName();
                                    networkListener.handleRemoveGroundItem(removeGroundItem, player);
                                }
                            }
                        }
                    }

                    // Update ground items
                    for (GroundItem groundItem : groundItems.values()) {
                        if (player.getPosition().distance(groundItem.getPosition()) < 40) {
                            Network.UpdateGroundItem updateGroundItem = new Network.UpdateGroundItem();
                            updateGroundItem.groundId = groundItem.getGroundId();
                            updateGroundItem.itemId = groundItem.getItem().getItemId();
                            updateGroundItem.itemAmount = groundItem.getItem().getAmount();
                            updateGroundItem.position = groundItem.getPosition();
                            player.getConnection().sendUDP(updateGroundItem);
                        }
                    }
                }

                // Update positions
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

    public HashMap<Integer, GroundItem> getGroundItems() {
        return groundItems;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getNextGroundId() {
        return currentGroundId++;
    }

}