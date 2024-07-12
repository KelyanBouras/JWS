package fr.epita.assistants.jws.domain.service;

import fr.epita.assistants.jws.converter.GameConverter;
import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.data.repository.GameRepository;
import fr.epita.assistants.jws.data.repository.PlayerRepository;
import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.domain.entity.GameListEntity;
import fr.epita.assistants.jws.domain.entity.PlayerEntity;
import fr.epita.assistants.jws.errors.GameAlreadyStartedException;
import fr.epita.assistants.jws.errors.PlayerAlreadyBombedException;
import fr.epita.assistants.jws.errors.PlayerAlreadyMovedException;
import fr.epita.assistants.jws.errors.TooMuchPlayersException;
import fr.epita.assistants.jws.presentation.rest.response.GameDetailResponse;
import fr.epita.assistants.jws.presentation.rest.response.GameListResponse;
import fr.epita.assistants.jws.presentation.rest.response.PlayerResponse;
import fr.epita.assistants.jws.utils.MapUtils;
import fr.epita.assistants.jws.utils.Point;
import fr.epita.assistants.jws.utils.State;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class GameService {
    @Inject
    GameRepository gameRepository;

    @Inject
    PlayerRepository playerRepository;


    @Transactional
    long addGame() throws IOException {
        String time = LocalDateTime.now().toString();
        GameModel gameModel = new GameModel();
        gameModel.state = State.STARTING;
        gameModel.Players = new ArrayList<>();
        gameModel.time = time;
        String map = System.getenv("JWS_MAP_PATH");
        gameModel.map = Files.readAllLines(Path.of(map));
        gameRepository.persist(gameModel);
        return gameRepository.find("time",time).stream().limit(1).toList().get(0).id;
    }

    @Transactional
    void addPlayer(String name, GameModel gm, int posx, int posy)
    {
        PlayerModel playerModel = new PlayerModel();
        playerModel.game = gm;
        playerModel.lives = 3;
        playerModel.name = name;
        playerModel.posx = posx;
        playerModel.posy = posy;
        playerModel.lastmovement = LocalDateTime.now().minus(1, ChronoUnit.DAYS).toString();
        playerModel.lastbomb = LocalDateTime.now().minus(1, ChronoUnit.DAYS).toString();
        playerRepository.persist(playerModel);
        gm.Players.add(playerRepository.find("name",name).stream().limit(1).toList().get(0));
        GameListEntity.getById(gm.id).addPlayer(GameConverter.PlayerToPlayer(playerModel));
    }

    @Transactional
    public long creategame(String name) throws IOException {
        long gid = addGame();
        GameModel gm =  gameRepository.findById(gid);
        GameListEntity.gameEntities.add(GameConverter.GameToGame(gm));
        addPlayer(name,gm,1,1);
        return gm.id;
    }

    @Transactional
    public GameModel Join(String name,long id) throws TooMuchPlayersException, GameAlreadyStartedException {
        GameModel gm = gameRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
        if (gm.Players.size() >= 4)
        {
            throw new TooMuchPlayersException();
        }
        if (gm.state != State.STARTING)
        {
            throw new GameAlreadyStartedException();
        }
        Point[] posarray = Point.initarray();
        Point pos = posarray[gm.Players.size()];
        addPlayer(name,gm,pos.getX(), pos.getY());
        return gm;
    }

    @Transactional
    public long start(long gameid) throws GameAlreadyStartedException {
        GameModel gm = gameRepository.findByIdOptional(gameid).orElseThrow(NotFoundException::new);
        if (gm.state != State.STARTING)
        {
            throw new GameAlreadyStartedException();
        }
        if (gm.Players.size() <= 1)
        {
            gm.setState(State.FINISHED);
            GameListEntity.getById(gameid).setState(State.FINISHED);
        }
        else
        {
            GameListEntity.getById(gameid).setState(State.RUNNING);
            gm.setState(State.RUNNING);
        }
        return gm.id;
    }

    public GameDetailResponse initDetailResponse(long id)
    {
        GameDetailResponse gr = new GameDetailResponse();

        GameModel gm = gameRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
        gr.id = gm.id;
        gr.state = gm.state;
        gr.startTime = gm.time;
        gr.map = gm.map;
        gr.players = new ArrayList<>();
        for (var a:gm.Players) {
            gr.players.add(initPlayerResponse(a));
        }

        return gr;
    }

    public PlayerResponse initPlayerResponse(PlayerModel pm)
    {
        PlayerResponse pr = new PlayerResponse();

        pr.id = pm.id;
        pr.lives = pm.lives;
        pr.name = pm.name;
        pr.posX = pm.posx;
        pr.posY = pm.posy;

        return pr;
    }

    public List<GameListResponse> initGameListResponse()
    {
        List<GameListResponse> gl = new ArrayList<>();
        gameRepository.listAll().forEach((e) -> gl.add(new GameListResponse().withState(e.state).withId(e.id).withPlayers(e.Players.size())));

        return gl;
    }

    @Transactional
    PlayerModel UpdatePlayer(PlayerEntity pe)
    {
        PlayerModel playerModel = playerRepository.findByIdOptional(pe.getId()).orElseGet(() ->
        {
            return new PlayerModel();
        });
        playerModel.posy = pe.getCoordinate().getY();
        playerModel.posx = pe.getCoordinate().getX();
        playerModel.lives = pe.getLives();
        playerModel.name = pe.getName();
        playerModel.lastbomb = pe.getLastbomb();
        playerModel.lastmovement = pe.getLastmov();
        return playerModel;
    }


    @Transactional
    void UpdateGame(long id)
    {
        GameEntity ge = GameListEntity.getById(id);
        GameModel gameModel = gameRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
        gameModel.state = ge.getState();
        gameModel.map = MapUtils.MapToMap(ge.getMap());
        for (var p:ge.getPlayerEntities()) {
            var pm = UpdatePlayer(p);
            pm.game = gameModel;
        }
    }

    @Transactional
    public GameDetailResponse Bombastic(long gid,long pid,Point point) throws PlayerAlreadyBombedException {
        GameEntity gameEntity = GameListEntity.getById(gid);
        if (gameEntity == null)
        {
            throw new NotFoundException();
        }
        PlayerEntity pe = gameEntity.getById(pid);
        if (pe == null)
        {
            throw new NotFoundException();
        }
        pe.Bombastic(point.getX(),point.getY());

        UpdateGame(gameEntity.getId());
        return initDetailResponse(gid);
    }

    @Transactional
    public GameDetailResponse Move(long gid,long pid,Point point) throws PlayerAlreadyMovedException {
        GameEntity gameEntity = GameListEntity.getById(gid);
        System.out.println(gameEntity);
        if (gameEntity == null)
        {
            throw new NotFoundException();
        }
        PlayerEntity pe = gameEntity.getById(pid);
        System.out.println(pe);
        if (pe == null)
        {
            throw new NotFoundException();
        }
        pe.Move(point.getX(), point.getY());
        UpdateGame(gameEntity.getId());
        return initDetailResponse(gid);
    }
}
