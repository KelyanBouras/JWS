package fr.epita.assistants.jws.presentation.rest.response;

import fr.epita.assistants.jws.utils.State;

import java.util.List;

public class GameDetailResponse {
    public String startTime;
    public State state;
    public List<PlayerResponse> players;

    public List<String> map;
    public long id;

    public GameDetailResponse() {
    }
}
