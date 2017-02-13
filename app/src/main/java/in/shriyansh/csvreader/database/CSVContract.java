package in.shriyansh.csvreader.database;

import android.provider.BaseColumns;

/**
 * Created by shriyanshgautam on 13/01/17.
 */

public class CSVContract {

    public static final String DB_NAME = "csv.db";
    public static final int DB_VERSION  = 8;
    public static final String TYPE_TEXT = " TEXT ";
    public static final String TYPE_INTEGER = " INTEGER ";
    public static final String TYPE_PRIMARY_KEY = " PRIMARY KEY";
    public static final String COMMA = ", ";

    public static class CSVTable implements BaseColumns{
        public static final String TABLE_NAME = "csv_table";
        public static final String COL_WORD = "word";
        public static final String COL_MEANING = "meaning";
        public static final String COL_PRONUNCIATION = "pronunciation";

        public static final String CREATE_TABLE = "CREATE TABLE "+
                TABLE_NAME + " ( " +
                _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + COMMA+
                COL_WORD + TYPE_TEXT + COMMA +
                COL_MEANING + TYPE_TEXT + COMMA +
                COL_PRONUNCIATION + TYPE_TEXT +
                " ); ";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class CSVHistoryTable implements BaseColumns{
        public static final String TABLE_NAME = "csv_table_history";
        public static final String COL_WORD = "word";
        public static final String COL_MEANING = "meaning";
        public static final String COL_PRONUNCIATION = "pronunciation";
        public static final String COL_TIMESTAMP = "timestamp";

        public static final String CREATE_TABLE = "CREATE TABLE "+
                TABLE_NAME + " ( " +
                _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + COMMA+
                COL_WORD + TYPE_TEXT + COMMA +
                COL_MEANING + TYPE_TEXT + COMMA +
                COL_PRONUNCIATION + TYPE_TEXT +
                " ); ";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

}
