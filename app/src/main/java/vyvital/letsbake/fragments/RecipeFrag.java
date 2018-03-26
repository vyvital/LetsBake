package vyvital.letsbake.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vyvital.letsbake.MainActivity;
import vyvital.letsbake.R;
import vyvital.letsbake.Utils.ApiService;
import vyvital.letsbake.Utils.RecipeAdapter;
import vyvital.letsbake.data.Recipe;

import static vyvital.letsbake.Utils.RecipeAdapter.TAG;


public class RecipeFrag extends Fragment {

    Call<List<Recipe>> call;

    RecyclerView recipeRV;
    private static Retrofit retrofit = null;
    public static final String URL = "https://d17h27t6h515a5.cloudfront.net";

    public RecipeFrag() {

    }

    public static RecipeFrag newInstance() {
        return new RecipeFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((MainActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).setActionBarTitle("Let's Bake");
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        recipeRV = view.findViewById(R.id.recipeRV);
        recipeRV.setHasFixedSize(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            recipeRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else recipeRV.setLayoutManager(new LinearLayoutManager(getContext()));

        if (testNetwork()) {
            connect();
        }

        return view;
    }

    private void connect() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final ApiService service = retrofit.create(ApiService.class);
        call = service.getJson();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();

                recipeRV.setAdapter(new RecipeAdapter(getActivity(), recipes));

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean testNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return ((networkInfo != null) && (networkInfo.isConnected()));
    }


}
