package com.example.vocabulary_notebook;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

public class WordsAdapter extends ArrayAdapter<Words> {

    int resourceId;
    public WordsAdapter(Context context,int textViewResourceId,List<Words> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Words words=getItem(position);//获取当前项的Words示例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView wordsWord=(TextView)view.findViewById(R.id.words_word);
        TextView wordsMean=(TextView)view.findViewById(R.id.words_mean);
        wordsWord.setText(words.getWord());
        wordsMean.setText(words.getMean());
        return view;
    }
}
