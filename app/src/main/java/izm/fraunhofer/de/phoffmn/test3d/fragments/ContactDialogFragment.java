package izm.fraunhofer.de.phoffmn.test3d.fragments;


import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.helper.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDialogFragment extends DialogFragment {

    public static final String KIND_OF_DIALOG = "kind_of_dialog";
    public static final int ADD_WITHOUT_IMAGE = 0;
    public static final int ADD_WITH_IMAGE = 1;
    private static final String TAG = "ContactDialogFragment";
    private View view;
    private EditText additionalInfoImage;
    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private CheckBox checkbox4;
    private CheckBox checkbox5;
    private EditText nameEditText;
    private EditText companyNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText additionalInfo;

    int kind_of_dialog;

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



        kind_of_dialog = getArguments().getInt(KIND_OF_DIALOG);

        Button saveContact = (Button) view.findViewById(R.id.savecontact);

        Button cancelDialog = (Button) view.findViewById(R.id.cancelcontact);
        cancelDialog.setOnClickListener(v -> {

            getDialog().dismiss();

            if (kind_of_dialog == ADD_WITH_IMAGE)
                new File(Tools.currentPhotoPath).delete();
        });



        final SharedPreferences pref = Tools.getPreferences(getActivity());
        final SharedPreferences.Editor edit = pref.edit();

        if (kind_of_dialog == ADD_WITH_IMAGE) {

            saveContact.setOnClickListener(v -> {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY).format(new Date());

                String additionalInfo = additionalInfoImage.getText().toString();

                JSONObject obj = new JSONObject();
                //TODO change the checkbox names accordingly
                try {
                    obj.put("timestamp", timeStamp)
                            .put("imagepath", Tools.currentPhotoPath)
                            .put("addInfo", additionalInfo)
                            .put("checkbox1", checkbox1.isChecked())
                            .put("checkbox2", checkbox2.isChecked())
                            .put("checkbox3", checkbox3.isChecked());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                edit.putString(timeStamp, obj.toString());
                edit.apply();

                getDialog().dismiss();

            });
        } else if (kind_of_dialog == ADD_WITHOUT_IMAGE) {
            saveContact.setOnClickListener(v -> {

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
                            .put("checkbox3", checkbox3.isChecked())
                            .put("checkbox4", checkbox4.isChecked())
                            .put("checkbox5", checkbox5.isChecked());

                    edit.putString(timeStamp, obj.toString()).apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                getDialog().dismiss();
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int kind_of_dialog = getArguments().getInt(KIND_OF_DIALOG);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if (kind_of_dialog == ADD_WITH_IMAGE) {


            view = inflater.inflate(R.layout.storeimageview, container, false);

            RelativeLayout storeImageView = (RelativeLayout) view.findViewById(R.id.storeimage);
            ImageView imageView = (ImageView) view.findViewById(R.id.capturedimage);
            additionalInfoImage = (EditText) view.findViewById(R.id.additionalinformation_edittext);

            Picasso.with(getActivity()).load(new File(Tools.currentPhotoPath)).into(imageView);
            storeImageView.setVisibility(View.VISIBLE);

        } else {

            view = inflater.inflate(R.layout.addcontactview, container, false);

            nameEditText = (EditText) view.findViewById(R.id.nameedittext);
            companyNameEditText = (EditText) view.findViewById(R.id.companymaneedittext);
            emailEditText = (EditText) view.findViewById(R.id.emailedittext);
            phoneEditText = (EditText) view.findViewById(R.id.phoneedittext);
            additionalInfo = (EditText) view.findViewById(R.id.addinfoedittext);

        }


        checkbox1 = (CheckBox) view.findViewById(R.id.contact_check1);
        checkbox2 = (CheckBox) view.findViewById(R.id.contact_check2);
        checkbox3 = (CheckBox) view.findViewById(R.id.contact_check3);
        checkbox4 = (CheckBox) view.findViewById(R.id.contact_check4);
        checkbox5 = (CheckBox) view.findViewById(R.id.contact_check5);


        return view;

    }



}
