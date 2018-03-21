package vyvital.letsbake.Utils;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vyvital.letsbake.data.Recipe;

public interface ApiService {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getJson();
}
