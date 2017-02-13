package in.shriyansh.csvreader.models;

/**
 * Created by shriyanshgautam on 13/01/17.
 */

public class Word {

    String word;
    String meaning;
    String pronunciation;

    public Word(String word,String meaning,String pronunciation){
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
    }

    public String getWord(){
        return this.word;
    }

    public String getMeaning(){
        return this.meaning;
    }

    public String getPronunciation(){ return this.pronunciation; }

    public void setWord(String word){
        this.word = word;
    }

    public void setMeaning(String meaning){
        this.meaning = meaning;
    }

    public void setPronunciation(String pronunciation){
        this.pronunciation = pronunciation;
    }
}
