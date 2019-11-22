package com.example.vocabulary_notebook.CRUD_Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vocabulary_notebook.MainActivity;
import com.example.vocabulary_notebook.MyDatabaseHelper;
import com.example.vocabulary_notebook.R;

public class AddActivity extends AppCompatActivity {

    //数据库创建
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "words.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);

        Button ok=(Button)findViewById(R.id.add_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText wordText=(EditText)findViewById(R.id.add_word);
                final EditText meanText=(EditText)findViewById(R.id.add_mean);
                final EditText epText=(EditText)findViewById(R.id.add_ep);
                if(wordText.length()==0){//word为空，出错
                    Toast.makeText(AddActivity.this,"添加失败，单词不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(wordIsOnly(wordText.getText().toString())==false){
                    Toast.makeText(AddActivity.this,"添加失败，"+wordText.getText().toString()+"已经存在",Toast.LENGTH_SHORT).show();
                }
                else if(meanText.length()==0){//mean为空，出错
                    Toast.makeText(AddActivity.this,"添加失败，释义不能为空",Toast.LENGTH_SHORT).show();
                }
                else {//添加成功结束
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("insert into words(word,mean,ep) values(?,?,?)",new String[]{wordText.getText().toString(),meanText.getText().toString(),epText.getText().toString()});
                    Toast.makeText(AddActivity.this,wordText.getText().toString()+"添加成功",Toast.LENGTH_SHORT).show();
                    db.close();
                    dbHelper.close();

                    AlertDialog.Builder dialog=new AlertDialog.Builder(AddActivity.this);

                    dialog.setMessage("是否继续添加单词?");//设置信息具体内容
                    dialog.setCancelable(false);//设置是否可取消
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override//设置ok的事件
                        public void onClick(DialogInterface dialogInterface, int i) {
                            wordText.setText("");
                            meanText.setText("");
                            epText.setText("");
                        }
                    });
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override//设置取消事件
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //返回MainActivity并清空活动栈
                            Intent intent=new Intent(AddActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }
            }
        });
        Button cancel=(Button)findViewById(R.id.add_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.close();
                Toast.makeText(AddActivity.this, "取消添加", Toast.LENGTH_SHORT).show();
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
