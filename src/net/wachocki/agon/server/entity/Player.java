package net.wachocki.agon.server.entity;

import com.esotericsoftware.kryonet.Connection;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class Player extends Entity {

    private Connection connection;

    public Player(String name, Connection connection) {
        super(name);
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
