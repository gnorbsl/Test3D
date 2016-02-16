package izm.fraunhofer.de.phoffmn.test3d.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wunderlist.slidinglayer.SlidingLayer;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ContactDialogFragment;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ContactDrawerFragment;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ModelFragment;
import izm.fraunhofer.de.phoffmn.test3d.fragments.StatisticsDrawerFragment;
import izm.fraunhofer.de.phoffmn.test3d.helper.Tools;

public class MainActivity extends AppCompatActivity implements ContactDrawerFragment.ContactFragmentInterface, StatisticsDrawerFragment.OnFragmentInteractionListener {

    //TODO how to convert 3d model:
    //1. convert model from vrml1 to vrml2 with vrml1tovrml2
    //2. import model in meshlab and export faces textures to vertex texture
    //3. if this doesnt work: try stuff until it works
    //4. ???
    //5. profit

    private static final String TAG = "MainActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final String CONTENT_FRAGMENT = "content_fragment";

    SharedPreferences pref;

    @Bind(R.id.slidingLayer1)
    SlidingLayer statDrawer;

    @Bind(R.id.showStats)
    Button showStats;
    @Bind(R.id.hideStats)
    Button hideStats;


    @Bind(R.id.logo) ImageView logo;
    @Bind(R.id.ninepatch) ImageView ninepatch;

    @Bind(R.id.showdata) Button showData;
    @Bind(R.id.deleteData) Button deleteData;

    private ContactDrawerFragment contactFragment;
    private StatisticsDrawerFragment statisticsFragment;

    @Bind(R.id.tambient_data)
    TextView tAmbientData;

    @Bind(R.id.main_layout)
    View mainView;

    private void removeLoadingAlertDialog() {

        this.runOnUiThread(() -> {

            logo.setVisibility(View.VISIBLE);
            ninepatch.setVisibility(View.VISIBLE);

            contactFragment = (ContactDrawerFragment) getFragmentManager().findFragmentById(R.id.contactinfofragment);
            if (null != contactFragment && contactFragment.isInLayout()) {
                contactFragment.showDrawer();
            }


        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //TODO add new instance stuff
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        try {
            final Fragment fragment = ModelFragment.newInstance(getIntent().getExtras().getString(StartActivity.BOND_DIAMETER));

            if (getFragmentManager().findFragmentByTag(CONTENT_FRAGMENT) != null)
                transaction.addToBackStack(null);

            transaction.replace(R.id.contentview, fragment, CONTENT_FRAGMENT);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }


        getSupportActionBar().hide();
        pref = Tools.getPreferences(MainActivity.this);

        removeLoadingAlertDialog();

        createViewsAndListeners();

        tAmbientData.setText(getIntent().getExtras().getString(StartActivity.T_AMBIENT));


    }


    private void createViewsAndListeners() {


        showStats.setOnClickListener(v -> statDrawer.openLayer(true));

        hideStats.setOnClickListener(v -> statDrawer.closeLayer(true));

        statDrawer.setOnInteractListener(new SlidingLayer.OnInteractListener() {
            @Override
            public void onOpen() {
                hideStats.setVisibility(View.VISIBLE);

            }

            @Override
            public void onShowPreview() {

            }

            @Override
            public void onClose() {
                showStats.setVisibility(View.VISIBLE);

            }

            @Override
            public void onOpened() {
                showStats.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onPreviewShowed() {

            }

            @Override
            public void onClosed() {
                hideStats.setVisibility(View.INVISIBLE);

            }
        });
    }

    private FragmentTransaction createFragmentTransaction(String fragmentName) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(fragmentName);
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        return ft;
    }






    public void createDialogFragment(int kind_of_dialog) {

        DialogFragment newFragment = ContactDialogFragment.newInstance(kind_of_dialog);
        newFragment.setCancelable(false);
        newFragment.show(createFragmentTransaction("dialog"), "dialog");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * starts the camera app and provides a pre defined file to store the pictuure
     */
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Tools.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //TODO show error
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                Log.d(TAG, "IMAGE CREATED INTENT" + Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    /**
     * will be called after the picture was taken or cancelled, picture will be shown in an alertdialog
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                createDialogFragment(ContactDialogFragment.ADD_WITH_IMAGE);
            }

        }
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back pressed");

        finish();
        contactFragment.closeDrawer();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mainView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
    
}
