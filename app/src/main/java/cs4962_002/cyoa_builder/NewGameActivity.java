package cs4962_002.cyoa_builder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;

/**
 * Created by Melynda on 12/5/2014.
 */
public class NewGameActivity extends Activity {
    private static final int REQUEST_CHOOSER = 1313; // The game that never was.
    private Boolean isXML = true;
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_script_screen);

        Intent intent = getIntent();
        Log.i("Intent Received: ", intent.toString());
    }

    public void launchCharacterCreation(View view)
    {
        if (!isXML || path == null || path == "") {
            Log.i("File Format == XML", isXML.toString());
            toastTime("Illegal path.");
        } else {
            GameState.getInstance().sanitize();
            GameState.getInstance().setScriptPath(path);
            Intent intent = new Intent(this, CharacterCreationActivity.class);
            startActivity(intent);
        }
    }

    /*
     *  launchFileDialog - Tied to the "Search" button in the load_script_screen.
     *  Launches iPaulPro's aFileChooser library: https://github.com/iPaulPro/aFileChooser
     */
    public void launchFileDialog(View view)
    {
        // Create the ACTION_GET_CONTENT Intent
        Intent getContentIntent = FileUtils.createGetContentIntent();

        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {

                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    path = FileUtils.getPath(this, uri);
                    String extension = FileUtils.getExtension(uri.toString());
                    Log.i("Script Extension:", extension);
                    //GameState.getInstance().setScriptPath(path);
                    ((EditText)findViewById(R.id.path_field)).setText(path);

                    if(!extension.equalsIgnoreCase(".XML"))
                        isXML = false;
                    else
                        isXML = true;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void toastTime(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
