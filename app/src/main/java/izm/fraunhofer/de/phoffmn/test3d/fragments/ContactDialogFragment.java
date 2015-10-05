package izm.fraunhofer.de.phoffmn.test3d.fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import izm.fraunhofer.de.phoffmn.test3d.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDialogFragment extends DialogFragment {

    public static final String KIND_OF_DIALOG = "kind_of_dialog";
    public static final int ADD_WITHOUT_IMAGE = 0;
    public static final int ADD_WITH_IMAGE = 1;
    public static final String SHARED_KEY = "FRAUNHOFER_IZM";
    private static final String TAG = "ContactDialogFragment";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private String mCurrentPhotoPath;
    private ImageView imageView;
    private RelativeLayout storeImageView;
    private View view;
    private EditText additionalInfoImage;
    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private EditText nameEditText;
    private EditText companyNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText additionalInfo;

    static public ContactDialogFragment newInstance(int value) {
        ContactDialogFragment f = new ContactDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(KIND_OF_DIALOG, value);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button saveContact = (Button) view.findViewById(R.id.savecontact);
        Button cancelDialog = (Button) view.findViewById(R.id.cancelcontact);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        int kind_of_dialog = getArguments().getInt(KIND_OF_DIALOG);

        final SharedPreferences pref = getActivity().getSharedPreferences(SHARED_KEY, Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = pref.edit();

        if (kind_of_dialog == ADD_WITH_IMAGE) {

            saveContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY).format(new Date());

                    String additionalInfo = additionalInfoImage.getText().toString();

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("timestamp", timeStamp)
                                .put("imagepath", mCurrentPhotoPath)
                                .put("addInfo", additionalInfo)
                                .put("checkbox1", checkbox1.isChecked())
                                .put("checkbox2", checkbox2.isChecked())
                                .put("checkbox3", checkbox3.isChecked());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    edit.putString(timeStamp, obj.toString());
                    edit.apply();

                    Toast.makeText(getActivity(), "SAVED IMAGE " + obj.toString(), Toast.LENGTH_LONG).show();
                    getDialog().cancel();

                }
            });
        } else if (kind_of_dialog == ADD_WITHOUT_IMAGE) {
            saveContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY).format(new Date());
                    JSONObject obj = new JSONObject();
                    try {

                        String name = nameEditText.getText().toString();
                        String company = companyNameEditText.getText().toString();
                        String email = emailEditText.getText().toString();
                        String phone = phoneEditText.getText().toString();
                        String addInfo = additionalInfo.getText().toString();

                        obj.put("timestamp", timeStamp)
                                .put("name", name)
                                .put("company", company)
                                .put("email", email)
                                .put("phone", phone)
                                .put("addInfo", addInfo)
                                .put("checkbox1", checkbox1.isChecked())
                                .put("checkbox2", checkbox2.isChecked())
                                .put("checkbox3", checkbox3.isChecked());

                        edit.putString(timeStamp, obj.toString()).apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(getActivity(), "SAVED CONTACT " + obj.toString(), Toast.LENGTH_SHORT).show();
                    getDialog().cancel();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int kind_of_dialog = getArguments().getInt(KIND_OF_DIALOG);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if (kind_of_dialog == ADD_WITH_IMAGE) {
            dispatchTakePictureIntent();
            view = inflater.inflate(R.layout.storeimageview, container, false);
            storeImageView = (RelativeLayout) view.findViewById(R.id.storeimage);
            imageView = (ImageView) view.findViewById(R.id.capturedimage);
            additionalInfoImage = (EditText) view.findViewById(R.id.additionalinformation_edittext);

            checkbox1 = (CheckBox) view.findViewById(R.id.checkbox1);
            checkbox2 = (CheckBox) view.findViewById(R.id.checkbox2);
            checkbox3 = (CheckBox) view.findViewById(R.id.checkbox3);


            return view;
        } else {

            view = inflater.inflate(R.layout.addcontactview, container, false);

            nameEditText = (EditText) view.findViewById(R.id.nameedittext);
            companyNameEditText = (EditText) view.findViewById(R.id.companymaneedittext);
            emailEditText = (EditText) view.findViewById(R.id.emailedittext);
            phoneEditText = (EditText) view.findViewById(R.id.phoneedittext);
            additionalInfo = (EditText) view.findViewById(R.id.addinfoedittext);

            checkbox1 = (CheckBox) view.findViewById(R.id.checkbox1);
            checkbox2 = (CheckBox) view.findViewById(R.id.checkbox2);
            checkbox3 = (CheckBox) view.findViewById(R.id.checkbox3);

            return view;
        }


    }

    /**
     * creates a file for the camera app and stores the path ion mCurrentPhotoPath
     *
     * @return empty file which will be filled by camera application
     * @throws IOException when file cant created
     */
    private File createImageFile() throws IOException {
        // Create an image file name

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BusinessCards");

        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY).format(new Date());
        File mediaFile = new File(storageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = mediaFile.getAbsolutePath();

        Log.d(TAG, "IMAGE CREATED " + mCurrentPhotoPath);
        return mediaFile;
    }

    /**
     * starts the camera app and provides a pre defined file to store the pictuure
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
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
                // Image captured and saved to fileUri specified in the Intent

                Log.d(TAG, "CURRENTPATH: " + mCurrentPhotoPath);

                Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

                imageView.setImageBitmap(imageBitmap);
                storeImageView.setVisibility(View.VISIBLE);


            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
                getDialog().cancel();
            } else {
                // Image capture failed, advise user
                //TODO add dialog to inform user
            }

        }
    }


}
