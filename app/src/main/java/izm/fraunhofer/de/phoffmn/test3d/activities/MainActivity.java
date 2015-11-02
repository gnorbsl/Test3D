package izm.fraunhofer.de.phoffmn.test3d.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import hollowsoft.slidingdrawer.SlidingDrawer;
import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ContactDialogFragment;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ContactDrawerFragment;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ContactListFragment;
import izm.fraunhofer.de.phoffmn.test3d.helper.Tools;
import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

public class MainActivity extends RendererActivity implements ContactDrawerFragment.ContactFragmentInterface{

    //TODO how to convert 3d model:
    //1. convert model from vrml1 to vrml2 with vrml1tovrml2
    //2. import model in meshlab and export faces textures to vertex texture
    //3. if this doesnt work: try stuff until it works
    //4. ???
    //5. profit

    private static final String TAG = "MainActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private float touchedX, touchedY;
    private float lastplanePosition, planePosition;
    private float maxScal = 2f, minScal = .1f;
    private float totalRatio = .9f;
    private int counter = 0;
    private Object3dContainer bondModel;

    //empty holder must be used to center the model
    private Object3dContainer holder;
    private float initHolderScaleX;
    private float initHolderScaleY;
    private float initHolderScaleZ;
    private float initHolderRotationX;
    private float initHolderRotationY;
    private float initHolderPositionX;
    private float initHolderPositionY;

    private Dialog dialog;
    private TextView message;
    private Handler handler;

    private ArrayList<String> logDummyLines;


    SharedPreferences pref;

    //StatisticsDrawer
    @Bind(R.id.drawer) SlidingDrawer drawer;
    @Bind(R.id.handle) ImageView handle;



    //MainLayoutViews
    @Bind(R.id.content) LinearLayout contentView;

    @Bind(R.id.logo) ImageView logo;
    @Bind(R.id.ninepatch) ImageView ninepatch;

    @Bind(R.id.showdata) Button showData;
    @Bind(R.id.deleteData) Button deleteData;

    //Navigationviews
    @Bind(R.id.navigation) LinearLayout navigation;

    @Bind(R.id.top) ImageButton top;
    @Bind(R.id.left) ImageButton left;
    @Bind(R.id.bottom) ImageButton bottom;
    @Bind(R.id.right) ImageButton right;
    @Bind(R.id.resetView) Button resetButton;

    private ContactDrawerFragment contactFragment;


    @Override
    public void initScene() {

        createBondModel();
        addSceneLights();

        holder.addChild(bondModel);
        scene.addChild(holder);

    }

    private void addSceneLights() {

        Light myLight = new Light();
        myLight.position.setZ(150);

        scene.lights().add(myLight);
    }

    /**
     * creates the BondModel and also adds a loading dialog while loading the model
     */
    private void createBondModel() {

        IParser parser = Parser.createParser(Parser.Type.OBJ, getResources(),
                "izm.fraunhofer.de.phoffmn.test3d:raw/blubba_obj", true);

        createDummyLoadingText();
        createAndStartLoadingAlertDialog();

        //in case something goes wrong to avoid that the user get stuck on the loading dialog
        try {
            parser.parse();
        } catch (Exception e) {
            removeLoadingAlertDialog();
        }

        removeLoadingAlertDialog();

        scene.backgroundColor().setAll(getResources().getColor(R.color.background_material_light));

        bondModel = parser.getParsedObject();
        bondModel.colorMaterialEnabled(true);

        positionModelInWorld();

        saveInitialValuesOfModel();

    }

    private void positionModelInWorld() {

        holder.scale().x = .75f;
        holder.scale().y = .75f;
        holder.scale().z = .75f;

        bondModel.position().x = -2.7f;
        bondModel.position().y = -1.2f;
        bondModel.position().z = 0;

        bondModel.rotation().y = 90;
        bondModel.rotation().x = 180;
    }

    private void removeLoadingAlertDialog() {

        this.runOnUiThread(() -> {

            dialog.cancel();
            handler.removeCallbacksAndMessages(null);
            drawer.setVisibility(View.VISIBLE);
            handle.setVisibility(View.VISIBLE);
            navigation.setVisibility(View.VISIBLE);
            logo.setVisibility(View.VISIBLE);
            ninepatch.setVisibility(View.VISIBLE);

            contactFragment = (ContactDrawerFragment) getFragmentManager().findFragmentById(R.id.contactinfofragment);
            if (null != contactFragment && contactFragment.isInLayout()) {
                contactFragment.showDrawer();
            }


        });



    }


