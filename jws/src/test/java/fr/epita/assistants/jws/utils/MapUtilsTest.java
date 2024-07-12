package fr.epita.assistants.jws.utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapUtilsTest {

    static char TileToChar(TileType tileType)
    {
        char a = 'N';
        switch (tileType)
        {
            case GROUND -> a = 'G';
            case WOOD -> a = 'W';
            case METAL -> a = 'M';
            case BOMB -> a = 'B';
        }
        return a;
    }
    static String Mapline(TileType[] tileTypes)
    {
        String res = "";
        int i = 0;
        while (i < tileTypes.length)
        {
            int j = 0;
            char fchar = TileToChar(tileTypes[i]);
            while (j < 9 && i < tileTypes.length && TileToChar(tileTypes[i]) == fchar)
            {
                j++;
                i++;
            }
            res = String.format("%s%d%c",res,j,fchar);
        }
        return res;
    }

    @Test
    void mapline() {
        List<TileType> po = new ArrayList<>();
        for (var i = 0; i < 9;i++)
        {
            po.add(TileType.METAL);
        }
        for (var i = 0; i < 8;i++)
        {
            po.add(TileType.METAL);
        }
        System.out.println(Mapline(po.toArray(new TileType[0])));
    }

    public static long getNumberOfMilli(long delay)
    {
        return delay * 1000;
    }
    public boolean canMove(String lastmove,long delay)
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime a = LocalDateTime.parse(lastmove);
        LocalDateTime n = a.plus(getNumberOfMilli(delay),ChronoUnit.MILLIS);
        return !n.isAfter(now);
    }

    @Test
    void canm()
    {
        String lm = LocalDateTime.now().toString();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(canMove(lm,1));
    }
}