package fr.epita.assistants.jws.presentation.rest.response;

import fr.epita.assistants.jws.utils.State;
import lombok.With;

@With
public class GameListResponse {
    public long id;
    public int players;
    public State state;

    public GameListResponse(long id, int player, State state) {
        this.id = id;
        this.players = player;
        this.state = state;
    }

    public GameListResponse() {
    }
}
