package fr.epita.assistants.jws.converter;

import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.data.repository.GameRepository;
import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.domain.entity.PlayerEntity;
import fr.epita.assistants.jws.domain.service.GameService;
import fr.epita.assistants.jws.utils.Point;

import javax.inject.Inject;

public class GameConverter {

    public static PlayerEntity PlayerToPlayer(PlayerModel pm)
    {
        PlayerEntity pe = new PlayerEntity(pm.getId(), pm.getName(), new Point(pm.posx, pm.posy), pm.getLives());
        return pe;
    }

    public static GameEntity GameToGame(GameModel gm)
    {
        GameEntity gameEntity = new GameEntity(gm.getId(),gm.getState(),System.getenv("JWS_MAP_PATH"));
        for (var a:gm.Players)
        {
            var p = PlayerToPlayer(a);
            p.setGame(gameEntity);
            gameEntity.addPlayer(p);
        }
        return gameEntity;
    }
}
