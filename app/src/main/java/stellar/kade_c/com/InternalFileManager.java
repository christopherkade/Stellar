package stellar.kade_c.com;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Handles methods that deal with internal files.
 */
public class InternalFileManager {
    private Context context;

    public InternalFileManager(Context ctx) {
        context = ctx;
    }

    /**
     * Saves the picture displayed in the file system.
     */
    public void savePicture(String filename, Bitmap b, Context ctx){
        try {
            ObjectOutputStream oos;
            FileOutputStream out;// = new FileOutputStream(filename);
            out = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(out);
            b.compress(Bitmap.CompressFormat.PNG, 100, oos);

            oos.close();
            oos.notifyAll();
            out.notifyAll();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads picture from file system and returns it as a Bitmap.
     */
    public Bitmap loadPicture(String filename) {
        Bitmap b = null;

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(fis);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            b = BitmapFactory.decodeStream(ois);
            try {
                ois.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }
}
