package com.example.vocabulary_notebook.CRUD_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vocabulary_notebook.MainActivity;
import com.example.vocabulary_notebook.MyDatabaseHelper;
import com.example.vocabulary_notebook.R;

public class DeleteActivity extends AppCompatActivity {

    MyDatabaseHelper dbHelper=new MyDatabaseHelper(this,"words.db",null,1);


    public static void actionStart(Context context, String word) {
        Intent intent = new Intent(context, DeleteActivity.class);
        intent.putExtra("word", word);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_layout);

        //获取传入的信息
        Intent intent = getIntent();

        final String word = intent.getStringExtra("word");

        //初始化需要修改的值
        TextView wordText = (TextView) findViewById(R.id.delete_word);
        TextView meanText = (TextView) findViewById(R.id.delete_mean);
        TextView epText = (TextView) findViewById(R.id.delete_ep);

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

        Button ok=(Button)findViewById(R.id.delete_ok);
        Button cancel=(Button)findViewById(R.id.delete_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView meanText = (TextView) findViewById(R.id.delete_mean);
                TextView epText = (TextView) findViewById(R.id.delete_ep);
                if(wordIsOnly(word)==true){
                    Toast.makeText(DeleteActivity.this,word+"不存在",Toast.LENGTH_SHORT).show();
                }
                else{
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("delete from words where word=?",new String[]{word});//从数据库删除
                    db.close();
                    Toast.makeText(DeleteActivity.this,word+"删除成功",Toast.LENGTH_SHORT).show();
                }
                dbHelper.close();

                //返回MainActivity并清空活动栈
                Intent intent=new Intent(DeleteActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {//将原始值插入数据库
            @Override
            public void onClick(View v) {
                dbHelper.close();
                Toast.makeText(DeleteActivity.this, "取消删除", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    //判断word是否为新单词
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
