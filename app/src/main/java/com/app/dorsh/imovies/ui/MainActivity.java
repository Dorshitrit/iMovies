package com.app.dorsh.imovies.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.dorsh.imovies.R;
import com.app.dorsh.imovies.db.DBHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import static com.app.dorsh.imovies.db.Constants.AD_KEY;
import static com.app.dorsh.imovies.db.Constants.DEVICE_AD_KEY;
import static com.app.dorsh.imovies.db.Constants.MOB_AD_KEY;


public class MainActivity extends AppCompatActivity {
    DBHandler handler;
    Context context;
    Activity activity;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String oneFrag = "My Library";
    String twoFrag = "My Favorite";
    String threeFrag = "My Rated";
    String fourFrag = "My Watched";
    InterstitialAd mInterstitialAd;


    private int[] tabIcons = {
            R.drawable.ic_add_plus,
            R.drawable.ic_favorite_on,
            R.drawable.ic_rating_on,
            R.drawable.ic_watch
    };

    public MainActivity(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AD_KEY);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                mInterstitialAd.show();
            }
        });

        requestNewInterstitial();


        MobileAds.initialize(getApplicationContext(), MOB_AD_KEY);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(DEVICE_AD_KEY)

                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                mInterstitialAd.show();
            }
        });



        mInterstitialAd.show();


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        handler = new DBHandler(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), oneFrag);
        adapter.addFragment(new TwoFragment(), twoFrag);
        adapter.addFragment(new ThreeFragment(), threeFrag);
        adapter.addFragment(new FourFragment(), fourFrag);
        viewPager.setAdapter(adapter);
        setupTabIcons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_deleteAll) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("WARNING!");
            dialog.setMessage("Remove movie list?");
            dialog.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    handler.dellAllmovies();
                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    adapter.addFragment(new OneFragment(), oneFrag);
                    adapter.addFragment(new TwoFragment(), twoFrag);
                    adapter.addFragment(new ThreeFragment(), threeFrag);
                    adapter.addFragment(new FourFragment(), fourFrag);
                    viewPager.setAdapter(adapter);
                    setupTabIcons();
                }
            });
            dialog.setNegativeButton("No", null);
            dialog.show();
            return true;
        }
        if (id == R.id.action_about) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Credits");
            dialog.setMessage("Design & Program by Dor Shitrit \n \n App Credits: \n https://www.omdbapi.com \n https://www.themoviedb.org");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                }
            });
            dialog.setNegativeButton("Exit", null);
            dialog.show();
            return true;
        }
        if (id == R.id.action_Exit) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), oneFrag);
        adapter.addFragment(new TwoFragment(), twoFrag);
        adapter.addFragment(new ThreeFragment(), threeFrag);
        adapter.addFragment(new FourFragment(), fourFrag);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("34984E4EF97ACA")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }




}
