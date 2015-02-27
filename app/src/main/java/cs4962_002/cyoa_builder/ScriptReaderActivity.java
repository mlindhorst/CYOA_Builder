package cs4962_002.cyoa_builder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Melynda on 12/4/2014.
 */
public class ScriptReaderActivity extends Activity {
    public static String CONTINUE_EXTRA = "cont_bool";

    private HashMap<String, String> characters = new HashMap<String, String>();
    private Map<String, String> scenes = new HashMap<String, String>();
    private ArrayList<Pair<String, String>> currentChoices = new ArrayList<Pair<String, String>>();
    private List<String> choiceSpinnerList = new ArrayList<String>();

    private XmlPullParserFactory factory = null;
    private XmlPullParser parser = null;

    private String txt = "";
    private String id = "";
    private String choice = "";

    private Boolean imgTypeCharacter = true;
    private int eventType;
    private String waitForID = "";
    private Boolean isWaitChoices = false;
    private Boolean isContinuation = false;
    private Boolean waiting = false;
    private Boolean choiceReady = false;
    private Boolean waitSet = false;
    private FileInputStream script;
    private String currentScene = "";
    private String currentCharacter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        Intent intent = getIntent();
        isContinuation = intent.getBooleanExtra(CONTINUE_EXTRA, false);
        Log.i("Intent Received: ", intent.toString());

