package net.wachocki.agon.client.world;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 * User: Marty
 * Date: 10/26/13
 * Time: 5:39 PM
 */
public class CollisionMap implements TileBasedMap {

    private GameClient game;

    public CollisionMap(GameClient game) {
        this.game = game;
    }

    @Override
    public int getWidthInTiles() {
        return 300;
    }

    @Override
    public int getHeightInTiles() {
        return 300;
    }

    @Override
    public void pathFinderVisited(int x, int y) {
    }

    @Override
    public boolean blocked(PathFindingContext context, int tx, int ty) {
        return game.getTileMap().getTileId(tx, ty, 1) != 0;
    }

    @Override
    public float getCost(PathFindingContext context, int tx, int ty) {
        return 1.0f;
    }
}
