package vyvital.letsbake.fragments;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String favID;
    private Menu menu;
    private MenuItem itemz;
    private SharedPreferences mFav;

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
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((MainActivity) getActivity()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        ((MainActivity) getActivity()).getSupportActionBar().show();
        View view = inflater.inflate(R.layout.step_fragment, container, false);
        if (getArguments() != null) recipe = getArguments().getParcelable(RecipeAdapter.RECIPE_KEY);
        if (recipe != null) {
            initialize(view);
            ((MainActivity) getActivity()).setActionBarTitle(recipe.getName());
        }
        mFav = this.getActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        if (favID == null) {
            favID = mFav.getString("id", "");
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
        TextView serv = view.findViewById(R.id.servings);
        ImageView img = view.findViewById(R.id.recipe_img);
        ImageView ing = view.findViewById(R.id.ingridients);
        ingredients = new ArrayList<Ingredients>();
        ingredients = recipe.getIngredientsList();
        steps = new ArrayList<Steps>();
        steps = recipe.getStepsList();
        serv.setText(String.valueOf(recipe.getServings()));
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
                startActivity(Intent.createChooser(intentt, "Invite some friends to share your meal :)"));
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        itemz = menu.findItem(R.id.action_settings);
        if (favID.equals(recipe.getId())) {
            itemz.setIcon(R.drawable.ic_favorite_black_24dp);
        }
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mFav = this.getActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mFav.edit();
        if (id == R.id.action_settings) {
            if (item.getIcon().getConstantState() == getResources().getDrawable(R.drawable.ic_favorite_black_24dp).getConstantState()) {
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                editor.putString("id", "");
                editor.putString("name", "Pick a Recipe");
                editor.putString("ing", "");
                editor.apply();
                Toast.makeText(getContext(), "Removed Favorite Recipe", Toast.LENGTH_SHORT).show();

            } else {
                item.setIcon(R.drawable.ic_favorite_black_24dp);
                favID = recipe.getId();
                editor.putString("id", favID);
                editor.putString("name", recipe.getName());
                editor.putString("ing", recipe_ing_list);
                editor.apply();
                Toast.makeText(getContext(), "Marked as Favorite Recipe", Toast.LENGTH_SHORT).show();
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity().getApplicationContext());
            ComponentName componentName = new ComponentName(getActivity().getApplication(), IngWidgetProvider.class);
            int[] ids = appWidgetManager.getAppWidgetIds(componentName);
            if (ids.length > 0) {
                new IngWidgetProvider().onUpdate(getActivity(), appWidgetManager, ids);
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