        if(isContinuation)
            load();
        else {
            openScript();
            nextPage(findViewById(R.id.next_page));
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        isContinuation = false;

        try
        {
            Gson gson = new Gson();
            String jsonAutoSave = gson.toJson(GameState.getInstance());
            File file = new File(getFilesDir(), "AutoSave.txt");
            FileWriter textWriter = new FileWriter(file);
            BufferedWriter bufferedTextWriter = new BufferedWriter(textWriter);
            bufferedTextWriter.write(jsonAutoSave);

            Log.i("Bookmarking page:", GameState.getInstance().getCurrentPage());

            bufferedTextWriter.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(isContinuation)
            load();
    }

    private void load()
    {
        try
        {
            File file = new File(getFilesDir(), "AutoSave.txt");
            FileReader textReader = new FileReader(file);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);
            String stateInfo = null;
            String wholeState = "";

            do
            {
                stateInfo = bufferedTextReader.readLine();
                wholeState += stateInfo;
            }while(stateInfo != null);

            if(wholeState != null) {
                wholeState = wholeState.replace("null", "");

                Gson getPaints = new Gson();
                Type gameToken = new TypeToken<GameState>() {
                }.getType();
                GameState.getInstance().loadGame((GameState) getPaints.fromJson(wholeState, gameToken));

                waitForID = GameState.getInstance().getCurrentPage();
                waiting = true;

                Log.i("Opening on page:", waitForID);

                openScript();
            }else{
                Log.i("On Resume:", "Cannot load state.");
                toastTime("Cannot load game state.");
            }

            bufferedTextReader.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void openScript()
    {
        if (GameState.getInstance().getScriptPath() == "" || GameState.getInstance().getScriptPath() == null) {
            Log.i("Script Reader: ", "Path does not exist.");
            return;
        }

        try {
            script = new FileInputStream(new File(GameState.getInstance().getScriptPath()));
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            parser.setInput(script, null);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        }
    }

    public void nextPage(View view)
    {
        if(choiceReady)
        {
            int pos = ((Spinner)findViewById(R.id.choice_spinner)).getSelectedItemPosition();
            GameState.getInstance().addChoice(currentChoices.get(pos).first, currentChoices.get(pos).second);

            if(waiting)
                waitForID = currentChoices.get(pos).second;

            choiceReady = false;
        }

        ((Spinner)findViewById(R.id.choice_spinner)).setVisibility(View.INVISIBLE);
        currentChoices.clear();
        choiceSpinnerList.clear();

        try {
            while(eventType != XmlPullParser.END_DOCUMENT) {

                eventType = parser.getEventType();

                String tagname = parser.getName();
                if(tagname == null || tagname == "")
                    tagname = "blank line";

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        //Log.i("START TAG", tagname);
                        if (tagname.equalsIgnoreCase("chapter")) {
                            GameState.getInstance().setCurrentChapter(parser.getAttributeValue(null, "title"));
                        } else if (tagname.equalsIgnoreCase("page")) {
                            String pgID = parser.getAttributeValue(null, "id");
                            String msg = waitForID + " vs " + pgID;
                            Log.i("waitForID vs. current ID", msg);
                            if(waiting && pgID.equals(waitForID))
                                waiting = false;
                            GameState.getInstance().setCurrentPage(pgID);
                        } else if (tagname.equalsIgnoreCase("characterImages")) {
                            imgTypeCharacter = true;
                        } else if (tagname.equalsIgnoreCase("sceneImages")) {
                            imgTypeCharacter = false;
                        } else if (tagname.equalsIgnoreCase("dialogue")) {
                            // Handle speaker attribute.
                            File img;
                            String s = parser.getAttributeValue(null, "speaker");
                            if (s != null && s.equals("NARRATOR"))
                                s = "";
                            else if (s != null && s.equals("PC"))
                                s = GameState.getInstance().getPc().getName();
                            ((TextView) findViewById(R.id.speaker_box)).setText(s);

                            // Handle scene attribute.
                            s = parser.getAttributeValue(null, "scene");
                            if(scenes.get(s) != null) {
                                Log.i("Scene Image " + s, scenes.get(s).toString());
                                currentScene = scenes.get(s);
                                img = new File(scenes.get(s));
                                Picasso.with(this).load(img).fit().into((ImageView) findViewById(R.id.scene_bg));
                            }

                            //Handle character attribute.
                            s = parser.getAttributeValue(null, "character");
                            if (s != null && s.equals("remove")) {
                                Log.i("CHARACTER IMAGE", "Removing...");
                                ((ImageView) findViewById(R.id.character)).setImageDrawable(null);
                            }else{
                                if(characters.get(s) != null) {
                                    Log.i("Character Image " + s, characters.get(s).toString());
                                    img = new File(characters.get(s));
                                    currentCharacter = characters.get(s);
                                    Picasso.with(this).load(img).fit().into((ImageView) findViewById(R.id.character));
                                }
                            }

                            //Handle retrieve_value attribute.
                            s = parser.getAttributeValue(null, "retrieve_value");
                            if (s != null && !waiting) {
                                choice = GameState.getInstance().getChoice(s);
                                Log.i("CHOICE", choice);
                                ((TextView)findViewById(R.id.dialogue_box)).setText(choice);
                                parser.next();
                                parser.next();
                                return;
                            }

                            //Handle jump_to attribute.
                            s = parser.getAttributeValue(null, "jump_to");
                            if (s != null) {
                                if(!waiting) {
                                    waitSet = true;
                                    waitForID = s;
                                }
                                Log.i("WAITING for page:", waitForID);
                            }
                        } else if (tagname.equalsIgnoreCase("image")) {
                            id = parser.getAttributeValue(null, "id");
                        } else if (tagname.equalsIgnoreCase("choices")) {
                            if(!waiting)
                                isWaitChoices = true;
                            currentChoices.clear();
                            id = parser.getAttributeValue(null, "id");
                        } else if (tagname.equalsIgnoreCase("choice")) {
                            String action = parser.getAttributeValue(null, "action");
                            String val = parser.getAttributeValue(null, "action_value");

                            if(action.equals("SAVE_VALUE"))
                                currentChoices.add(new Pair<String, String>(id, val));
                            else if(action.equals("JUMP_TO")) {
                                waiting = true;
                                currentChoices.add(new Pair<String, String>(id, val));
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        txt = parser.getText();
                        if (txt == null || txt == "")
                            txt = "NO TEXT";
                        //Log.i("TEXT TAG", txt);
                        break;

                    case XmlPullParser.END_TAG:
                        //Log.i("END TAG", tagname);
                        if (tagname.equalsIgnoreCase("image")) {
                            txt = txt.replace("\t", "");
                            txt = txt.replace("\n", "");
                            txt = txt.replace(" ", "");

                            if(imgTypeCharacter)
                                characters.put(id, txt);
                            else
                                scenes.put(id, txt);
                        } else if (tagname.equalsIgnoreCase("dialogue")) {
                            if(waitForID == null || waitForID == "" || waitSet || (waitForID.equals(GameState.getInstance().getCurrentPage()) && waitSet == false)) {
                                if(!waitSet)
                                    waitForID = "";

                                waiting = false;
                                waitSet = false;
                                txt = txt.replace("\t", "");
                                txt = txt.replace("\n", "");
                                if (txt != "" || txt != null)
                                    ((TextView) findViewById(R.id.dialogue_box)).setText(txt);

                                parser.next();
                                return;
                            }else{
                                break;
                            }
                        } else if (tagname.equalsIgnoreCase("choice")) {
                            txt = txt.replace("\t", "");
                            txt = txt.replace("\n", "");
                            choiceSpinnerList.add(txt);

                            if(choiceSpinnerList.size() >= 4 && (!waiting || isWaitChoices))
                            {
                                ArrayAdapter<String> choiceAdapter = new ArrayAdapter<String>
                                        (this, android.R.layout.simple_spinner_item, choiceSpinnerList);
                                ((Spinner)findViewById(R.id.choice_spinner)).setAdapter(choiceAdapter);
                                ((Spinner)findViewById(R.id.choice_spinner)).setVisibility(View.VISIBLE);
                                choiceReady = true;
                                parser.next();
                                return;
                            }
                        }else if (tagname.equalsIgnoreCase("choices")) {
                            waitSet = false;
                        }
                        break;

                    default:
                        //Log.i("Default", "HIT");
                        break;
                }

                parser.next();
            }

            if(eventType == XmlPullParser.END_DOCUMENT) {
                script.close();
                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.i("XML PULL PARSER", "Something terrible has occurred.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("I/O", "Something terrible has occurred.");
        }
    }

    public void toastTime(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}