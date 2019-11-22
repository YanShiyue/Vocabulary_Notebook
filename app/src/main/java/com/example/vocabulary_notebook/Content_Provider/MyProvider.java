package com.example.vocabulary_notebook.Content_Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.vocabulary_notebook.MyDatabaseHelper;

public class MyProvider extends ContentProvider {

    public static final int WORDS_DIR=0;
    public static final int WORDS_ITEM=1;
    public static final String AUTHORITY="com.example.Vocabulary_Notebook.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"words",WORDS_DIR);
        uriMatcher.addURI(AUTHORITY,"words/*",WORDS_ITEM);
    }

    public boolean onCreate(){
        dbHelper=new MyDatabaseHelper(getContext(),"words.db",null,1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
                //查询words表中所有数据
                cursor=db.query("words",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WORDS_ITEM:
                //查询words表中单条数据
                String words_word=uri.getPathSegments().get(1);
                cursor=db.query("words",projection,"word=?",new String[]{words_word},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //添加数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Uri uriReturn=null;
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
            case WORDS_ITEM:
                long i=db.insert("words",null,values);
                String id=values.getAsString("word");
                uriReturn=Uri.parse("content://"+AUTHORITY+"/words/"+id);
            default:break;
        }
        return uriReturn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
                updateRows=db.update("words",values,selection,selectionArgs);
                break;
            case WORDS_ITEM:
                String id=uri.getPathSegments().get(1);
                updateRows=db.update("words",values,"word=?",new String[]{id});
                break;
        }
        return updateRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int deleteRows=0;
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
                deleteRows=db.delete("words",selection,selectionArgs);
                break;
            case WORDS_ITEM:
                String id =uri.getPathSegments().get(1);
                deleteRows=db.delete("words","word=?",new String[]{id});
                break;
                default:break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.Vocabulary_Notebook.provider.words";
            case WORDS_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.Vocabulary_Notebook.provider.words";
        }
        return null;
    }
}
