package in.shriyansh.csvreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shriyanshgautam on 13/01/17.
 */

public class DBMethods {

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private int HISTORY_COUNT = 10;

    public DBMethods(Context context){

        dbHelper = new DbHelper(context);
        db =  dbHelper.getWritableDatabase();
    }

    public long insertWords(String word,String meaning,String pronunciation){
        ContentValues values = new ContentValues();
        values.put(CSVContract.CSVTable.COL_WORD,word);
        values.put(CSVContract.CSVTable.COL_MEANING,meaning);
        values.put(CSVContract.CSVTable.COL_PRONUNCIATION,pronunciation);
        return db.insert(CSVContract.CSVTable.TABLE_NAME,null,values);
    }

    public int deleteAllWords(){
        return db.delete(CSVContract.CSVTable.TABLE_NAME,null,null);
    }

    public Cursor getWordRecord(String word){
        return db.query(CSVContract.CSVTable.TABLE_NAME,null, CSVContract.CSVTable.COL_WORD+" = ? "+" COLLATE NOCASE ",new String[]{word},null,null,null);
    }

    public Cursor getAllWords(){
        return db.query(CSVContract.CSVTable.TABLE_NAME,null, null,null,null,null,null);
    }

    public Cursor getWordById(long id){
        return db.query(CSVContract.CSVTable.TABLE_NAME,null, CSVContract.CSVTable._ID+" =?",new String[]{id+""},null,null,null);
    }
//    History

    public Cursor getAllHistoryWords(){
        return db.query(CSVContract.CSVHistoryTable.TABLE_NAME,null, null,null,null,null, CSVContract.CSVHistoryTable._ID+" DESC",null);
    }

    public long insertHistoryWords(String word,String meaning,String pronunciation){
        if(getAllHistoryWords().getCount()>=HISTORY_COUNT){
            Cursor cursor = db.query(CSVContract.CSVHistoryTable.TABLE_NAME,null,null,null,null,null,CSVContract.CSVHistoryTable._ID+" DESC","1");
            if(cursor.moveToFirst()){
                db.delete(CSVContract.CSVHistoryTable.TABLE_NAME,CSVContract.CSVHistoryTable._ID+" <=?",new String[]{(cursor.getLong(cursor.getColumnIndex(CSVContract.CSVHistoryTable._ID))-9)+""});
            }
        }

        ContentValues values = new ContentValues();
        values.put(CSVContract.CSVHistoryTable.COL_WORD,word);
        values.put(CSVContract.CSVHistoryTable.COL_MEANING,meaning);
        values.put(CSVContract.CSVHistoryTable.COL_PRONUNCIATION,pronunciation);
        return db.insert(CSVContract.CSVHistoryTable.TABLE_NAME,null,values);


    }

    public Cursor getHistoryWordById(long id){
        return db.query(CSVContract.CSVHistoryTable.TABLE_NAME,null, CSVContract.CSVTable._ID+" =?",new String[]{id+""},null,null,null);
    }

    public int deleteAllHistoryWords(){
        return db.delete(CSVContract.CSVHistoryTable.TABLE_NAME,null,null);
    }



}
