package com.example.vocabulary_notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        EditText ques=(EditText)findViewById(R.id.search_ques);
        //为EditText设置监听，注意监听类型为TextWatcher
        ques.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*EditText ques=(EditText)findViewById(R.id.search_ques);
                //Log.d("word", s+"");
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(SearchActivity.this, "words.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("words",new String[]{"word","mean"},"? like ?",new String[]{"word","''+%s%+''"},null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        String _word=cursor.getString(cursor.getColumnIndex("word"));
                    }while(cursor.moveToNext());
                }
                cursor.close();
                db.close();
                dbHelper.close();*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
