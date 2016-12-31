package stellar.kade_c.com;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import stellar.kade_c.com.stellar.R;

/**
 * Loads the image and displays it in the ImageView.
 */
public class GalleryDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_details_activity);

        final InternalFileManager IFH = new InternalFileManager(this);

        String filename = getIntent().getStringExtra("filename");
        Bitmap bitmap = IFH.loadPicture(filename);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
    }
}