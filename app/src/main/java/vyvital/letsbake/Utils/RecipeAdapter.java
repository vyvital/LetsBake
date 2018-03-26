package vyvital.letsbake.Utils;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.test.espresso.idling.CountingIdlingResource;
import java.util.List;

import vyvital.letsbake.MainActivity;
import vyvital.letsbake.R;
import vyvital.letsbake.data.Recipe;
import vyvital.letsbake.fragments.EmptyFrag;
import vyvital.letsbake.fragments.RecipeFrag;
import vyvital.letsbake.fragments.StepFrag;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {
    public static final String RECIPE_KEY = "RECIPE_KEY";
    public static final String TAG = RecipeFrag.class.getSimpleName();
    private Context mContext;
    private List<Recipe> recipes;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CardView card;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.recipe_name);
            card = view.findViewById(R.id.card_view);
        }
    }


    public RecipeAdapter(Context mContext, List<Recipe> recipes) {
        this.mContext = mContext;
        this.recipes = recipes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Recipe recipe = recipes.get(position);
        holder.name.setText(recipe.getName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!MainActivity.isTablet(mContext)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RECIPE_KEY, recipes.get(position));
                    StepFrag fragment = StepFrag.newInstance();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(TAG)
                            .replace(R.id.content, fragment)
                            .commit();

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RECIPE_KEY, recipes.get(position));
                    StepFrag fragment = StepFrag.newInstance();
                    EmptyFrag empty = EmptyFrag.newInstance();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentL, fragment)
                            .replace(R.id.contentR, empty)
                            .addToBackStack(null)
                            .commit();

                }

            }
        });

        switch (position) {
            case 0:
                holder.card.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.green, null));
                break;
            case 1:
                holder.card.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.purple, null));
                break;
            case 2:
                holder.card.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.orange, null));
                break;
            case 3:
                holder.card.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.pink, null));
                break;
            default:
                holder.card.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.red, null));
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }



}
