package com.example.vocabulary_notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    MyDatabaseHelper dbHelper=new MyDatabaseHelper(this,"words.db",null,1);
    SQLiteDatabase db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        refrshList();//初始化listView

        ListView listView=(ListView)findViewById(R.id.word_list_view);
        //长按单词列表某一项进行put_to_newWord,update,delete
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {//长按listview弹出增删选项
                contextMenu.add(Menu.NONE, 0, 0, "添加到生词本");
                contextMenu.add(Menu.NONE, 1, 0, "修改");
                contextMenu.add(Menu.NONE, 2, 0, "删除");
            }
        });
        //横屏：点击左列单词加载右边碎片
        if(this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT) {//横屏
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TextView word=(TextView)view.findViewById(R.id.words_word);//获取listView点击项的内容
                    /*Log.d("word", word.getText().toString());*///获取选择项的内容（内置android.R.layout.simple_list_item_1）
                    String[]Word=getWord(parent.getItemAtPosition(position).toString());
                    MeanFragment meanFragment=(MeanFragment)getSupportFragmentManager().findFragmentById(R.id.mean_fragment);
                    meanFragment.refresh(Word[0],Word[1],Word[2]);//刷新右侧布局
                }
            });
        }
        //查询触发
        EditText search= (EditText) findViewById(R.id.search);
        search.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent);
                    EditText et = (EditText) findViewById(R.id.search);
                    et.clearFocus();
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });
    }


    //上下文菜单监听（添加到生词本，删除，修改）
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        ListView listView=(ListView)findViewById(R.id.word_list_view);
        String word=new String();//获取点击的单词
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            TextView wordText=(TextView)listView.getChildAt(menuInfo.position).findViewById(R.id.words_word);
            word=wordText.getText().toString();
            Log.d("word", wordText.getText().toString());
        }
        else{
            //parent.getItemAtPosition(position).toString()  //menuInfo.position
            word=listView.getItemAtPosition(menuInfo.position).toString();
            //Log.d("wordText", word);
        }
        switch (item.getItemId()){
            case 0:
                //添加单词到生词本
                db=dbHelper.getWritableDatabase();
                db.execSQL("update words set new=? where word=?",new String[]{"1",word});
                db.close();
                refrshList();//实时刷新listView
                Toast.makeText(MainActivity.this, "生词本添加成功："+word, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                //修改单词
                ChangeActivity.actionStart(MainActivity.this,word);//调用ChangeActivity修改
                break;
            case 2:
                //删除单词
                DeleteActivity.actionStart(MainActivity.this,word);//调用DeleteActivity修改
                break;
        }
        return super.onContextItemSelected(item);
    }
    //Menu菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.newWords_item://打开生词本
                break;
            case R.id.add_item://添加单词
                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                break;
            case R.id.search_inter_item://联网查找
                break;
        }
        return true;
    }
    //根据数据库更新ListView内容
    public void refrshList(){
        List<Words> wordsList1=new ArrayList<>();
        List<String> wordsList2=new ArrayList<>();
        //将数据库内容读入wordList
        db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("words",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                String word=cursor.getString(cursor.getColumnIndex("word"));
                String mean=cursor.getString(cursor.getColumnIndex("mean"));
                if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){//竖屏操作
                    Words words=new Words(word,mean);
                    wordsList1.add(words);
                }
                else{
                    wordsList2.add(word);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        //wordlist的内容通过Adapter添加到listview（横竖屏listview适配器不同）
        ListView listView=(ListView)findViewById(R.id.word_list_view);
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            WordsAdapter adapter=new WordsAdapter(MainActivity.this,R.layout.words_item,wordsList1);
            listView.setAdapter(adapter);
        }
        else {
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,wordsList2);
            listView.setAdapter(adapter);
        }
    }
    //已知word获取Word详情
    public String[] getWord(String word){
        db=dbHelper.getWritableDatabase();//依据word查询数据库
        Cursor cursor=db.rawQuery("select * from words where word=?",new String[]{word});
        cursor.moveToFirst();
        String mean=cursor.getString(cursor.getColumnIndex("mean"));
        String ep=cursor.getString(cursor.getColumnIndex("ep"));
        String _new=cursor.getString(cursor.getColumnIndex("new"));
        cursor.close();
        db.close();
        String[] Word=new String[]{word,mean,ep,_new};
        return Word;
    }
}


        /*//查询数据库words所有数据
        Cursor cursor=db.query("words",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                String word=cursor.getString(cursor.getColumnIndex("word"));
                String mean=cursor.getString(cursor.getColumnIndex("mean"));
                String ep=cursor.getString(cursor.getColumnIndex("ep"));
                String _new=cursor.getString(cursor.getColumnIndex("new"));
                Log.d("MainActivity","words word is "+word);
                Log.d("MainActivity","words mean is "+mean);
                Log.d("MainActivity","words ep is "+ep);
                Log.d("MainActivity","words new is "+_new);
            }while(cursor.moveToNext());
        }*/