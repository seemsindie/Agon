package net.wachocki.agon.common.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import net.wachocki.agon.common.types.GameState;
import net.wachocki.agon.common.types.Specialization;
import org.newdawn.slick.geom.Vector2f;

import java.util.LinkedList;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:46 AM
 */
public class Network {

    public static String HOST = "127.0.0.1";
    public static int PORT = 2882;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(LoginRequest.class);
        kryo.register(LoginResponse.class);
        kryo.register(UpdatePosition.class);
        kryo.register(UpdateGameState.class);
        kryo.register(MapRequest.class);
        kryo.register(MapResponse.class);
        kryo.register(MapChunk.class);
        kryo.register(AddPlayer.class);
        kryo.register(RemovePlayer.class);
        kryo.register(SendMessage.class);

        kryo.register(Specialization.class);
        kryo.register(GameState.class);

        kryo.register(LinkedList.class);
        kryo.register(Vector2f.class);
        kryo.register(byte[].class);
    }

    public static class LoginRequest {
        public String name;
    }

    public static class LoginResponse {
        public boolean success;
        public String message;
    }

    public static class UpdatePosition {
        public String playerName;
        public Vector2f position;
        public Vector2f destination;
    }

    public static class MapRequest {
        public String playerName;
    }

    public static class MapResponse {
        public int totalSize;
    }

    public static class MapChunk {
        public byte[] bytes;
    }

    public static class UpdateGameState {
        public String playerName;
        public GameState gameState;
    }

    public static class AddPlayer {
        public String playerName;
        public Specialization specialization;
        public Vector2f position;
    }

    public static class RemovePlayer {
        public String playerName;
    }

    public static class SendMessage {
        public String playerName;
        public String message;
    }


}
