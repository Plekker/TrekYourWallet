package com.example.flow.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PersonDto {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "api_key")
    public String apiKey;

    public PersonDto(String apiKey) {
        this.apiKey = apiKey;
    }
}
