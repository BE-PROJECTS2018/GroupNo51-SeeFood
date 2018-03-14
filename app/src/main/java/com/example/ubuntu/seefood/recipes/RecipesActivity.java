package com.example.ubuntu.seefood.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ubuntu.seefood.R;

import java.util.StringTokenizer;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
    }

    public void onClickShowResults(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("param", "Sample Call");
        Intent intent = new Intent(RecipesActivity.this, RecipeResultsActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public void onClickSearchYummlyApi(View view) {

        String url = "recipes?_app_id=94931240&_app_key=e33572938a728cc2e9a831c955c6fbad";
        StringBuilder params = new StringBuilder(url);
        params.append("&");

        CheckBox dairy = findViewById(R.id.dairy_checkbox);
        CheckBox egg = findViewById(R.id.egg_checkbox);
        CheckBox gluten = findViewById(R.id.gluten_checkbox);
        CheckBox peanut = findViewById(R.id.peanut_checkbox);
        CheckBox seafood = findViewById(R.id.seafood_checkbox);
        CheckBox sesame = findViewById(R.id.sesame_checkbox);
        CheckBox soy = findViewById(R.id.soy_checkbox);
        CheckBox tree_nut = findViewById(R.id.tree_nut_checkbox);
        CheckBox wheat = findViewById(R.id.wheat_checkbox);
        CheckBox lacto_veg = findViewById(R.id.lacto_vegetarian_checkbox);
        CheckBox ovo_veg = findViewById(R.id.ovo_vegetarian_checkbox);
        CheckBox pescetarian = findViewById(R.id.pescetarian_checkbox);
        CheckBox vegan = findViewById(R.id.vegan_checkbox);
        CheckBox vegetarian = findViewById(R.id.vegetarain_checkbox);
        CheckBox american = findViewById(R.id.American_checkbox);
        CheckBox italian = findViewById(R.id.Italian_checkbox);
        CheckBox mexican = findViewById(R.id.Mexican_checkbox);
//        CheckBox south_soul = (CheckBox)findViewById(R.id.souther_soult_checkbox);
//        CheckBox french = (CheckBox)findViewById(R.id.French_checkbox);
//        CheckBox southwestern = (CheckBox)findViewById(R.id.Southwestern_checkbox);
//        CheckBox barbeque = (CheckBox)findViewById(R.id.Barbecue_checkbox);
        CheckBox indian = findViewById(R.id.Indian_checkbox);
//        CheckBox chinese = (CheckBox)findViewById(R.id.Chinese_checkbox);
//        CheckBox cajun = (CheckBox)findViewById(R.id.cajun_creole_checkbox);
//        CheckBox english = (CheckBox)findViewById(R.id.English_checkbox);
//        CheckBox meditarian = (CheckBox)findViewById(R.id.Mediterranean_checkbox);
//        CheckBox greek = (CheckBox)findViewById(R.id.Greek_checkbox);
//        CheckBox spanish = (CheckBox)findViewById(R.id.Spanish_checkbox);
//        CheckBox german = (CheckBox)findViewById(R.id.German_checkbox);
//        CheckBox thai = (CheckBox)findViewById(R.id.Thai_checkbox);
//        CheckBox morocon = (CheckBox)findViewById(R.id.Moroccan_checkbox);
//        CheckBox irish = (CheckBox)findViewById(R.id.Irish_checkbox);
//        CheckBox japanese = (CheckBox)findViewById(R.id.Japanese_checkbox);
//        CheckBox cuban = (CheckBox)findViewById(R.id.Cuban_checkbox);
//        CheckBox hawai = (CheckBox)findViewById(R.id.Hawaiin_checkbox);
//        CheckBox swedish = (CheckBox)findViewById(R.id.Swedish_checkbox);
//        CheckBox hungarian = (CheckBox)findViewById(R.id.Hungarian_checkbox);
//        CheckBox portugese = (CheckBox)findViewById(R.id.Portugese_checkbox);
        CheckBox main_dish = findViewById(R.id.main_dishes_checkbox);
        CheckBox desert = findViewById(R.id.Desserts_checkbox);
        CheckBox side = findViewById(R.id.side_checkbox);
//        CheckBox lunch_snack = (CheckBox)findViewById(R.id.lunch_snack_checkbox);
//        CheckBox appetizer = (CheckBox)findViewById(R.id.Appetizers_checkbox);
//        CheckBox salad = (CheckBox)findViewById(R.id.Salads);
//        CheckBox bread = (CheckBox)findViewById(R.id.Breads_checkbox);
//        CheckBox breakfast = (CheckBox)findViewById(R.id.breakfast_brunch_checkbox);
//        CheckBox Soup = (CheckBox)findViewById(R.id.Soups_checkbox);
////        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);
//        CheckBox dairy = (CheckBox)findViewById(R.id.dairy_checkbox);

        EditText ingridients = findViewById(R.id.allowedIngridientList);
        EditText totalTime = findViewById(R.id.total_time);

        String ings = ingridients.getText().toString();
        String tottime = totalTime.getText().toString();

        if (ings != null) {
            StringTokenizer tokenizer = new StringTokenizer(ings, ",");
            while (tokenizer.hasMoreElements()) {
                params.append("allowedIngredient[]=" + tokenizer.nextToken() + "&");
            }
        }

        if (!tottime.equals("")) {
            params.append("maxTotalTimeInSeconds=" + tottime + "&");
        }

        if (dairy.isChecked()) {
            params.append("allowedAllergy[]=" + dairy.getText() + "&");
        }
        if (egg.isChecked()) {
            params.append("allowedAllergy[]=" + egg.getText() + "&");
        }
        if (gluten.isChecked()) {
            params.append("allowedAllergy[]=" + gluten.getText() + "&");
        }
        if (peanut.isChecked()) {
            params.append("allowedAllergy[]=" + peanut.getText() + "&");
        }
        if (seafood.isChecked()) {
            params.append("allowedAllergy[]=" + seafood.getText() + "&");
        }
        if (sesame.isChecked()) {
            params.append("allowedAllergy[]=" + sesame.getText() + "&");
        }
        if (soy.isChecked()) {
            params.append("allowedAllergy[]=" + soy.getText() + "&");
        }
        if (tree_nut.isChecked()) {
            params.append("allowedAllergy[]=" + tree_nut.getText() + "&");
        }
        if (wheat.isChecked()) {
            params.append("allowedAllergy[]=" + wheat.getText() + "&");
        }
        if (lacto_veg.isChecked()) {
            params.append("allowedDiet[]=" + lacto_veg.getText() + "&");
        }
        if (ovo_veg.isChecked()) {
            params.append("allowedDiet[]=" + ovo_veg.getText() + "&");
        }
        if (pescetarian.isChecked()) {
            params.append("allowedDiet[]=" + pescetarian.getText() + "&");
        }
        if (vegan.isChecked()) {
            params.append("allowedDiet[]=" + vegan.getText() + "&");
        }
        if (vegetarian.isChecked()) {
            params.append("allowedDiet[]=" + vegetarian.getText() + "&");
        }
        if (american.isChecked()) {
            params.append("allowedCuisine[]=" + american.getText() + "&");
        }
        if (italian.isChecked()) {
            params.append("allowedCuisine[]=" + italian.getText() + "&");
        }
        if (mexican.isChecked()) {
            params.append("allowedCuisine[]=" + mexican.getText() + "&");
        }
        if (indian.isChecked()) {
            params.append("allowedCuisine[]=" + indian.getText() + "&");
        }
        if (main_dish.isChecked()) {
            params.append("allowedCourse[]=" + main_dish.getText() + "&");
        }
        if (desert.isChecked()) {
            params.append("allowedCourse[]=" + desert.getText() + "&");
        }
        if (side.isChecked()) {
            params.append("allowedCourse[]=" + side.getText() + "&");
        }

        params.deleteCharAt(params.length() - 1);

        Toast.makeText(this, "" + params, Toast.LENGTH_LONG).show();

        Bundle bundle = new Bundle();
        bundle.putString("params", params.toString());
        Intent intent = new Intent(RecipesActivity.this, YummlyResultsActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);


    }
}
