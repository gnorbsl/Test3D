package izm.fraunhofer.de.phoffmn.test3d.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import izm.fraunhofer.de.phoffmn.test3d.R;

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

    public int getViewTypeCount() {

        return getCount()>=1?getCount():1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        final JSONObject contact = contacts[position];

        if (convertView == null) {
            holder = new ViewHolder();

            if (contact.has("imagepath")) {
                convertView = LayoutInflater.from(context).inflate(R.layout.image_contact_item, parent, false);

                holder.businessCard = (ImageView)convertView.findViewById(R.id.businesscard);
                holder.additionalInformation = (TextView) convertView.findViewById(R.id.add_info_textview_image_contact);

                holder.checkbox1 = (CheckBox)convertView.findViewById(R.id.image_check1);
                holder.checkbox2 = (CheckBox)convertView.findViewById(R.id.image_check2);
                holder.checkbox3 = (CheckBox)convertView.findViewById(R.id.image_check3);

            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);

                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.company = (TextView) convertView.findViewById(R.id.company);
                holder.telephone = (TextView) convertView.findViewById(R.id.telephone);
                holder.email = (TextView) convertView.findViewById(R.id.email);

                holder.additionalInformation = (TextView) convertView.findViewById(R.id.add_info_textview_contact);

                holder.checkbox1 = (CheckBox)convertView.findViewById(R.id.contact_check1);
                holder.checkbox2 = (CheckBox)convertView.findViewById(R.id.contact_check2);
                holder.checkbox3 = (CheckBox)convertView.findViewById(R.id.contact_check3);
            }


            convertView.setTag(holder);


        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        try {

            if (contact.has("imagepath")) {


                if (null != holder.businessCard) {
                    Picasso.with(context).load(new File(contact.getString("imagepath"))).into(holder.businessCard);

                    holder.businessCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

                            View layout = inflater.inflate(R.layout.large_image, null);

                            ImageView largeImage = (ImageView) layout.findViewById(R.id.large_image);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);

                            builder.setView(layout);

                            Dialog d = builder.create();


                            View bla = d.getLayoutInflater().inflate(R.layout.large_image, parent, false);
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(d.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                            try {
                                Picasso.with(context).load(new File(contact.getString("imagepath"))).into(largeImage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            d.show();
                            d.getWindow().setAttributes(lp);

                        }
                    });
                }


            } else {
                if (null != holder.name &&
                        null != holder.company &&
                        null != holder.telephone &&
                        null != holder.email) {

                    holder.name.setText(contact.getString("name"));
                    holder.company.setText(contact.getString("company"));
                    holder.telephone.setText(contact.getString("phone"));
                    holder.email.setText(contact.getString("email"));

                }


            }


            holder.additionalInformation.setText(contact.getString("addInfo"));

            holder.checkbox1.setChecked(contact.getBoolean("checkbox1"));
            holder.checkbox2.setChecked(contact.getBoolean("checkbox2"));
            holder.checkbox3.setChecked(contact.getBoolean("checkbox3"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }


    private class ViewHolder {

        ImageView businessCard;

        TextView additionalInformation;

        TextView name, company, email, telephone;
        CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6;
    }
}
