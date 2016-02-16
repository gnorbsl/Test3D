package izm.fraunhofer.de.phoffmn.test3d.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import izm.fraunhofer.de.phoffmn.test3d.R;

/**
 * Created by phoffmn on 26.01.2016.
 */
public class CustomBondPagerAdapter extends PagerAdapter {

    private final static int BOND_MODE = 0;

    int[] mResources;

    int[] mResourcesBond = {
            R.drawable.wire_125_new_small2,
            R.drawable.wire_400_new_small2
    };

    int[] mResourcesCooler = {
            R.drawable.cooler_passive_new,
            R.drawable.cooler_active_new_small
    };

    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomBondPagerAdapter(Context context, int mode) {

        if (mode == BOND_MODE) {
            mResources = mResourcesBond;
        } else {
            mResources = mResourcesCooler;
        }
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.bond_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        imageView.setImageResource(mResources[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
