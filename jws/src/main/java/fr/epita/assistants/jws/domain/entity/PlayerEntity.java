package fr.epita.assistants.jws.domain.entity;

import fr.epita.assistants.jws.errors.PlayerAlreadyBombedException;
import fr.epita.assistants.jws.errors.PlayerAlreadyMovedException;
import fr.epita.assistants.jws.utils.Point;
import fr.epita.assistants.jws.utils.State;
import fr.epita.assistants.jws.utils.TileType;
import fr.epita.assistants.jws.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.BadRequestException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static fr.epita.assistants.jws.utils.TimeUtils.getNumberOfMilli;

public class PlayerEntity {
    @Getter
    long id;

    @Setter
    @Getter
    String lastmov;
    @Setter
    @Getter
    String lastbomb;

    public boolean canMove()
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime a = LocalDateTime.parse(lastmov);
        LocalDateTime n = a.plus(getNumberOfMilli(Long.parseLong(System.getenv("JWS_DELAY_MOVEMENT"))),ChronoUnit.MILLIS);
        return !n.isAfter(now);
    }

    public boolean canBomb()
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime a = LocalDateTime.parse(lastbomb);
        LocalDateTime n = a.plus(getNumberOfMilli(Long.parseLong(System.getenv("JWS_DELAY_BOMB"))),ChronoUnit.MILLIS);
        return !n.isAfter(now);
    }

    @Setter @Getter
    GameEntity game;

    @Setter
    @Getter
    String name;
    @Setter
    @Getter
    Point coordinate;

    @Setter
    @Getter
    int lives;

    public void damage()
    {
        this.lives -= 1;
        if (lives == 0)
        {
            Die();
        }
    }

    public void Move(int x,int y) throws PlayerAlreadyMovedException {
        if (!canMove())
        {throw new PlayerAlreadyMovedException();}
        if (game.state != State.RUNNING)
        {
            throw new BadRequestException();
        }
        Point pos = new Point(x,y);
        if (!game.isInRange(pos))
        {
            throw new BadRequestException();
        }
        if (!(pos.equals(coordinate.plus(1,0)) ||
                pos.equals(coordinate.plus(0,1)) ||
                pos.equals(coordinate.plus(-1,0)) ||
                pos.equals(coordinate.plus(0,-1))))
        {
            throw new BadRequestException();
        }
        if (game.map[pos.getX()][pos.getY()] == TileType.GROUND)
        {
            this.coordinate = pos;
            this.lastmov = LocalDateTime.now().toString();
        }
        else
        {
            throw new BadRequestException();
        }
    }

    public void Bombastic(int x,int y) throws PlayerAlreadyBombedException {
        if (!canBomb()){
            throw new PlayerAlreadyBombedException();
        }
        if (game.state != State.RUNNING)
        {
            throw new BadRequestException();
        }

        Point pos = new Point(x,y);
        if (!coordinate.equals(pos))
        {
            throw new BadRequestException();
        }
        game.map[x][y] = TileType.BOMB;
        BombEntity n = new BombEntity(game,x,y);
        this.lastbomb = LocalDateTime.now().toString();
    }

    public void Die()
    {
        game.Removeplayer(id);
    }

    public PlayerEntity(long id, String name, Point coordinate, int lives) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
        this.lives = lives;
        this.lastbomb = LocalDateTime.now().minus(1, ChronoUnit.DAYS).toString();
        this.lastmov = LocalDateTime.now().minus(1, ChronoUnit.DAYS).toString();
    }
}
