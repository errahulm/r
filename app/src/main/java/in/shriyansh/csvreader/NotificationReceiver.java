package in.shriyansh.csvreader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import in.shriyansh.csvreader.database.CSVContract;
import in.shriyansh.csvreader.database.DBMethods;
import in.shriyansh.csvreader.models.Word;


/**
 * Created by shriyanshgautam on 24/01/17.
 */

public class NotificationReceiver extends BroadcastReceiver{

    public static final int REQUEST_CODE = 1;
    DBMethods dbMethods;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    public static final String LAST_WORD_ID = "last_word_id";
    Word currentWord;

    @Override
    public void onReceive(Context context, Intent intent) {
        dbMethods = new DBMethods(context);
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        int lastWordId = sharedPreference.getInt(LAST_WORD_ID,0);
        int wordsCount = dbMethods.getAllWords().getCount();
        if(wordsCount>0){
            Cursor cursor = dbMethods.getAllWords();
            editor = sharedPreference.edit();
            if(lastWordId>=wordsCount){
                lastWordId=lastWordId%(wordsCount);
            }
            Log.d("Position",lastWordId+"");
            //Toast.makeText(context,lastWordId+"",Toast.LENGTH_SHORT).show();
            cursor.moveToPosition(lastWordId);
            editor.putInt(LAST_WORD_ID,++lastWordId);
            editor.commit();
            currentWord = new Word(cursor.getString(cursor.getColumnIndex(CSVContract.CSVTable.COL_WORD)),cursor.getString(cursor.getColumnIndex(CSVContract.CSVTable.COL_MEANING)),cursor.getString(cursor.getColumnIndex(CSVContract.CSVTable.COL_PRONUNCIATION)));
            cursor.close();
            dbMethods.insertHistoryWords(currentWord.getWord(),currentWord.getMeaning(),currentWord.getPronunciation());
            showNotification(context);
        }else{
            Log.d("Error in db","No words in db");
        }

    }

    public void showNotification(Context context) {
        Intent intent = new Intent(context, LastWords.class);
        PendingIntent pi = PendingIntent.getActivity(context, REQUEST_CODE, intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_find_in_page_white_24dp)
                .setContentTitle(currentWord.getWord())
                .setContentText(currentWord.getMeaning()+" ["+currentWord.getPronunciation()+"]");
        mBuilder.setContentIntent(pi);
        mBuilder.setSound(alarmSound);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(REQUEST_CODE, mBuilder.build());
    }
}
