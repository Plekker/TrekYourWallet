package com.example.flow.services;

public class Empty {
    public static boolean isImputNotEmpty(String test){
        if(test != null){
            if(!test.isEmpty()){
                return true;
            }
        }

        return false;
    }
}
