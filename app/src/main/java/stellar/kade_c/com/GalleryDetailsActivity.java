package stellar.kade_c.com;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import stellar.kade_c.com.stellar.R;

/**
 * Called when we need to display a picture in a bigger view.
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