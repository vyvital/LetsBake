package vyvital.letsbake.Utils;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vyvital.letsbake.MainActivity;
import vyvital.letsbake.R;
import vyvital.letsbake.data.Recipe;
import vyvital.letsbake.data.Steps;
import vyvital.letsbake.fragments.DetailFrag;
import vyvital.letsbake.fragments.StepFrag;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {
    public static final String STEP_LIST = "STEP_LIST";
    public static final String STEP_KEY = "STEP_KEY";
    public static final String STEP_SIZE = "STEP_SIZE";
    public static final String TAG = StepFrag.class.getSimpleName();
    private Context mContext;
    private List<Steps> steps;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shortDesc;


        public MyViewHolder(View view) {
            super(view);
            shortDesc = view.findViewById(R.id.tv_parentName);
        }
    }

    public StepsAdapter(Context mContext, List<Steps> steps) {
        this.mContext = mContext;
        this.steps = steps;
    }

    @NonNull
    @Override

    public StepsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Steps step = steps.get(position);
        holder.shortDesc.setText(step.getShortDescription());
        holder.shortDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Recipe recipe = new Recipe(null, null, null, null, null, steps);
                Bundle bundle = new Bundle();
                bundle.putParcelable(STEP_LIST, recipe);
                bundle.putParcelable(STEP_KEY, step);
                bundle.putInt(STEP_SIZE, steps.size());
                if (!MainActivity.isTablet(mContext)) {
                    DetailFrag detailFrag = DetailFrag.newInstance();
                    detailFrag.setArguments(bundle);
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content, detailFrag)
                            .addToBackStack(TAG)
                            .commit();
                } else {
                    DetailFrag detailFrag = DetailFrag.newInstance();
                    detailFrag.setArguments(bundle);
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentR, detailFrag)
                            .addToBackStack(TAG)
                            .commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
}
