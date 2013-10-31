package net.wachocki.agon.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.util.InputStreamSender;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.types.EntityType;
import net.wachocki.agon.common.types.Specialization;
import net.wachocki.agon.common.util.FileUtils;
import net.wachocki.agon.server.entity.Player;
import net.wachocki.agon.server.items.GroundItem;
import net.wachocki.agon.server.items.Item;
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
        } else if (object instanceof Network.MapRequest) {
            try {
                handleMapRequest(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (object instanceof Network.UpdateGameState) {
            Player player = getPlayer(connection, ((Network.UpdateGameState) object).playerName);
            if (player != null) {
                handleUpdateGameState((Network.UpdateGameState) object, player);
            }
        } else if (object instanceof Network.UpdatePosition) {
            Player player = getPlayer(connection, ((Network.UpdatePosition) object).playerName);
            if (player != null) {
                handleUpdatePosition((Network.UpdatePosition) object, player);
            }
        } else if (object instanceof Network.SendMessage) {
            if (getPlayer(connection, ((Network.SendMessage) object).playerName) != null) {
                handleSendMessage((Network.SendMessage) object);
            }
        } else if (object instanceof Network.DropItem) {
            Player player = getPlayer(connection, ((Network.DropItem) object).playerName);
            if (player != null) {
                handleDropItem((Network.DropItem) object, player);
            }
        } else if (object instanceof Network.RemoveGroundItem) {
            Player player = getPlayer(connection, ((Network.RemoveGroundItem) object).playerName);
            if (player != null) {
                handleRemoveGroundItem((Network.RemoveGroundItem) object, player);
            }
        } else if (object instanceof FrameworkMessage.Ping) {
            // ignore
        } else {
            System.out.println("Unknown object: " + object.toString());
        }
    }

    public Player getPlayer(Connection connection, String playerName) {
        if (gameServer.getPlayers().containsKey(playerName)) {
            Player player = gameServer.getPlayers().get(playerName);
            if (player.getConnection() == connection) {
                return player;
            }
        }
        return null;
    }

    public void handleDropItem(Network.DropItem dropItem, Player player) {
        if (player.getInventory().getInventorySize() > dropItem.inventoryIndex && player.getInventory().getItem(dropItem.inventoryIndex) != null) {
            // Drop item
            Item item = player.getInventory().getItem(dropItem.inventoryIndex);
            player.getInventory().setItem(dropItem.inventoryIndex, null);
            int groundId = gameServer.getNextGroundId();
            gameServer.getGroundItems().put(groundId, new GroundItem(item, groundId, player.getPosition()));
            // Update ground item
            player.getInventory().sendUpdate(player, new int[] { dropItem.inventoryIndex });
        }
    }

    public void handleRemoveGroundItem(Network.RemoveGroundItem removeGroundItem, Player player) {
        if (gameServer.getGroundItems().containsKey(removeGroundItem.groundId)) {
            if (!player.getInventory().isFull()) {
                // Remove ground item
                GroundItem groundItem = gameServer.getGroundItems().get(removeGroundItem.groundId);
                if (player.getPosition().distance(groundItem.getPosition()) < 0.5) {
                    groundItem.sendRemove(gameServer.getPlayers().values().toArray(new Player[gameServer.getPlayers().values().size()]), player);
                    gameServer.getGroundItems().remove(groundItem.getGroundId());
                    int slot = player.getInventory().addItem(groundItem.getItem());
                    // Update inventory
                    player.getInventory().sendUpdate(player, new int[] { slot });
                }
            }
        }
    }

    public void handleSendMessage(Network.SendMessage sendMessage) {
        for (Player update : gameServer.getPlayers().values()) {
            Network.SendMessage updateMessage = new Network.SendMessage();
            updateMessage.playerName = sendMessage.playerName;
            updateMessage.message = sendMessage.message.length() > 150 ? sendMessage.message.substring(0, 150) : sendMessage.message;
            update.getConnection().sendTCP(updateMessage);
        }
    }

    public void handleUpdatePosition(Network.UpdatePosition updatePosition, Player player) {
        player.setPosition(updatePosition.position);
        player.setDestination(updatePosition.destination);
        if(updatePosition.entityTargetType != EntityType.NONE) {
            if(updatePosition.entityTargetType == EntityType.GROUND_ITEM) {
                if(gameServer.getGroundItems().containsKey(updatePosition.entityTargetId)) {
                    player.setTargetEntity(gameServer.getGroundItems().get(updatePosition.entityTargetId));
                }
            }
            player.setTargetEntityAction(updatePosition.entityTargetAction);
        }
    }

    public void handleUpdateGameState(Network.UpdateGameState updateGameState, Player player) {
        player.setGameState(updateGameState.gameState);
    }

    public void handleMapRequest(final Connection connection) throws IOException {
        final byte[] fileBytes = FileUtils.getFileByteArray(new File("resources/map.tmx"));
        ByteArrayInputStream input = new ByteArrayInputStream(fileBytes);
        connection.addListener(new InputStreamSender(input, 512) {
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

    public void handleLoginRequest(Connection connection, Network.LoginRequest loginRequest) {
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
