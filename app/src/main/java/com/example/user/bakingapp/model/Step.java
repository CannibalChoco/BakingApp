package com.example.user.bakingapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "step",
        foreignKeys = {@ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "parent_id")})
public class Step {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "parent_id")
    long parentId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(long id, long parentId, String shortDescription, String description,
                String videoURL, String thumbnailURL) {
        this.id = id;
        this.parentId = parentId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    @Ignore
    public Step(long parentId, String shortDescription, String description, String videoURL,
                String thumbnailURL) {
        this.parentId = parentId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public long getId() {
        return id;
    }

    public long getParentId() {
        return parentId;
    }

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

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }
}
