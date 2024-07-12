package fr.epita.assistants.presentation.rest.request;

import fr.epita.assistants.presentation.rest.response.ReverseResponse;

public class ReverseRequest {
    public String content;

    public ReverseRequest(String content) {
        this.content = content;
    }

    public ReverseRequest() {
        content = null;
    }

    public String reverse()
    {
        return (new ReverseResponse(content)).reversed;
    }
}

