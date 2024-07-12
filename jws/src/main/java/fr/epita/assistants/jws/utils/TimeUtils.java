package fr.epita.assistants.jws.utils;

public class TimeUtils {
    public static long getNumberOfMilli(long delay)
    {
        return delay * Long.parseLong(System.getenv("JWS_TICK_DURATION"));
    }
}
