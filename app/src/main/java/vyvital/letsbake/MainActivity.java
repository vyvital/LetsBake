package vyvital.letsbake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import vyvital.letsbake.fragments.RecipeFrag;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
