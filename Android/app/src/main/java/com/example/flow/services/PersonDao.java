package com.example.flow.services;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.flow.classes.PersonDto;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM persondto")
    List<PersonDto> getAll();

    @Insert
    void insertAll(PersonDto... persons);

    @Delete
    void delete(PersonDto person);
}
