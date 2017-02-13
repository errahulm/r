package in.shriyansh.csvreader;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import in.shriyansh.csvreader.database.CSVContract;
import in.shriyansh.csvreader.database.DBMethods;
import in.shriyansh.csvreader.models.Word;

import static in.shriyansh.csvreader.NotificationReceiver.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {


    SharedPreferences permissionStatus;

    TextView resultView,resultPronunciation;
    EditText queryView;
    Button searchButton,resetButton;
    DBMethods dbMethods;
    TextToSpeech textToSpeech;

    int INTERVAL = (int)AlarmManager.INTERVAL_DAY;
    long START_TIME = 1485725078183L;
    public static int COLUMNS_TO_READ = 3;
    public static int COL_WORD = 0;
    public static int COL_MEANING = 1;
    public static int COL_PRONUNCIATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(getResources().getString(R.string.hindi_app_name));
        toolbar.setSubtitle(getResources().getString(R.string.app_name));

        permissionStatus = PreferenceManager.getDefaultSharedPreferences(this);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(new Locale("hi"));
                }
            }
        });

        initUI();
        dbMethods = new DBMethods(this);


        if(dbMethods.getAllWords().getCount()==0){
            //database empty
            readCSVFile();
            Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE, intent, 0);
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.setRepeating(am.RTC_WAKEUP, START_TIME, INTERVAL, pendingIntent);
        }else {
            Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE, intent, 0);
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.setRepeating(am.RTC_WAKEUP, START_TIME, INTERVAL, pendingIntent);
        }


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    if(!queryView.getText().toString().contentEquals("")){
                        Cursor cursor = dbMethods.getWordRecord(queryView.getText().toString());
                        if(cursor.moveToFirst()){
                            resultView.setText(cursor.getString(cursor.getColumnIndex(CSVContract.CSVTable.COL_MEANING)));
                            resultPronunciation.setText(cursor.getString(cursor.getColumnIndex(CSVContract.CSVTable.COL_PRONUNCIATION)));

                        }else{
                            Snackbar.make(view, "Word not found", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            resultView.setText("");
                            resultPronunciation.setText("");
                        }
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }else{
                        Snackbar.make(view, "Write a query", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        resultView.setText("");
                        resultPronunciation.setText("");
                    }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryView.setText("");
                resultView.setText("");
                resultPronunciation.setText("");
            }
        });

        resultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!resultView.getText().toString().isEmpty())
                textToSpeech.speak(resultView.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        resultPronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!resultView.getText().toString().isEmpty())
                    textToSpeech.speak(resultView.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    public void initUI(){

        queryView = (EditText)findViewById(R.id.query) ;
        resultView = (TextView)findViewById(R.id.result);
        resultPronunciation = (TextView)findViewById(R.id.result_pronunciation);
        resetButton = (Button)findViewById(R.id.reset_button);
        searchButton = (Button)findViewById(R.id.search_button);


    }


    private void readCSVFile(){

        dbMethods.deleteAllWords();
        //*Don't* hardcode "/sdcard"
        //File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        //File file = new File(sdcard,"dictionary.csv");


        //Read text from file
        StringBuilder text = new StringBuilder();
        //Log.d("File",(file!=null)+"");

        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("dictionary.csv"));
            BufferedReader br = new BufferedReader(is);
            String line;
            Log.d("File",br.readLine());
            while ((line = br.readLine()) != null) {
                text.append(line);
                String[] row = line.toString().split(",");
                Log.d("Line",line+" "+row.length);
                if(row.length==COLUMNS_TO_READ){
                    if(!row[COL_WORD].contentEquals("") && !row[COL_MEANING].contentEquals("")){
                        dbMethods.insertWords(row[COL_WORD],row[COL_MEANING],row[COL_PRONUNCIATION]);
                    }
                }

                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_all_words) {
            //readCSVFile();
            startActivity(new Intent(this,AllWords.class));
            return true;
        }else if(id == R.id.action_last_words){
            startActivity(new Intent(this,LastWords.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
}
