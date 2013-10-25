package net.wachocki.agon.client;

import com.esotericsoftware.kryonet.Client;
import net.wachocki.agon.client.camera.Camera;
import net.wachocki.agon.client.entity.Player;
import net.wachocki.agon.client.input.KeyboardInput;
import net.wachocki.agon.client.input.MouseInput;
import net.wachocki.agon.client.spells.Spell;
import net.wachocki.agon.client.ui.*;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.types.GameState;
import net.wachocki.agon.common.types.Specialization;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
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

    private static AppGameContainer app;

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
    private HashMap<String, Player> players;
    private Map<Specialization, SpriteSheet> spritesSheets;
    private ActionBar actionBar;
    private boolean gameInitialized = false;
    private String playerName;
    private Chat chat;

    public static void main(String[] args) {
        try {
            app = new AppGameContainer(new GameClient());
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
        gameContainer.setUpdateOnlyWhenVisible(false);
        gameContainer.setAlwaysRender(true);
        if (gameState == GameState.LOGIN) {
            networkListener = new NetworkListener(this);
            login = new Login(this, gameContainer);
            settings = new Settings();
            settings.check(gameContainer, app);
            loading = new Loading(this, gameContainer);
            gameContainer.setMouseCursor("resources/cursor.png", 0, 0);
            players = new HashMap<String, Player>();
        } else if (gameState == GameState.INGAME) {
            chat = new Chat(this, gameContainer);
            actionBar = new ActionBar(new Image("resources/actionbar.png"), new Spell[0]);
            map = new TiledMap(new ByteArrayInputStream(mapBytes));
            camera = new Camera();
            camera.centerOn(player, gameContainer);
            keyboard = new KeyboardInput(this, gameContainer);
            keyboard.bind();
            mouse = new MouseInput(this, gameContainer);
            mouse.bind();
            minimap = new Minimap(this, gameContainer);
            spritesSheets = new HashMap<Specialization, SpriteSheet>();
            spritesSheets.put(Specialization.ARCHER, new SpriteSheet("resources/archer_sprites.png", 34, 46, 3));
            players.put(player.getName(), player);
            Network.UpdateGameState updateGameState = new Network.UpdateGameState();
            updateGameState.playerName = player.getName();
            updateGameState.gameState = GameState.INGAME;
            client.sendTCP(updateGameState);
            settings.setChatFont(new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12), true));
            gameInitialized = true;
        }
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        if (gameState == GameState.INGAME) {
            if (!gameInitialized) {
                init(gameContainer);
            }
            keyboard.poll();
            mouse.poll();
            for (Player player : players.values()) {
                if (!player.getWalkingQueue().isEmpty()) {
                    if(player.getWalkingQueue().getFirst().distance(player.getPosition()) < 1) {
                        Network.UpdatePosition updatePosition = new Network.UpdatePosition();
                        updatePosition.playerName = player.getName();
                        updatePosition.position = player.getPosition();
                        client.sendUDP(updatePosition);
                        player.getWalkingQueue().pop();
                        continue;
                    }
                    Vector2f direction = new Vector2f(player.getWalkingQueue().getFirst().getX() - player.getPosition().getX(),
                            player.getWalkingQueue().getFirst().getY() - player.getPosition().getY());
                    float speed = 0.15f * delta;
                    double x = player.getPosition().getX() + (speed * Math.cos(Math.toRadians(direction.getTheta())));
                    double y = player.getPosition().getY() + (speed * Math.sin(Math.toRadians(direction.getTheta())));
                    player.setPosition(new Vector2f((float) x, (float) y));
                }
            }
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
            for (Player player : players.values()) {
                player.render(this);
            }
            actionBar.render(gameContainer);
            minimap.render();
            chat.render();
            graphics.setColor(Color.white);
            graphics.drawString("Mouse: (" + Mouse.getX() + ", " + Mouse.getY() + ")", 10, 30);
            graphics.drawString("Camera: (" + camera.getX() + ", " + camera.getY() + ")", 10, 50);
            graphics.drawString("Player: (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")", 10, 70);
        }
    }

    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Agon";
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public Map<Specialization, SpriteSheet> getSpritesSheets() {
        return spritesSheets;
    }

    public byte[] getMapBytes() {
        return mapBytes;
    }

    public void setMapBytes(byte[] mapBytes) {
        this.mapBytes = mapBytes;
    }
}
