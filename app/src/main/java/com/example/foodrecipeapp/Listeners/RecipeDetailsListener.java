package com.example.foodrecipeapp.Listeners;

import com.example.foodrecipeapp.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);

}
