package com.example.flow.services;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.flow.classes.PersonDto;

@Database(entities = {PersonDto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
}
