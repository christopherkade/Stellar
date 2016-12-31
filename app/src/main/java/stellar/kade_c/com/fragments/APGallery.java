package stellar.kade_c.com.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import stellar.kade_c.com.GalleryDetailsActivity;
import stellar.kade_c.com.GalleryGridViewAdapter;
import stellar.kade_c.com.ImageItem;
import stellar.kade_c.com.InternalFileManager;
import stellar.kade_c.com.MainActivity;
import stellar.kade_c.com.stellar.R;


/**
 * Astronomy Pictures Gallery
 * Contains pictures already viewed by the user.
 */
// TODO: Conserve pictures viewed when app process is stopped.
public class APGallery extends Fragment implements HomeViewPager.FragmentSwipeItf {
    View view;
    private GridView gridView;
    private GalleryGridViewAdapter gridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.ap_gallery, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridAdapter = new GalleryGridViewAdapter(getContext(), R.layout.gallery_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                // Create intent.
                Intent intent = new Intent(getActivity(), GalleryDetailsActivity.class);

                // Pass the name of the file clicked.
                intent.putExtra("filename", item.getTitle());

                // Start details activity.
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Gets our list of saved files to display.
     * @return ArrayList containing the file names.
     */
    private ArrayList<ImageItem> getData() {
        InternalFileManager IFH = new InternalFileManager(getContext());
        ArrayList<String> pictures = ((MainActivity)getActivity()).getPictures();
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        for (String picture : pictures) {
            Bitmap bitmap = IFH.loadPicture(picture);
            imageItems.add(new ImageItem(bitmap, picture));
        }
        return imageItems;
    }

    /**
     * Update the picture list when swiped in.
     */
    @Override
    public void fragmentBecameVisible() {
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridAdapter = new GalleryGridViewAdapter(getContext(), R.layout.gallery_item_layout, getData());
        gridView.setAdapter(gridAdapter);
    }
}
