package izm.fraunhofer.de.phoffmn.test3d.activities;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.adapter.CustomBondPagerAdapter;
import izm.fraunhofer.de.phoffmn.test3d.adapter.TestAdapter;

public class StartActivity extends AppCompatActivity {


    //TODO add constant class
    public static final String BOND_DIAMETER = "bond_diameter";
    private static final String TAG = "StartActivity";
    @Bind(R.id.button100)
    Button button100;



    @OnClick(R.id.button100)
    public void click100() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra(BOND_DIAMETER, "guidetest");
        startActivity(intent);
    }


    @Bind(R.id.button20)
    Button button20;
    @OnClick(R.id.button20)
    public void click20() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra(BOND_DIAMETER, "hopethisworks");
        startActivity(intent);
    }

    @Bind(R.id.bondPager)
    InfiniteViewPager bondPager;

    @Bind(R.id.coolerPager)
    InfiniteViewPager coolerPager;



    private PagerAdapter mCustomBondPagerAdapter;
    private PagerAdapter mCustomCoolerPagerAdapter;

    @Bind(R.id.leftButton)
    ImageButton leftButton;
    @Bind(R.id.rightButton)
    ImageButton rightButton;

    @Bind(R.id.leftButtonCooler)
    ImageButton leftButtonCooler;
    @Bind(R.id.rightButtonCooler)
    ImageButton rightButtonCooler;

    @Bind(R.id.bondInfo)
    TextView bondInfo;

    @Bind(R.id.coolerInfo)
    TextView coolerInfo;

    @Bind(R.id.tambientSeekBar)
    SeekBar tAmbientSeekBar;

    @Bind(R.id.currentTemp)
    TextView currentTemp;

    @Bind(R.id.startLifetime)
    Button startLifeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);


        getSupportActionBar().hide();
        String[] infos = getResources().getStringArray(R.array.bondInfos);

        bondInfo.setText(infos[0]);
        coolerInfo.setText("Passive Cooler");

        mCustomBondPagerAdapter = new CustomBondPagerAdapter(this, 0);
        TestAdapter wrappedAdapter = new TestAdapter(mCustomBondPagerAdapter);
        bondPager.setAdapter(wrappedAdapter);



        bondPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled " + position + " " + positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

                int realPosition = position % wrappedAdapter.getRealCount();

                bondInfo.setText(infos[realPosition]);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });



        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bondPager.setCurrentItem(bondPager.getCurrentItem() - 1, true);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bondPager.setCurrentItem(bondPager.getCurrentItem() + 1, true);
            }
        });

        mCustomCoolerPagerAdapter = new CustomBondPagerAdapter(this, 1);
        TestAdapter wrappedAdapterCooler = new TestAdapter(mCustomCoolerPagerAdapter);
        coolerPager.setAdapter(wrappedAdapterCooler);

        coolerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled " + position + " " + positionOffset);
            }

            @Override
            public void onPageSelected(int position) {


                if (position % wrappedAdapter.getRealCount() == 0) {

                    coolerInfo.setText("Passive Cooler");
                } else {
                    coolerInfo.setText("Active Cooler");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        leftButtonCooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coolerPager.setCurrentItem(coolerPager.getCurrentItem() - 1, true);
            }
        });

        rightButtonCooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coolerPager.setCurrentItem(coolerPager.getCurrentItem() + 1, true);
            }
        });


        tAmbientSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged " + progress);

                currentTemp.setText((progress + 20) + " °C");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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


}
