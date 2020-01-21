package com.noavaran.system.vira.baryab.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.DachshundTabLayout;
import com.noavaran.system.vira.baryab.dialogs.SyncFailureDialog;
import com.noavaran.system.vira.baryab.dialogs.SynchronizationDialog;
import com.noavaran.system.vira.baryab.fragments.CarriedLoadingFragment;
import com.noavaran.system.vira.baryab.fragments.MyLoadingsFragment;
import com.noavaran.system.vira.baryab.fragments.NewLoadFragment;
import com.noavaran.system.vira.baryab.fragments.ProfileFragment;
import com.noavaran.system.vira.baryab.helpers.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    //    private SpaceTabLayout tabLayout;
    private DachshundTabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> listFragments;

    private int[] enTabIcons = {
            R.drawable.ic_home1,
            R.drawable.ic_safiran1,
            R.drawable.ic_register_new_shipment,
            R.drawable.ic_my_loadings
    };

    private int[] faTabIcons = {
            R.drawable.ic_my_loadings,
            R.drawable.ic_register_new_shipment,
            R.drawable.ic_safiran1,
            R.drawable.ic_home1
    };

    private CarriedLoadingFragment carriedLoadingFragment;
    private NewLoadFragment        newLoadFragment;
    private MyLoadingsFragment     myLoadingsFragment;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initComponents();
        setViewsListeners();
        setupViewPager();
        setupTabLayout();
        new Handler(Looper.getMainLooper()).postDelayed(doSynchronization, 500);

        changeTabsFont();
    }

    private void findViews() {
        viewPager = (ViewPager) findViewById(R.id.acMain_viewPager);
        tabLayout = (DachshundTabLayout) findViewById(R.id.acMain_spaceTabLayout);
    }

    private void initComponents() {
        carriedLoadingFragment = new CarriedLoadingFragment();
        newLoadFragment        = new NewLoadFragment();
        myLoadingsFragment     = new MyLoadingsFragment();


    }

    private void setViewsListeners() {
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        if (LocaleHelper.getLanguage(MainActivity.this).equals("en")) {
            tabLayout.getTabAt(3).setIcon(enTabIcons[3]);
            tabLayout.getTabAt(2).setIcon(enTabIcons[2]);
            tabLayout.getTabAt(1).setIcon(enTabIcons[1]);
            tabLayout.getTabAt(0).setIcon(enTabIcons[0]);
        } else if (LocaleHelper.getLanguage(MainActivity.this).equals("fa")) {
            tabLayout.getTabAt(3).setIcon(faTabIcons[3]);
            tabLayout.getTabAt(2).setIcon(faTabIcons[2]);
            tabLayout.getTabAt(1).setIcon(faTabIcons[1]);
            tabLayout.getTabAt(0).setIcon(faTabIcons[0]);
        } else {
            tabLayout.getTabAt(3).setIcon(enTabIcons[3]);
            tabLayout.getTabAt(2).setIcon(enTabIcons[2]);
            tabLayout.getTabAt(1).setIcon(enTabIcons[1]);
            tabLayout.getTabAt(0).setIcon(enTabIcons[0]);
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (LocaleHelper.getLanguage(MainActivity.this).equals("en")) {
            adapter.addFrag(new ProfileFragment(), "خانه");
            adapter.addFrag(carriedLoadingFragment, "حمل شده");
            adapter.addFrag(newLoadFragment, "ثبت بار");
            adapter.addFrag(myLoadingsFragment, "بارهای من");
        } else if (LocaleHelper.getLanguage(MainActivity.this).equals("fa")) {
            adapter.addFrag(myLoadingsFragment, "بارهای من");
            adapter.addFrag(newLoadFragment, "ثبت بار");
            adapter.addFrag(carriedLoadingFragment, "حمل شده");
            adapter.addFrag(new ProfileFragment(), "خانه");
        } else {
            adapter.addFrag(new ProfileFragment(), "خانه");
            adapter.addFrag(carriedLoadingFragment, "حمل شده");
            adapter.addFrag(newLoadFragment, "ثبت بار");
            adapter.addFrag(myLoadingsFragment, "بارهای من");
        }
        viewPager.setAdapter(adapter);

        if (LocaleHelper.getLanguage(MainActivity.this).equals("en"))
            viewPager.setCurrentItem(2);
        else if (LocaleHelper.getLanguage(MainActivity.this).equals("fa"))
            viewPager.setCurrentItem(1);
        else
            viewPager.setCurrentItem(2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newLoadFragment.onActivityResult(requestCode, resultCode, data);
        myLoadingsFragment.onActivityResult(requestCode, resultCode, data);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void changeTabsFont() {
        Typeface customFontTypeface = Typeface.createFromAsset(getAssets(), getString(R.string.baseFont));
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(customFontTypeface);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToastInfo("برای خروج یکبار دیگر بازگشت را بزنید");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        freeMemory();
        super.onDestroy();
    }

    private Runnable doSynchronization = new Runnable() {
        @Override
        public void run() {
            findViewById(R.id.acMain_rlMainLayout).post(new Runnable() {
                @Override
                public void run() {
                    final SynchronizationDialog dialog = new SynchronizationDialog(MainActivity.this);
                    dialog.setOnSynchronizationListener(new SynchronizationDialog.OnSynchronizationListener() {
                        @Override
                        public void onSuccess() {
                            showToastSuccess("همگام سازی اطلاعات با سرور انجام شد");
                        }

                        @Override
                        public void onFailure() {
                            SyncFailureDialog syncFailureDialog = new SyncFailureDialog(MainActivity.this);
                            syncFailureDialog.show();
                        }
                    });
                    dialog.show();
                }
            });
        }
    };
}