package stellar.kade_c.com.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import stellar.kade_c.com.APIRequests;
import stellar.kade_c.com.GalleryDetailsActivity;
import stellar.kade_c.com.InternalFileManager;
import stellar.kade_c.com.MainActivity;
import stellar.kade_c.com.stellar.R;


/**
 * Astronomy Picture of the Day Fragment.
 */
// TODO: Adapt picture image to all devices.
public class APD extends Fragment implements HomeViewPager.FragmentSwipeItf {

    private View view;
    private APDInfo apdInfo = new APDInfo();
    private int yearSelected;
    private int monthSelected;
    private int daySelected;
    private String date;

    /**
     * APD information
     */
    private class APDInfo {
        String date;
        String explanation;
        String url;
        String title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.apd, container, false);

        // Set current date by default.
        setDate(0, 0, 0, true);

        // Gets / Displays information.
        displayAPD();

        // Handles APD click.
        handleImageClick();

        // Handles the calendar.
        handleDateChange();

        return view;
    }

    /**
     * Handles the click of our displayed image.
     */
    // TODO: Save image when displayed, not when clicked.
    private void handleImageClick() {
        final ImageView apd = (ImageView) view.findViewById(R.id.apd_image);
        final TextView title_text = (TextView) view.findViewById(R.id.title_text);

        apd.setOnClickListener(new View.OnClickListener() {

            /**
             * Display a bigger image in anoter activity.
             */
            @Override
            public void onClick(View view) {
                String picture_title = title_text.getText().toString();

                // Save the image in internal files.
                saveImage();

                // Create intent.
                Intent intent = new Intent(getActivity(), GalleryDetailsActivity.class);
                intent.putExtra("filename", picture_title);

                // Start details activity.
                startActivity(intent);

                apd.destroyDrawingCache();
            }
        });
    }

    /**
     * Sets the date displayed on the DatePicker.
     */
    private void setDate(int year, int month, int day, boolean currentDate) {
        final Calendar c = Calendar.getInstance();

        if (currentDate) {
            yearSelected = c.get(Calendar.YEAR);
            monthSelected = c.get(Calendar.MONTH) + 1;
            daySelected = c.get(Calendar.DAY_OF_MONTH);
        } else {
            yearSelected = year;
            monthSelected = month;
            daySelected = day;
        }
        date = yearSelected + "-" + monthSelected + "-" + daySelected;
    }

    /**
     * Sets up the calendar listeners.
     * Catches the input of a new date and acts accordingly.
     */
    private void handleDateChange() {
        final ImageView calendar_image = (ImageView) view.findViewById(R.id.calendar_image);

        // Sets the calendar image size.
        calendar_image.getLayoutParams().width = 100;
        calendar_image.getLayoutParams().height = 100;

        // Listener for date selection.
        calendar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        // Save date set.
                        setDate(year, month, day, false);

                        displayAPD();
                    }
                }, yearSelected, monthSelected, daySelected);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    /**
     * Gets information to display and displays it.
     */
    private void displayAPD() {
        final APDRequest apdRequest = new APDRequest();

        try {
            // Get data with date selected.
            apdRequest.execute(date).get();

            // Display APD information.
            setAPDInfo();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets APD info on our view.
     */
    private void setAPDInfo() {
        // Get page Views
        final ImageView apd_image = (ImageView) view.findViewById(R.id.apd_image);
        final TextView title_text = (TextView) view.findViewById(R.id.title_text);
        final TextView explanation_text = (TextView) view.findViewById(R.id.explenation_text);
        final TextView date_text = (TextView) view.findViewById(R.id.date_text);

        // Sets the title.
        title_text.setText(apdInfo.title);

        // Sets the image.
        Glide.with(getContext()).load(apdInfo.url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(apd_image);

        // Sets the explanation text.
        explanation_text.setText(apdInfo.explanation);

        // Sets the date.
        date_text.setText(apdInfo.date);
    }

    /**
     * Calls method to save the image (Bitmap) in local files.
     */
    private void saveImage() {
        final ImageView apd_image = (ImageView) view.findViewById(R.id.apd_image);
        final InternalFileManager IFH = new InternalFileManager(getContext());
        final TextView titletv = (TextView) view.findViewById(R.id.title_text);

        apd_image.setDrawingCacheEnabled(true);
        apd_image.buildDrawingCache(true);
        Bitmap bitmap = apd_image.getDrawingCache();

        String title = titletv.getText().toString();
        IFH.savePicture(title, bitmap, getContext());
        ((MainActivity)getActivity()).addPicture(title);
    }

    @Override
    public void fragmentBecameVisible() {}

    /**
     * Handles the ADP API Request.
     */
    private class APDRequest extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... date) {
            APIRequests apiRequests = new APIRequests();
            JSONObject jsonObject;

            // Retrieves information from API.
            jsonObject = apiRequests.getAPD(date[0]);

            try {
                if (jsonObject != null) {
                    // Sets information retrieved.
                    apdInfo.date = (String) jsonObject.get("date");
                    apdInfo.explanation = (String) jsonObject.get("explanation");
                    apdInfo.url = (String) jsonObject.get("hdurl");
                    apdInfo.title = (String) jsonObject.get("title");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
