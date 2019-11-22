package com.example.vocabulary_notebook.CRUD_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vocabulary_notebook.MainActivity;
import com.example.vocabulary_notebook.MyDatabaseHelper;
import com.example.vocabulary_notebook.R;

public class ChangeActivity extends AppCompatActivity {

    //数据库创建
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "words.db", null, 1);

    //启动活动，传入word
    public static void actionStart(Context context, String word) {
        Intent intent = new Intent(context, ChangeActivity.class);
        intent.putExtra("word", word);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_layout);

        //获取传入的信息
        Intent intent = getIntent();

        final String word = intent.getStringExtra("word");

        //初始化需要修改的值
        TextView wordText = (TextView) findViewById(R.id.change_word);
        EditText meanText = (EditText) findViewById(R.id.change_mean);
        EditText epText = (EditText) findViewById(R.id.change_ep);

        //初始化Text
        wordText.setText(word);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor=db.query("words",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndex("word")).equals(word)){
                    meanText.setText(cursor.getString(cursor.getColumnIndex("mean")));
                    epText.setText(cursor.getString(cursor.getColumnIndex("ep")));
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        Button ok = (Button) findViewById(R.id.change_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText meanText = (EditText) findViewById(R.id.change_mean);
                EditText epText = (EditText) findViewById(R.id.change_ep);
                if(wordIsOnly(word)==true){
                    Toast.makeText(ChangeActivity.this,word+"不存在",Toast.LENGTH_SHORT).show();
                }
                else if(meanText.length()==0){
                    Toast.makeText(ChangeActivity.this,"修改失败，释义不能为空",Toast.LENGTH_SHORT).show();
                }
                else{//修改成功结束
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("update words set mean=? where word=?",new String[]{meanText.getText().toString(),word});
                    db.execSQL("update words set ep=? where word=?",new String[]{epText.getText().toString(),word});
                    db.close();
                    Toast.makeText(ChangeActivity.this,word+"修改成功",Toast.LENGTH_SHORT).show();

                    dbHelper.close();
                    //返回MainActivity并清空活动栈
                    Intent intent=new Intent(ChangeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
        Button cancel = (Button) findViewById(R.id.change_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {//将原始值插入数据库
            @Override
            public void onClick(View v) {
                dbHelper.close();
                Toast.makeText(ChangeActivity.this, "取消修改", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    boolean wordIsOnly(String word){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor=db.query("words",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{

                String _word=cursor.getString(cursor.getColumnIndex("word"));
                if(_word.equals(word)){
                    cursor.close();
                    db.close();
                    return false;
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return true;
    }
}
