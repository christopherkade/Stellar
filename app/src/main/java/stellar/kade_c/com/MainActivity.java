package stellar.kade_c.com;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import stellar.kade_c.com.fragments.HomeViewPager;
import stellar.kade_c.com.stellar.R;

public class MainActivity extends AppCompatActivity {

    // List of our pictures names.
    ArrayList<String> pictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        replaceFragment(new HomeViewPager());
    }

    /**
     * Replaces the current fragment and closes the Drawer.
     * @param fragment
     */
    public void replaceFragment(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void addPicture(String picture) {
        pictures.add(picture);
    }
}
