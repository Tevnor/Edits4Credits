package basic.editor;

import java.util.Date;

public class Artwork {

    private int id;
    private String name;
    private String artist;
    private Date creationDate;
    private Date updateDate;
    private Image art;                  //only one Image per Artwork or more (Array??)

    public Artwork(int id, String name, String artist, Date creationDate, Date updateDate){

        this.id = id;
        this.name = name;
        this.artist = artist;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public void newImage(){
        this.art = new Image(); //TODO create new Image(Artwork)
    }

    public void save(){
        //TODO save Artwork
    }


    public void print(){
        //TODO print Image(Artwork)
    }

}
