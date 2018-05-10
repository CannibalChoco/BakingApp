package com.example.user.bakingapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "step")
public class Step {

    @PrimaryKey(autoGenerate = true)
    private long id;
    //long recipeId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(long id, /*long recipeId,*/ String shortDescription, String description,
                String videoURL, String thumbnailURL) {
        this.id = id;
        //this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    @Ignore
    public Step(/*long recipeId,*/ String shortDescription, String description, String videoURL,
                String thumbnailURL) {
        //this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public long getId() {
        return id;
    }

//    public long getRecipeId() {
//        return recipeId;
//    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
