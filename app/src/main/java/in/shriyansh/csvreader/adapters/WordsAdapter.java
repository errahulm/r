package in.shriyansh.csvreader.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import in.shriyansh.csvreader.R;
import in.shriyansh.csvreader.database.CSVContract;

/**
 * Created by shriyanshgautam on 29/01/17.
 */

public class WordsAdapter extends CursorAdapter {
    public WordsAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.word_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView wordTv = (TextView) view.findViewById(R.id.item_word);
        TextView wordMeaning = (TextView) view.findViewById(R.id.item_meaning);
        TextView wordPronunciation = (TextView) view.findViewById(R.id.item_pronunciation);
        wordTv.setText(cursor.getString(cursor.getColumnIndex(CSVContract.CSVTable.COL_WORD)));
        wordMeaning.setText(cursor.getString(cursor.getColumnIndex(CSVContract.CSVTable.COL_MEANING)));
        wordPronunciation.setText(cursor.getString(cursor.getColumnIndex(CSVContract.CSVTable.COL_PRONUNCIATION)));
    }
}
