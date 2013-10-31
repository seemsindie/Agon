package net.wachocki.agon.client;

import com.esotericsoftware.kryonet.Client;
import net.wachocki.agon.client.camera.Camera;
import net.wachocki.agon.client.entity.Entity;
import net.wachocki.agon.client.entity.GroundItem;
import net.wachocki.agon.client.entity.Player;
import net.wachocki.agon.client.input.KeyboardInput;
import net.wachocki.agon.client.input.MouseInput;
import net.wachocki.agon.client.items.Inventory;
import net.wachocki.agon.client.items.ItemDefinition;
import net.wachocki.agon.client.spells.Spell;
import net.wachocki.agon.client.ui.*;
import net.wachocki.agon.client.world.CollisionMap;
import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.common.types.GameState;
import net.wachocki.agon.common.types.Specialization;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.LinkedList;
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
    private TiledMap tileMap;
    private Client client;
    private GameState gameState = GameState.LOGIN;
    private Player player;
    private Settings settings;
    private KeyboardInput keyboard;
    private MouseInput mouse;
    private Minimap minimap;
    private NetworkListener networkListener;
    private HashMap<String, Player> players;
    private HashMap<Integer, GroundItem> groundItems;
    private Map<Specialization, SpriteSheet> spritesSheets;
    private ActionBar actionBar;
    private boolean gameInitialized;
    private String playerName;
    private Chat chat;
    private GameMap gameMap;
    private CollisionMap collisionMap;
    private boolean displayNames;
    private Inventory inventory;
    private boolean debugMode = true;
    private ContextMenu contextMenu;
    private LinkedList<Vector2f> walkingQueue = new LinkedList<Vector2f>();
    private Entity targetEntity;
    private int targetEntityActionIndex;

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
        gameContainer.setShowFPS(false);
        if (gameState == GameState.LOGIN) {
            networkListener = new NetworkListener(this);
            login = new Login(this, gameContainer);
            settings = new Settings();
            settings.check(gameContainer, app);
            loading = new Loading(this, gameContainer);
            gameContainer.setMouseCursor("resources/cursor.png", 0, 0);
            players = new HashMap<String, Player>();
        } else if (gameState == GameState.INGAME) {
            ItemDefinition.load("resources/items.xml");
            spritesSheets = new HashMap<Specialization, SpriteSheet>();
            groundItems = new HashMap<Integer, GroundItem>();
            spritesSheets.put(Specialization.ARCHER, new SpriteSheet("resources/archer_sprites.png", 34, 46, 3));
            actionBar = new ActionBar(new Image("resources/actionbar.png"), new Spell[0]);
            tileMap = new TiledMap(new ByteArrayInputStream(mapBytes));
            collisionMap = new CollisionMap(this);
            chat = new Chat(this, gameContainer);
            gameMap = new GameMap(this, gameContainer);
            minimap = new Minimap(this, gameContainer);
            camera = new Camera(this, gameContainer);
            inventory = new Inventory(new Image("resources/inventory.png"), this, gameContainer);
            keyboard = new KeyboardInput(this, gameContainer);
            mouse = new MouseInput(this, gameContainer);
            keyboard.bind();
            mouse.bind();
            camera.centerOn(player);
            players.put(player.getName(), player);
            Network.UpdateGameState updateGameState = new Network.UpdateGameState();
            updateGameState.playerName = player.getName();
            updateGameState.gameState = GameState.INGAME;
            client.sendTCP(updateGameState);
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
                player.walk(this, delta);
                if (player.getImage() == null) {
                    player.setImage(getSpritesSheets().get(player.getSpecialization()).getSprite(0, 0));
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
            if (gameMap.isVisible()) {
                gameMap.render();
                return;
            }
            tileMap.render(-camera.getX(), -camera.getY());
            for (GroundItem groundItem : groundItems.values()) {
                groundItem.render(this, gameContainer);
            }
            for (Player player : players.values()) {
                player.render(this, gameContainer);
            }
            actionBar.render(gameContainer);
            minimap.render();
            chat.render();
            if (inventory.isVisible()) {
                inventory.render(this, gameContainer);
            }
            if (contextMenu != null) {
                contextMenu.render();
            }
            if (debugMode) {
                graphics.setColor(Color.white);
                if (!getWalkingQueue().isEmpty()) {
                    for (Vector2f w : getWalkingQueue()) {
                        Vector2f s = camera.worldToScreen(new Vector2f(w.x, w.y));
                        graphics.fillOval(s.getX(), s.getY(), 4, 4);
                    }
                }
                graphics.drawString("FPS: " + gameContainer.getFPS(), 10, 10);
                graphics.drawString("Mouse: (" + Mouse.getX() + ", " + Mouse.getY() + ")", 10, 30);
                graphics.drawString("Camera: (" + camera.getX() + ", " + camera.getY() + ")", 10, 50);
                graphics.drawString("Player: (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")", 10, 70);
                graphics.drawString("Tile: " + tileMap.getTileId((int) player.getPosition().getX(), (int) player.getPosition().getY(), 1), 10, 90);
            }
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

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public int getTargetEntityActionIndex() {
        return targetEntityActionIndex;
    }

    public void setTargetEntityActionIndex(int targetEntityActionIndex) {
        this.targetEntityActionIndex = targetEntityActionIndex;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public GameMap getGameMap() {
        return gameMap;
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

    public CollisionMap getCollisionMap() {
        return collisionMap;
    }

    public void toggleDebugMode() {
        debugMode = !debugMode;
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

    public HashMap<Integer, GroundItem> getGroundItems() {
        return groundItems;
    }

    public Inventory getInventory() {
        return inventory;
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

    public boolean isDisplayNames() {
        return displayNames;
    }

    public void setDisplayNames(boolean displayNames) {
        this.displayNames = displayNames;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public TiledMap getTileMap() {
        return tileMap;
    }

    public void setTileMap(TiledMap tileMap) {
        this.tileMap = tileMap;
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

    public LinkedList<Vector2f> getWalkingQueue() {
        return walkingQueue;
    }

    public void setWalkingQueue(LinkedList<Vector2f> walkingQueue) {
        this.walkingQueue = walkingQueue;
    }
}
