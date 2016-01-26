package izm.fraunhofer.de.phoffmn.test3d.adapter;

import android.support.v4.view.PagerAdapter;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;

/**
 * Created by phoffmn on 26.01.2016.
 */
public class TestAdapter extends InfinitePagerAdapter {
    public TestAdapter(PagerAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getCount() {
        return 3000;
    }

}
