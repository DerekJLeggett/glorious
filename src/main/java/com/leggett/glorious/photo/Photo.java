package com.leggett.glorious.photo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	private String fileName;
    private Date dateTaken;

    protected Photo(){}

    public Photo(String fileName, Date dateTaken){
        this.fileName = fileName;
        this.dateTaken = dateTaken;
    }

    public Long getId(){
        return this.id;
    }

    public String getFileName(){
        return this.fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public Date getDateTaken(){
        return this.dateTaken;
    }

    public void setDateTaken(Date dateTaken){
        this.dateTaken = dateTaken;
    }

       @Override
    public String toString() {
      return "Photo{id=" + this.id + ", fileName='" + this.fileName + ", dateTaken='" + this.dateTaken + "'}";
    }
}

