package cs4962_002.cyoa_builder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Melynda on 12/6/2014.
 */
public class CharacterCreationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_creation_screen);

        Intent intent = getIntent();
        Log.i("Intent Received: ", intent.toString());
    }

    public void startGame(View view)
    {
        Player pc = getPlayer();
        GameState.getInstance().setPc(pc);
        Log.i("NEW PLAYER", pc.getName());

        Intent continueDetail = new Intent();
        continueDetail.putExtra(ScriptReaderActivity.CONTINUE_EXTRA, false);
        continueDetail.setClass(CharacterCreationActivity.this, ScriptReaderActivity.class);
        startActivity(continueDetail);
    }

    private Player getPlayer()
    {
        Player playa = new Player();

        // Retrieve all info from character creator.
        String nameField = ((EditText)findViewById(R.id.name_field)).getText().toString();
        RadioGroup genderRadios = (RadioGroup)findViewById(R.id.rg_gender);
        String genderButton = ((RadioButton)findViewById(genderRadios.getCheckedRadioButtonId())).getText().toString();
        Boolean defaultPronouns = ((RadioButton)findViewById(R.id.default_pronouns)).isChecked();
        String theyField = "They", themField = "Them", theirField = "Their", themselfField = "Themself";
            if(!defaultPronouns)
            {
                theyField = ((EditText)findViewById(R.id.they_field)).getText().toString();
                themField = ((EditText)findViewById(R.id.them_field)).getText().toString();
                theirField = ((EditText)findViewById(R.id.their_field)).getText().toString();
                themselfField = ((EditText)findViewById(R.id.themself_field)).getText().toString();
            }
        String hairCField = ((Spinner)findViewById(R.id.hair_color_spinner)).getSelectedItem().toString();
        String hairSField = ((Spinner)findViewById(R.id.hair_style_spinner)).getSelectedItem().toString();
        String eyeCField = ((Spinner)findViewById(R.id.eye_color_spinner)).getSelectedItem().toString();
        String statureField = ((Spinner)findViewById(R.id.stature_spinner)).getSelectedItem().toString();

        // Check all information, set to default if not filled in.
        if(nameField != "" || nameField != null)
            playa.setName(nameField);
        if(genderButton != "" || genderButton != null)
            playa.setGender(Gender.valueOf(genderButton));
        if(!defaultPronouns)
        {
            Map<String, String> pros = new HashMap<String, String>();
            if(theyField != "" || theyField != null)
                pros.put("They", theyField);
            if(themField != "" || themField != null)
                pros.put("Them", themField);
            if(theirField != "" || theirField != null)
                pros.put("Their", theirField);
            if(themselfField != "" || themselfField != null)
                pros.put("Themself", themselfField);

            playa.setPronouns(pros);
        }
        if(hairCField != "" || hairCField != null)
            playa.setHairColor(hairCField);
        if(hairSField != "" || hairSField != null)
            playa.setHairStyle(hairSField);
        if(eyeCField != "" || eyeCField != null)
            playa.setEyeColor(eyeCField);
        if(statureField != "" || statureField != null)
            playa.setStature(statureField);

        return playa;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
