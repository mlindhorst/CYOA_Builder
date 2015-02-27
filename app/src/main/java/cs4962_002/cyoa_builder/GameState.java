package cs4962_002.cyoa_builder;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Melynda on 12/3/2014.
 */
public class GameState
{
    private Player pc;
    private String currentChapter;
    private String currentPage = "1";
    private HashMap<String, String> playerChoices = new HashMap<String, String>();
    private String scriptPath;
    private int id;

    // Singleton!
    public static GameState _instance = null;

    public static GameState getInstance()
    {
        // TODO: Make sure singleton is threadsafe.
        if(_instance == null) {
            _instance = new GameState();
        }
        return _instance;
    }

    private GameState(){}

    public void loadGame(GameState loadedGame)
    {
        _instance = loadedGame;
    }

    public void sanitize()
    {
        _instance = new GameState();
    }

    private void fillAttributeChoices()
    {
        addChoice("NAME", pc.getName());
        addChoice("HAIR_COLOR", pc.getHairColor());
        addChoice("HAIR_STYLE", pc.getHairStyle());
        addChoice("EYE_COLOR", pc.getEyeColor());
        addChoice("STATURE", pc.getStature());
        addChoice("GENDER", pc.getGender().toString());
        addChoice("THEY", pc.getPronouns().get("They"));
        addChoice("THEIR", pc.getPronouns().get("Their"));
        addChoice("THEM", pc.getPronouns().get("Them"));
        addChoice("THEMSELF", pc.getPronouns().get("Themself"));
    }

    public void addChoice(String id, String choice)
    {
        playerChoices.put(id, choice);
    }

    public String getChoice(String id)
    {
        if(!playerChoices.containsKey(id))
            Log.i("Choice ID not found:", id);

        return playerChoices.get(id);
    }

    public HashMap<String, String> getPlayerChoices() {
        return playerChoices;
    }

    public void setPlayerChoices(HashMap<String, String> playerChoices) {
        this.playerChoices = playerChoices;
    }

    public Player getPc() {
        return pc;
    }

    public void setPc(Player pc) {
        this.pc = pc;

        fillAttributeChoices();
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public String getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(String currentChapter) {
        this.currentChapter = currentChapter;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
