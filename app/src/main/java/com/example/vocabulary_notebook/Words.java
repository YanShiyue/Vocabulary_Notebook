package com.example.vocabulary_notebook;

public class Words {
    private String word;
    private String mean;
    public Words(String word,String mean){
        this.word=word;
        this.mean=mean;
    }
    public String getWord(){
        return this.word;
    }
    public String getMean(){
        return this.mean;
    }
}
