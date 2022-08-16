package com.example.foodrecipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodrecipeapp.Adapters.IngredientsAdapter;
import com.example.foodrecipeapp.Adapters.SimilarRecipeAdapter;
import com.example.foodrecipeapp.Listeners.RecipeClickListner;
import com.example.foodrecipeapp.Listeners.RecipeDetailsListener;
import com.example.foodrecipeapp.Models.Ingredient;
import com.example.foodrecipeapp.Models.RecipeDetailsResponse;
import com.example.foodrecipeapp.Models.SimilarRecipeResponse;
import com.example.foodrecipeapp.Models.SimilarRecipesListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView textView_meal_name, textView_meal_source, textView_meal_summary;  //id in recipe details design
    ImageView imageView_meal_image;
    RecyclerView recyclerView_meal_ingredients, recycler_meal_similar;
    RequestManager manager; //call api
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        id =Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id); //call api
        manager.getSimilarRecipes(similarRecipesListener, id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details... "); //message
        dialog.show();
    }

    private void findViews() {
           textView_meal_name = findViewById(R.id.textView_meal_name);
           textView_meal_source= findViewById(R.id.textView_meal_source);
           textView_meal_summary   = findViewById(R.id.textView_meal_summary);
           imageView_meal_image    = findViewById(R.id.imageView_meal_image);
           recyclerView_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
           recycler_meal_similar         =  findViewById(R.id.recycler_meal_similar);

    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {  //interface Recipe Details
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
              dialog.dismiss();
              textView_meal_name.setText(response.title); //response
              textView_meal_source.setText(response.sourceName);
              textView_meal_summary.setText(response.summary);

              Picasso.get().load(response.image).into(imageView_meal_image);


              recyclerView_meal_ingredients.setHasFixedSize(true); //activity_recipe_details
              recyclerView_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL,false));

              ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
              recyclerView_meal_ingredients.setAdapter(  ingredientsAdapter );
        }

        @Override
        public void didError(String message) {

            Toast.makeText(RecipeDetailsActivity.this, "message", Toast.LENGTH_SHORT).show();

        }
    };

    private final SimilarRecipesListener similarRecipesListener =  new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> responses, String message) {
            recycler_meal_similar.setHasFixedSize(true);
            recycler_meal_similar.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL,false ));
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeDetailsActivity.this, responses, recipeClickListner);

            recycler_meal_similar.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this,"message", Toast.LENGTH_SHORT).show();
        }
    };

       private final RecipeClickListner recipeClickListner = new RecipeClickListner() {
           @Override
           public void onRecipeClicked(String id) {

               startActivity(new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class)
                       .putExtra("id",id));
           }
       };

}