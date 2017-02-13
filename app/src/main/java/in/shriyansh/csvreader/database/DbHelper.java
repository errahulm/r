package in.shriyansh.csvreader.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shriyanshgautam on 13/01/17.
 */

public class DbHelper extends SQLiteOpenHelper{


    public DbHelper(Context context) {
        super(context, CSVContract.DB_NAME, null, CSVContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CSVContract.CSVTable.CREATE_TABLE);
        db.execSQL(CSVContract.CSVHistoryTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CSVContract.CSVTable.DELETE_TABLE);
        db.execSQL(CSVContract.CSVHistoryTable.DELETE_TABLE);
        onCreate(db);
    }

}
