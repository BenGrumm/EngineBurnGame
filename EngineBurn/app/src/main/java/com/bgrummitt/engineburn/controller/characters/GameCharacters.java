package com.bgrummitt.engineburn.controller.characters;

import com.bgrummitt.engineburn.R;

public class GameCharacters {

    final static public String DEFAULT_UFO = "DEFAULT_UFO";
    final static public String RED_UFO = "RED_UFO";
    final static public String GREEN_UFO = "GREEN_UFO";
    final static public String BLUE_UFO = "BLUE_UFO";
    final static public String YELLOW_UFO = "YELLOW_UFO";

    static private GameCharacter[] gameCharacters;

    /**
     *
     * @return
     */
    public static GameCharacter[] getGameCharacters(){
        gameCharacters = new GameCharacter[5];
        gameCharacters[0] = new GameCharacter(DEFAULT_UFO, R.drawable.default_ufo, R.drawable.default_ufo_low_fire, R.drawable.default_ufo_no_fire);
        gameCharacters[1] = new GameCharacter(RED_UFO, R.drawable.red_ufo, R.drawable.red_ufo_low_fire, R.drawable.red_ufo_no_fire);
        gameCharacters[2] = new GameCharacter(GREEN_UFO, R.drawable.green_ufo, R.drawable.green_ufo_low_fire, R.drawable.green_ufo_no_fire);
        gameCharacters[3] = new GameCharacter(BLUE_UFO, R.drawable.blue_ufo, R.drawable.blue_ufo_low_fire, R.drawable.blue_ufo_no_fire);
        gameCharacters[4] = new GameCharacter(YELLOW_UFO, R.drawable.yellow_ufo, R.drawable.yellow_ufo_low_fire, R.drawable.yellow_ufo_no_fire);
        return gameCharacters;
    }

    /**
     * Function to return character with given name
     * @param name name of character to retrieve
     * @return character asked for
     */
    public static GameCharacter getCharacter(String name){
        if(gameCharacters == null)
            gameCharacters = getGameCharacters();
        for(GameCharacter character : gameCharacters){
            if(character.getCharacterName().equals(name)){
                return character;
            }
        }
        return null;
    }

}
