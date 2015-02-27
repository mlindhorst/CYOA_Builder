package cs4962_002.cyoa_builder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;


public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        File file = getBaseContext().getFileStreamPath("AutoSave.txt");
        if(file.exists())
            ((Button)findViewById(R.id.continue_button)).setClickable(true);
        else
            ((Button)findViewById(R.id.continue_button)).setClickable(false);
    }

    public void newGame(View view)
    {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }

    public void continueGame(View view)
    {
        Intent continueDetail = new Intent();
        continueDetail.putExtra(ScriptReaderActivity.CONTINUE_EXTRA, true);
        continueDetail.setClass(MainMenuActivity.this, ScriptReaderActivity.class);
        startActivity(continueDetail);
    }

    @Override
    protected void onResume() {
        super.onResume();

        File file = getBaseContext().getFileStreamPath("AutoSave.txt");
        if(file.exists())
            ((Button)findViewById(R.id.continue_button)).setClickable(true);
        else
            ((Button)findViewById(R.id.continue_button)).setClickable(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
