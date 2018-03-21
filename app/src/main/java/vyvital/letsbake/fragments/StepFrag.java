package vyvital.letsbake.fragments;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vyvital.letsbake.IngWidgetProvider;
import vyvital.letsbake.MainActivity;
import vyvital.letsbake.R;
import vyvital.letsbake.Utils.RecipeAdapter;
import vyvital.letsbake.Utils.StepsAdapter;
import vyvital.letsbake.data.Ingredients;
import vyvital.letsbake.data.Recipe;
import vyvital.letsbake.data.Steps;


public class StepFrag extends Fragment {
    public static final String TAG = StepFrag.class.getSimpleName();
    public static final String PIE = "https://www.manusmenu.com/wp-content/uploads/2013/07/1-Nutella-Tarts-1-1-of-1.jpg";
    public static final String BROWNIE = "http://cdn.skim.gs/images/vmnyvwn0wrjpms0ds4m5/fudgy-brownie-recipe";
    public static final String YELLOW = "https://i.pinimg.com/originals/51/99/44/519944f37f3f8f436cda3c2859369c91.jpg";
    public static final String CHEESE = "http://assets.kraftfoods.com/recipe_images/opendeploy/Philadelphia-New-York-Cheesecake-1366_640x428.jpg";
    public static final String TEMP = "https://www.tastefullysimple.com/_/media/images/recipe-default-image.png";
    private Recipe recipe;
    private TextView ing_list;
    private String recipe_ing_list;
    private List<Ingredients> ingredients;
    private List<Steps> steps;
    private RecyclerView stepsRV;

    public StepFrag() {
    }

    public static StepFrag newInstance() {
        return new StepFrag();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_fragment, container, false);

        if (getArguments() != null) recipe = getArguments().getParcelable(RecipeAdapter.RECIPE_KEY);
        if (recipe != null) {
            initialize(view);
            ((MainActivity) getActivity()).setActionBarTitle(recipe.getName());

        }

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initialize(View view) {
        FloatingActionButton share = view.findViewById(R.id.share);
        FloatingActionButton call = view.findViewById(R.id.call);
        stepsRV = view.findViewById(R.id.stepsID);
        stepsRV.setHasFixedSize(true);
        stepsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView img = view.findViewById(R.id.recipe_img);
        ImageView ing = view.findViewById(R.id.ingridients);
        ingredients = new ArrayList<Ingredients>();
        ingredients = recipe.getIngredientsList();
        steps = new ArrayList<Steps>();
        steps = recipe.getStepsList();
        recipe_ing_list = ingredientsStringBuild(ingredients);
        stepsRV.setAdapter(new StepsAdapter(getActivity(), steps));
        String photo_url;
        switch (recipe.getId()) {
            case "1":
                photo_url = PIE;
                break;
            case "2":
                photo_url = BROWNIE;
                break;
            case "3":
                photo_url = YELLOW;
                break;
            case "4":
                photo_url = CHEESE;
                break;
            default:
                photo_url = TEMP;
                break;
        }
        Picasso.with(getActivity()).load(photo_url).noFade().into(img);
        ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.ing_dialog, null);
                dialog.setContentView(dialog_view);
                ing_list = dialog_view.findViewById(R.id.ingredients_list);
                ing_list.setText(recipe_ing_list);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                dialog.show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey! Just wanted to share with you this amazing baking app! Link:############## ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Let's Bake");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(Intent.ACTION_DIAL);
                startActivity(Intent.createChooser(intentt,"Invide some friends to share your meal :)"));
            }
        });
    }

    public String ingredientsStringBuild(List<Ingredients> ingredients) {
        StringBuilder builder = new StringBuilder();
        for (Ingredients ing : ingredients) {
            builder.append(ing.getQuantity())
                    .append(" ")
                    .append(ing.getMeasure())
                    .append(" ")
                    .append("-")
                    .append(" ")
                    .append(ing.getIngredient())
                    .append("\n");
        }
        return builder.toString();

    }


}
