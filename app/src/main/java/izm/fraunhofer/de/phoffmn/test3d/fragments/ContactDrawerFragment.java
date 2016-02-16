package izm.fraunhofer.de.phoffmn.test3d.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hollowsoft.slidingdrawer.SlidingDrawer;
import izm.fraunhofer.de.phoffmn.test3d.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDrawerFragment extends Fragment {

    //interface to communicate with the activity
    ContactFragmentInterface contactFragmentInterface;

    public void closeDrawer() {
        if (drawerContact.isOpened())
        drawerContact.animateClose();
    }

    public interface ContactFragmentInterface {
        void dispatchTakePictureIntent();
        void createDialogFragment(int kind_of_dialog);
    }


    //ContactDrawer
    @Bind(R.id.contact_drawer) SlidingDrawer drawerContact;


    //handle clicks on items in the drawer
    @OnClick(R.id.contact_camera_text)
    void addContactWithImage(){

        contactFragmentInterface.dispatchTakePictureIntent();
        drawerContact.close();
    }

    @OnClick(R.id.contact_add_text)
    void addContactWithoutImage() {

        contactFragmentInterface.createDialogFragment(ContactDialogFragment.ADD_WITHOUT_IMAGE);
        drawerContact.close();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            contactFragmentInterface = (ContactFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ContactFragmentInterface");
        }
    }

    public void showDrawer() {

        drawerContact.setVisibility(View.VISIBLE);
    }





}


