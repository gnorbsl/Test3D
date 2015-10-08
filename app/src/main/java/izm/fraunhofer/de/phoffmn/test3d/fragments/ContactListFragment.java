package izm.fraunhofer.de.phoffmn.test3d.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.adapter.ContactListAdapter;
import rx.Observable;
import rx.functions.Func1;

public class ContactListFragment extends DialogFragment {


    private AbsListView mListView;

    private ListAdapter mAdapter;


    public static ContactListFragment newInstance(String[] contacts) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putStringArray("contacts", contacts);
        fragment.setArguments(args);
        return fragment;
    }


    public ContactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] contacts = (String[]) getArguments().get("contacts");

        JSONObject[] jsonContacts = new JSONObject[contacts.length];

        for (int i = 0; i < contacts.length; i++) {
            try {
                jsonContacts[i] = new JSONObject(contacts[i]);
                Log.d("MainActivity", contacts[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mAdapter = new ContactListAdapter(getActivity(), R.layout.fragment_contact_list, jsonContacts);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.getDialog().setTitle("Stored contacts");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }


}
