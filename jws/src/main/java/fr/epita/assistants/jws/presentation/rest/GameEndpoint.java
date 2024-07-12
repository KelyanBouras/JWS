package fr.epita.assistants.jws.presentation.rest;

import fr.epita.assistants.jws.domain.service.GameService;
import fr.epita.assistants.jws.errors.GameAlreadyStartedException;
import fr.epita.assistants.jws.errors.PlayerAlreadyBombedException;
import fr.epita.assistants.jws.errors.PlayerAlreadyMovedException;
import fr.epita.assistants.jws.errors.TooMuchPlayersException;
import fr.epita.assistants.jws.presentation.rest.request.CreateGameRequest;
import fr.epita.assistants.jws.presentation.rest.request.JoinGameRequest;
import fr.epita.assistants.jws.presentation.rest.request.MovePlayerRequest;
import fr.epita.assistants.jws.presentation.rest.request.PutBombRequest;
import fr.epita.assistants.jws.presentation.rest.response.GameDetailResponse;
import fr.epita.assistants.jws.presentation.rest.response.GameListResponse;
import fr.epita.assistants.jws.utils.Point;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameEndpoint {

    @Inject
    GameService gs;

    @Path("")
    @GET
    public List<GameListResponse> Games()
    {
        return gs.initGameListResponse();
    }

    @Path("")
    @POST
    @Transactional
    public GameDetailResponse createGame(CreateGameRequest request)
    {
        if (request == null || request.name == null || request.toString().equals(""))
        {
            throw new BadRequestException();
        }
        try {
            return gs.initDetailResponse(gs.creategame(request.name));
        } catch (IOException e) {
            throw new BadRequestException();
        }
    }

    @Path("/{id}")
    @GET
    public GameDetailResponse gameInfo(@PathParam("id") long id)
    {
        return gs.initDetailResponse(id);
    }

    @Path("/{id}")
    @POST
    @Transactional
    public GameDetailResponse join(@PathParam("id") long id, JoinGameRequest request)
    {
        try {
            gs.Join(request.name,id);
            return gs.initDetailResponse(id);
        } catch (TooMuchPlayersException | GameAlreadyStartedException e) {
            throw new BadRequestException();
        }
    }

    @Path("/{id}/start")
    @PATCH
    public GameDetailResponse start(@PathParam("id") long id)
    {
        try {
            gs.start(id);
            return gs.initDetailResponse(id);
        } catch (GameAlreadyStartedException e) {
            throw new NotFoundException();
        }
    }

    @Path("/{gameId}/players/{playerId}/bomb")
    @POST
    public GameDetailResponse Bombastic(@PathParam("gameId") long gameId, @PathParam("playerId") long playerId, PutBombRequest request)
    {
        if (request == null || request.toString().equals(""))
        {
            throw new BadRequestException();
        }
        Point p = new Point(request.posX, request.posY);
        try {
            return gs.Bombastic(gameId,playerId,p);
        } catch (PlayerAlreadyBombedException e) {
            throw new WebApplicationException(Response.Status.TOO_MANY_REQUESTS);
        }
    }

    @Path("/{gameId}/players/{playerId}/move")
    @POST
    public GameDetailResponse Move(@PathParam("gameId") long gameId, @PathParam("playerId") long playerId, MovePlayerRequest request)
    {
        if (request == null || request.toString().equals(""))
        {
            throw new BadRequestException();
        }
        Point p = new Point(request.posX, request.posY);
        try {
            return gs.Move(gameId,playerId,p);
        } catch (PlayerAlreadyMovedException e) {
            throw new WebApplicationException(Response.Status.TOO_MANY_REQUESTS);
        }
    }

}

