package in.shriyansh.csvreader;

import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Locale;

import in.shriyansh.csvreader.adapters.WordsAdapter;
import in.shriyansh.csvreader.database.CSVContract;
import in.shriyansh.csvreader.database.DBMethods;

public class LastWords extends AppCompatActivity {

    WordsAdapter wordsAdapter;
    DBMethods dbMethods;
    ListView wordListView;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_words);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUI();

        dbMethods = new DBMethods(this);
        wordsAdapter = new WordsAdapter(this,dbMethods.getAllHistoryWords());
        wordListView.setAdapter(wordsAdapter);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(new Locale("hi"));
                }
            }
        });

        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = dbMethods.getHistoryWordById(id);
                if(cursor.moveToFirst()){
                    textToSpeech.speak(cursor.getString(cursor.getColumnIndex(CSVContract.CSVHistoryTable.COL_MEANING)),TextToSpeech.QUEUE_FLUSH,null);
                    cursor.close();
                }
            }
        });
    }

    private void initUI(){
        wordListView = (ListView)findViewById(R.id.privious_words_list);
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