    /**
     * Creates the LoadingDialog which simulates a calculation, must run from UI thread
     */
    private void createAndStartLoadingAlertDialog() {

        this.runOnUiThread(new Runnable() {

            public void run() {

                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_loading);

                message = (TextView) dialog.findViewById(R.id.message);
                message.setMovementMethod(new ScrollingMovementMethod());

                handler = new Handler();

                final Runnable r = new Runnable() {
                    public void run() {
                        message.append(logDummyLines.get(counter++) + "\n");
                        handler.postDelayed(this, (long) ((Math.random() * 500) + 300));
                    }
                };

                handler.postDelayed(r, 500);
                dialog.setCancelable(false);
                dialog.show();

            }
        });
    }

    /**
     * Loads a textfile (logdummy.txt) from ressources and adds the lines to an ArrayList
     * these lines are used to simulate the calculating while loading a model
     */
    private void createDummyLoadingText() {

        InputStream fileIn = getResources().openRawResource(getResources().getIdentifier(
                "raw/logdummy", "raw", getPackageName()));

        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(fileIn));

        String line;

        logDummyLines = new ArrayList<>();

        try {
            while ((line = buffer.readLine()) != null) {
                logDummyLines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * store first postion and scale values of model to be able to reset the view
     */
    private void saveInitialValuesOfModel() {

        initHolderScaleX = holder.scale().x;
        initHolderScaleY = holder.scale().y;
        initHolderScaleZ = holder.scale().z;

        initHolderRotationX = holder.rotation().x;
        initHolderRotationY = holder.rotation().y;

        initHolderPositionX = bondModel.position().x;
        initHolderPositionY = bondModel.position().y;


    }

    @Override
    protected void onCreateSetContentView() {
        super.onCreateSetContentView();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pref = Tools.getPreferences(MainActivity.this);

        contentView.addView(_glSurfaceView);

        holder = new Object3dContainer();
        create3DNavigation(contentView);

        createViewsAndListeners();

    }

    private void create3DNavigation(View contentView) {

        contentView.setOnTouchListener((v, event) -> {

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    touchedX = event.getX();
                    touchedY = event.getY();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (event.getPointerCount() == 2) {

                        lastplanePosition = spacing(event);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    switch (event.getPointerCount()) {

                        case 1:
                            holder.rotation().y -= (touchedX - event.getX()) / 2f;
                            holder.rotation().x -= (touchedY - event.getY()) / 2f;
                            touchedX = event.getX();
                            touchedY = event.getY();
                            break;

                        case 2:
                            planePosition = spacing(event);
                            float scaledRatio = planePosition / lastplanePosition;
                            totalRatio = totalRatio * scaledRatio;

                            if (totalRatio > maxScal) totalRatio = maxScal;
                            if (totalRatio < minScal) totalRatio = minScal;

                            holder.scale().x = holder.scale().y = holder.scale().z = totalRatio;
                            lastplanePosition = planePosition;
                            break;
                    }
                    break;
            }
            return true;
        });
    }

    private void createViewsAndListeners() {

        //createContactDrawer();

        createStatisticDrawer();

        createNavigation();

        logo.setOnClickListener(v -> finish());

        //TODO hide them somehow
        showData.setOnClickListener(v -> {

            Map<String, ?> prefEntries = pref.getAll();


            String[] contacts = new String[prefEntries.size()];

            int i = 0;
            for (Map.Entry<String, ?> entry : prefEntries.entrySet()) {

                contacts[i] = entry.getValue().toString();
                i++;
            }

            DialogFragment newFragment = ContactListFragment.newInstance(contacts);
            newFragment.show(createFragmentTransaction("list_dialog"), "list_dialog");


        });


        deleteData.setOnClickListener(v -> {

            pref.edit().clear().apply();

            deleteImagesInDirectory();
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

    private void deleteImagesInDirectory() {

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BusinessCards");

        if (storageDir.isDirectory()) {

            String[] files = storageDir.list();
            for (String fileName : files) {
                new File(storageDir, fileName).delete();
            }
        }
    }

    private void createStatisticDrawer() {



        drawer.setOnDrawerOpenListener(() -> handle.setImageDrawable(getResources().getDrawable(R.drawable.statshide)));

        drawer.setOnDrawerCloseListener(() -> handle.setImageDrawable(getResources().getDrawable(R.drawable.stats)));
    }


    private void createNavigation() {

        top.setOnClickListener(v -> bondModel.position().y -= 0.3f);
        right.setOnClickListener(v -> bondModel.position().x -= 0.3f);
        bottom.setOnClickListener(v -> bondModel.position().y += 0.3f);
        left.setOnClickListener(v -> bondModel.position().x += 0.3f);

        resetButton.setOnClickListener(v -> resetModelPosition());
    }

    public void createDialogFragment(int kind_of_dialog) {

        DialogFragment newFragment = ContactDialogFragment.newInstance(kind_of_dialog);
        newFragment.show(createFragmentTransaction("dialog"), "dialog");

    }

    private void resetModelPosition() {

        holder.scale().x = initHolderScaleX;
        holder.scale().y = initHolderScaleY;
        holder.scale().z = initHolderScaleZ;

        bondModel.position().x = initHolderPositionX;
        bondModel.position().y = initHolderPositionY;

        holder.rotation().x = initHolderRotationX;
        holder.rotation().y = initHolderRotationY;

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

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
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

}
