package com.example.foodrecipeapp;

import android.content.Context;
import android.location.GnssAntennaInfo;

import com.example.foodrecipeapp.Listeners.RandomRecipeResponseListener;
import com.example.foodrecipeapp.Listeners.RecipeDetailsListener;
import com.example.foodrecipeapp.Models.RandomRecipeApiResponse;
import com.example.foodrecipeapp.Models.RecipeDetailsResponse;
import com.example.foodrecipeapp.Models.SimilarRecipeResponse;
import com.example.foodrecipeapp.Models.SimilarRecipesListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {

    Context context;
    Retrofit retrofit = new Retrofit.Builder()              //call api
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags){

        CallRandomRecipes callRandomRecipes =  retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "10", tags);
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {

                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }

                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {

                listener.didError(t.getMessage());
            }
        });

    }

    public void getRecipeDetails(RecipeDetailsListener listener, int id){  //pass Details the RecipeDetailsListener interface

        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call    =  callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }

                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });

    }

    public void getSimilarRecipes(SimilarRecipesListener listener, int id){  //method for that interface
         CallSimilarRecipes callSimilarRecipes = retrofit.create(CallSimilarRecipes.class);
         Call<List<SimilarRecipeResponse>> call = callSimilarRecipes.callSimilarRecipe(id,"4", context.getString(R.string.api_key));
         call.enqueue(new Callback<List<SimilarRecipeResponse>>() {
             @Override
             public void onResponse(Call<List<SimilarRecipeResponse>> call, Response<List<SimilarRecipeResponse>> response) {
                 if (!response.isSuccessful()){
                     listener.didError(response.message());
                     return;
                 }
                 listener.didFetch(response.body(), response.message());
             }

             @Override
             public void onFailure(Call<List<SimilarRecipeResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
             }
         });
    }




    private interface CallRandomRecipes{
        @GET("recipes/random")

        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags")List<String> tags
        );

    }

        private interface CallRecipeDetails{      //get recipeDetails in api and call recipe interface
           @GET("recipes/{id}/information")       // recipeDetails
            Call<RecipeDetailsResponse> callRecipeDetails(  //method
                    @Path("id") int id,
                    @Query("apiKey") String apiKey

            );

        }

        private interface CallSimilarRecipes{         //interface
            @GET("recipes/{id}/similar")
            Call<List<SimilarRecipeResponse>> callSimilarRecipe(
                    @Path("id") int id,
                    @Query("number") String number,
                    @Query("apiKey") String apiKey


            );

        }

}
