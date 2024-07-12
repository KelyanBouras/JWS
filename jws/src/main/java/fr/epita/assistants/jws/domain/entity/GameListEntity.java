package fr.epita.assistants.jws.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class GameListEntity {
    public static List<GameEntity> gameEntities = new ArrayList<>();

    public static GameEntity getById(long id){
        for (var a:gameEntities)
        {
            if (a.id == id){ return a;}
        }
        return null;
    }
}
