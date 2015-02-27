package cs4962_002.cyoa_builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Melynda on 12/13/2014.
 */
public class Player
{
    private String name;
    private Gender gender;
    private Map<String, String> pronouns = new HashMap<String, String>();
    private String hairColor;
    private String hairStyle;
    private String eyeColor;
    private String stature;

    public Player()
    {
        name = "you";
        gender = Gender.NB;
        initializePronouns(Gender.NB);
        hairColor = "Brown";
        hairStyle = "Short";
        eyeColor = "Brown";
        stature = "Medium";
    }

    public Player(String n, String g)
    {
        name = n;
        gender = Gender.valueOf(g);
        initializePronouns(gender);
    }

    public Player(String n, String g, String[] p)
    {
        name = n;
        gender = Gender.valueOf(g);
        initializePronouns(p);
    }

    private void initializePronouns(Gender g)
    {
        if(g == Gender.FEMALE)
        {
            pronouns.put("they", "she");
            pronouns.put("them", "her");
            pronouns.put("their", "her");
            pronouns.put("themself", "herself");
        }
        else if(g == Gender.MALE)
        {
            pronouns.put("they", "he");
            pronouns.put("them", "him");
            pronouns.put("their", "his");
            pronouns.put("themself", "himself");
        }
        else
        {
            pronouns.put("they", "they");
            pronouns.put("them", "them");
            pronouns.put("their", "their");
            pronouns.put("themself", "themself");
        }
    }

    private void initializePronouns(String[] p)
    {
        if(p.length >= 4) {
            pronouns.put("they", p[0]);
            pronouns.put("them", p[1]);
            pronouns.put("their", p[2]);
            pronouns.put("themself", p[3]);
        }
        else
            initializePronouns(Gender.NB);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Map<String, String> getPronouns() {
        return pronouns;
    }

    public void setPronouns(Map<String, String> pronouns) {
        this.pronouns = pronouns;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(String hairStyle) {
        this.hairStyle = hairStyle;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getStature() {
        return stature;
    }

    public void setStature(String stature) {
        this.stature = stature;
    }
}