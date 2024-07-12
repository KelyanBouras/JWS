package fr.epita.assistants.jws.utils;

public enum TileType {
    METAL,
    GROUND,
    WOOD,
    BOMB;

    public TileType switchtype(String letter)
    {
        TileType tileType = WOOD;
        switch (letter)
        {
            case "M" -> tileType = METAL;
            case "B" -> tileType = BOMB;
            case "G" -> tileType = GROUND;
        }
        return tileType;
    }
}
