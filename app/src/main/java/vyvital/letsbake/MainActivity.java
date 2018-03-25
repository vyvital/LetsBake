package vyvital.letsbake;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import vyvital.letsbake.fragments.RecipeFrag;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (savedInstanceState == null) {
            getSupportActionBar().setTitle("Let's Bake");
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, RecipeFrag.newInstance(), "frag")
                    .commit();
        } else {
            getSupportFragmentManager()
                    .findFragmentByTag("frag");
        }


    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}
