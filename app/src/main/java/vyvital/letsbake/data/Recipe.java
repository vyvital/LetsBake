package vyvital.letsbake.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements Parcelable {

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("servings")
    private String servings;

    @SerializedName("id")
    private String id;

    protected Recipe(Parcel in) {
        name = in.readString();
        servings = in.readString();
        imagePath = in.readString();
        id = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    @SerializedName("image")
    private String imagePath;

    @SerializedName("ingredients")
    private List<Ingredients> ingredientsList;

    @SerializedName("steps")
    private List<Steps> stepsList;

    public Recipe(String id, String name, String servings, String imagePath, List<Ingredients> ingredients, List<Steps> steps)
    {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.imagePath = imagePath;
        this.ingredientsList = ingredients;
        this.stepsList = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(servings);
        dest.writeString(imagePath);
        dest.writeString(id);
        dest.writeList(ingredientsList);
        dest.writeList(stepsList);
    }
}
