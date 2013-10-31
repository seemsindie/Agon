package net.wachocki.agon.server.entity;

import com.esotericsoftware.kryonet.Connection;
import net.wachocki.agon.common.types.GameState;
import net.wachocki.agon.common.types.Specialization;
import net.wachocki.agon.server.items.Inventory;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class Player extends LivingEntity {

    private Connection connection;
    private GameState gameState;
    private Specialization specialization;
    private Inventory inventory;

    public Player(String name, Connection connection) {
        super(name);
        this.connection = connection;
        this.inventory = new Inventory();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
