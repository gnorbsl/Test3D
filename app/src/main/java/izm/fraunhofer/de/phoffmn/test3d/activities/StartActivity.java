package izm.fraunhofer.de.phoffmn.test3d.activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antonyt.infiniteviewpager.InfiniteViewPager;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.adapter.CustomBondPagerAdapter;
import izm.fraunhofer.de.phoffmn.test3d.adapter.TestAdapter;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ContactListFragment;
import izm.fraunhofer.de.phoffmn.test3d.helper.Tools;

public class StartActivity extends AppCompatActivity {


    //TODO add constant class
    public static final String BOND_DIAMETER = "bond_diameter";
    public static final String TAG = "StartActivity";
    public static final String COOLER_TYPE = "cooler_type";
    public static final String T_AMBIENT = "t_ambient";
    int realPosition;

    @Bind(R.id.mainView)
    View mainView;

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

    @Bind(R.id.aluminium)
    TextView aluminium;

    @Bind(R.id.copper)
    TextView copper;


    private int counter;

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

        bondInfo.setText("Ø " + infos[0] + " µm");

        bondPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled " + position + " " + positionOffset);
            }

            @Override
            public void onPageSelected(int position) {


                realPosition = position % wrappedAdapter.getRealCount();


                bondInfo.setText("Ø " + infos[realPosition] + " µm");

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


        startLifeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCalculationIntent = new Intent(StartActivity.this, MainActivity.class);
                startCalculationIntent.putExtra(BOND_DIAMETER, infos[realPosition])
                        .putExtra(COOLER_TYPE, coolerInfo.getText().toString().split(" ")[0])
                        .putExtra(T_AMBIENT, currentTemp.getText().toString());
                startActivity(startCalculationIntent);
            }
        });

        aluminium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if (counter > 3) {
                    Toast.makeText(StartActivity.this, "" + counter, Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onClick counter" + counter);
            }
        });

        copper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter == 5) {
                    Log.d(TAG, "onClick TEST");
                    Map<String, ?> prefEntries = Tools.getPreferences(StartActivity.this).getAll();

                    String[] contacts = new String[prefEntries.size()];

                    int i = 0;

                    for (Map.Entry<String, ?> entry : prefEntries.entrySet()) {

                        contacts[i] = entry.getValue().toString();
                        i++;
                    }

                    DialogFragment newFragment = ContactListFragment.newInstance(contacts);
                    newFragment.show(createFragmentTransaction("list_dialog"), "list_dialog");
                }
                counter = 0;
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mainView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
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

    @Override
    public void onBackPressed() {

    }
}
