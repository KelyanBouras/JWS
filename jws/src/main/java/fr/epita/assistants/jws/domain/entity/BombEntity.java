package fr.epita.assistants.jws.domain.entity;

import fr.epita.assistants.jws.utils.Point;
import fr.epita.assistants.jws.utils.TileType;
import fr.epita.assistants.jws.utils.TimeUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class BombEntity {
    GameEntity game;
    Point pos;

    public void explodePoint(Point pos1)
    {
        if (game.isInRange(pos1))
        {
            if (game.map[pos1.getX()][pos1.getY()] != TileType.METAL)
            {
                game.map[pos1.getX()][pos1.getY()] = TileType.GROUND;
                for (var p:game.playerEntities) {
                    if (p.coordinate.equals(pos1))
                    {
                        p.damage();
                    }
                }
            }
        }
    }
    public void Kaboom()
    {
        var pos1 = pos.plus(1,0);
        var pos2 = pos.plus(0,1);
        var pos3 = pos.plus(-1,0);
        var pos4 = pos.plus(0,-1);
        CompletableFuture completableFuture = new CompletableFuture<>();
            completableFuture.thenRunAsync(() -> explodePoint(pos1))
                    .thenRunAsync(() -> explodePoint(pos2))
                    .thenRunAsync(() -> explodePoint(pos3))
                    .thenRunAsync(() -> explodePoint(pos4))
                    .thenRunAsync(() -> explodePoint(pos));
    }

    public BombEntity(GameEntity game,int x,int y) {
        this.game = game;
        this.pos = new Point(x,y);
        long delay = Long.parseLong(System.getenv("JWS_DELAY_BOMB"));
        CompletableFuture completableFuture= (new CompletableFuture<>())
                .thenRunAsync(this::Kaboom
                        ,CompletableFuture.delayedExecutor(TimeUtils.getNumberOfMilli(delay), TimeUnit.MILLISECONDS));
    }
}
