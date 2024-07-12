package fr.epita.assistants.jws.utils;

import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    public static char TileToChar(TileType tileType)
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

    public static List<String> MapToMap(TileType[][] tileTypes)
    {
        List<String> res = new ArrayList<>();
        for (TileType[] a:tileTypes)
        {
            res.add(Mapline(a));
        }
        return res;
    }
}
