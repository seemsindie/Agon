package net.wachocki.agon.client;

import com.esotericsoftware.kryonet.Client;
import net.wachocki.agon.client.camera.Camera;
import net.wachocki.agon.client.entity.Player;
import net.wachocki.agon.client.input.KeyboardInput;
import net.wachocki.agon.client.input.MouseInput;
import net.wachocki.agon.client.ui.Cursor;
import net.wachocki.agon.client.ui.Loading;
import net.wachocki.agon.client.ui.Login;
import net.wachocki.agon.client.ui.Minimap;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:37 AM
 */
public class GameClient implements Game {

    public enum GameState {
        LOGIN, LOADING, INGAME
    }

    private Login login;
    private Loading loading;
    private Camera camera;
    private byte[] mapBytes;
    private TiledMap map;
    private Client client;
    private GameState gameState = GameState.LOGIN;
    private Player player;
    private Settings settings;
    private KeyboardInput keyboard;
    private MouseInput mouse;
    private Minimap minimap;
    private NetworkListener networkListener;
    private Cursor cursor;
    private HashMap<String, Player> players;
    private Map<Player.Specialization, SpriteSheet> spritesSheets;
    private boolean gameInitialized = false;

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new GameClient());
            app.setDisplayMode(1024, 576, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public GameClient() {

    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        if (gameState == GameState.LOGIN) {
            networkListener = new NetworkListener(this);
            login = new Login(this, gameContainer);
            settings = new Settings();
            loading = new Loading(this, gameContainer);
            cursor = new Cursor(new Image("resources/cursor.png"));
        } else if(gameState == GameState.INGAME) {
            map = new TiledMap(new ByteArrayInputStream(mapBytes));
            camera = new Camera();
            keyboard = new KeyboardInput(this, gameContainer);
            mouse = new MouseInput(this, gameContainer);
            minimap = new Minimap(this, gameContainer);
            spritesSheets = new HashMap<Player.Specialization, SpriteSheet>();
            spritesSheets.put(Player.Specialization.ARCHER, new SpriteSheet("resources/archer_sprites.png", 34, 46, 3));
            players = new HashMap<String, Player>();
            gameInitialized = true;
        }
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        if (gameState == GameState.INGAME) {
            if(!gameInitialized) {
                init(gameContainer);
            }
            keyboard.poll();
            mouse.poll();
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        if (gameState == GameState.LOGIN) {
            login.render();
        } else if (gameState == GameState.LOADING) {
            loading.render();
        } else if (gameState == GameState.INGAME && gameInitialized) {
            map.render(-camera.getX(), -camera.getY());
            player.render(this);
            minimap.render();
        }
        //cursor.render(this, gameContainer);
    }

    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Agon";
    }

    public NetworkListener getNetworkListener() {
        return networkListener;
    }

    public void setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    public Login getLogin() {
        return login;
    }

    public Loading getLoading() {
        return loading;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Map<Player.Specialization, SpriteSheet> getSpritesSheets() {
        return spritesSheets;
    }

    public byte[] getMapBytes() {
        return mapBytes;
    }

    public void setMapBytes(byte[] mapBytes) {
        this.mapBytes = mapBytes;
    }
}
