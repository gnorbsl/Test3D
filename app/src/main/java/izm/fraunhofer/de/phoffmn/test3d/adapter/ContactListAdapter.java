package izm.fraunhofer.de.phoffmn.test3d.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.helper.Contact;

/**
 * Created by phoffmn on 05.10.2015.
 */
public class ContactListAdapter extends ArrayAdapter{

    private static final String TAG = "ContactListAdapter";
    private final Context context;
    private final int layoutResourceId;
    private final JSONObject[] contacts;



    public ContactListAdapter(Context context, int layoutResourceId, JSONObject[] contacts) {
        super(context, layoutResourceId, contacts);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.contacts = contacts;

        for (JSONObject jsonObject : contacts) {
            Log.d(TAG, jsonObject.toString());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //TODO clean stuff up and use a recyclerview or whatever
        
        View row = convertView;
        ContactHolder holder = null;

        JSONObject contact = contacts[position];

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            holder = new ContactHolder();

            if (contact.has("imagepath")) {
                row = inflater.inflate(R.layout.image_item, parent, false);

                holder.businessCard = (ImageView)row.findViewById(R.id.businesscard);
                holder.additionalInformation = (TextView)row.findViewById(R.id.additionalInformation);

                holder.checkbox1 =(CheckBox)row.findViewById(R.id.checkBox1);
                holder.checkbox2 =(CheckBox)row.findViewById(R.id.checkBox2);
                holder.checkbox3 =(CheckBox)row.findViewById(R.id.checkBox3);

                Bitmap imageBitmap = null;
                try {
                    imageBitmap = BitmapFactory.decodeFile(contact.getString("imagepath"));
                    holder.businessCard.setImageBitmap(imageBitmap);

                    holder.additionalInformation.setText(contact.getString("addInfo"));

                    holder.checkbox1.setChecked(contact.getBoolean("checkbox1"));
                    holder.checkbox2.setChecked(contact.getBoolean("checkbox2"));
                    holder.checkbox3.setChecked(contact.getBoolean("checkbox3"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            } else {
                row = inflater.inflate(R.layout.contact_item, parent, false);

                holder.additionalInformation = (TextView)row.findViewById(R.id.additionalInformation);

                holder.name = (TextView)row.findViewById(R.id.name);
                holder.company = (TextView)row.findViewById(R.id.company);
                holder.email = (TextView)row.findViewById(R.id.email);
                holder.telephone = (TextView)row.findViewById(R.id.telephone);

                holder.checkbox1 =(CheckBox)row.findViewById(R.id.checkBox1);
                holder.checkbox2 =(CheckBox)row.findViewById(R.id.checkBox2);
                holder.checkbox3 =(CheckBox)row.findViewById(R.id.checkBox3);


                try {

                    holder.name.setText(contact.getString("name"));
                    holder.company.setText(contact.getString("company"));
                    holder.email.setText(contact.getString("email"));
                    holder.telephone.setText(contact.getString("phone"));

                    holder.additionalInformation.setText(contact.getString("addInfo"));

                    holder.checkbox1.setChecked(contact.getBoolean("checkbox1"));
                    holder.checkbox2.setChecked(contact.getBoolean("checkbox2"));
                    holder.checkbox3.setChecked(contact.getBoolean("checkbox3"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }




        }

        return row;
    }

    private class ContactHolder {

        ImageView businessCard;

        TextView additionalInformation;

        TextView name, company, email, telephone;
        CheckBox checkbox1, checkbox2, checkbox3;
    }
}
