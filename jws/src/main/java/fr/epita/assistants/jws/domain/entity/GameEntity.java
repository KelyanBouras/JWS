package fr.epita.assistants.jws.domain.entity;

import fr.epita.assistants.jws.utils.MapUtils;
import fr.epita.assistants.jws.utils.Point;
import fr.epita.assistants.jws.utils.State;
import fr.epita.assistants.jws.utils.TileType;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameEntity {

    @Getter
    long id;

    @Getter
    List<PlayerEntity> playerEntities;

    @Getter @Setter
    State state;

    @Setter
    @Getter
    TileType[][] map;

    public PlayerEntity getById(long id){
        for (var a:playerEntities)
        {
            if (a.id == id){ return a;}
        }
        return null;
    }

    public void start()
    {
        if (playerEntities.size() <= 1)
        {
            this.state = State.FINISHED;
        }else
        {
            this.state = State.RUNNING;
        }
    }

    TileType switchtype(char letter)
    {
        TileType tileType = TileType.WOOD;
        switch (letter)
        {
            case 'M' -> tileType = TileType.METAL;
            case 'B' -> tileType = TileType.BOMB;
            case 'G' -> tileType = TileType.GROUND;
        }
        return tileType;
    }

    public void Removeplayer(long id)
    {
        for (var a:playerEntities) {
            if (a.id == id)
            {
                playerEntities.remove(a);
            }
        }
        if (playerEntities.size() <= 1)
        {
            this.state = State.FINISHED;
        }
    }

    List<TileType[]> tmp;

    public void addPlayer(PlayerEntity pe)
    {
        pe.setGame(this);
        this.playerEntities.add(pe);
    }

    public boolean isInRange(Point p)
    {
        return (p.getX() < map.length && p.getX() >= 0) && (p.getY() < map[0].length && p.getY() >= 0);
    }

    public void Parseline(String line)
    {
        List<TileType> tiles = new ArrayList<>();
        int i1 = 0;
        int i2 = 1;
        while (i2 < line.length())
        {
            TileType toadd = switchtype(line.charAt(i2));
            for (var i = 0; i < (line.charAt(i1) - '0');i++)
            {
                tiles.add(toadd);
            }
            i1 += 2;
            i2 += 2;
        }
        tmp.add(tiles.toArray(new TileType[0]));
    }

    public GameEntity(long id, State state, String map) {
        this.id = id;
        this.state = state;
        this.playerEntities = new ArrayList<>();
        try {
            List<String> m = Files.readAllLines(Path.of(map));
            tmp = new ArrayList<>();
            for (var a:m) {
                Parseline(a);
            }
            this.map = tmp.toArray(new TileType[0][]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (var ds:this.map) {
            System.out.println(ds.length);
        }
    }
}
