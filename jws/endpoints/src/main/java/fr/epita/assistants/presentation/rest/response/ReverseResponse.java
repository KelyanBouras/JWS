package fr.epita.assistants.presentation.rest.response;

public class ReverseResponse {
    public String original;
    public String reversed;

    void reverse()
    {
        StringBuilder sb = new StringBuilder(original);
        reversed = sb.reverse().toString();
    }

    public ReverseResponse(String original) {
        this.original = original;
        reverse();
    }
}
