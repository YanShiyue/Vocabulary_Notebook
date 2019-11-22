package com.example.vocabulary_notebook.CRUD_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.vocabulary_notebook.MyDatabaseHelper;
import com.example.vocabulary_notebook.R;

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
                EditText ans=(EditText)findViewById(R.id.search_ans);
                //Log.d("word", s+"");
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(SearchActivity.this, "words.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor =db.rawQuery("select word,mean from words where word like ?",new String[]{"%"+s+"%"});
                ans.setText("");
                if(cursor.moveToFirst()){
                    do{
                        String word=cursor.getString(cursor.getColumnIndex("word"));
                        String mean=cursor.getString(cursor.getColumnIndex("mean"));
                        /*Log.d("MainActivity","words word is "+word);
                        Log.d("MainActivity","words mean is "+mean);*/
                        ans.append(word+"  "+mean+"\n");
                    }while(cursor.moveToNext());
                }
                cursor.close();
                db.close();
                dbHelper.close();
                //Log.d("MainActivity","words mean is "+before+" "+count+" ");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
