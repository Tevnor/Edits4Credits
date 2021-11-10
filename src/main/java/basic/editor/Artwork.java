package basic.editor;

import java.util.Date;

public class Artwork {

    private int id;
    private String name;
    private String artist;
    private Date creationDate;
    private Date updateDate;
    private Image art;

    public Artwork(int id, String name, String artist){

        this.id = id;
        this.name = name;
        this.artist = artist;
        this.creationDate = new Date(System.currentTimeMillis());
    }

    public void newImage(long xDim, long yDim){
        this.art = new Image(xDim, yDim); //TODO create new Image(Artwork)
    }

    public void setUpdateDate(Date updateDate){
        this.updateDate = updateDate;
    }


    public void save(){
        //TODO save Artwork
    }


    public void print(){
        //TODO print Image(Artwork)
    }

}
