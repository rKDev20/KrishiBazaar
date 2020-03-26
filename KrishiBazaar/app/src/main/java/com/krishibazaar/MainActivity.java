package com.krishibazaar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.krishibazaar.Utils.LocationManagerActivity;
import com.krishibazaar.Utils.VolleyRequestMaker;

public class MainActivity extends LocationManagerActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public final static String HOME = "home";
    public final static String SELL = "sell";
    public final static String PROFILE = "profile";

    private Fragment home;
    private Fragment profile;
    private Fragment sell;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String token = SharedPreferenceManager.getToken(this);
        String token="34736842368r32y83286483264";
        if (token == null)
            startActivity(new Intent(this, LoginActivity.class));
        else {
            initViews();
            loadFragment(HOME);
        }
//        startActivity(new Intent(this, ProductViewActivity.class));
    }

    private void initViews() {
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (navigation.getSelectedItemId() != menuItem.getItemId()) {
            menuItem.setChecked(true);
            switch (menuItem.getItemId()) {
                case R.id.sell:
                    loadFragment(SELL);
                    break;
                case R.id.home:
                    loadFragment(HOME);
                    break;
                case R.id.profile:
                    loadFragment(PROFILE);
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Fragment current = getSupportFragmentManager().getPrimaryNavigationFragment();
        if (current instanceof ProfileActivity) {
            if (((ProfileActivity) current).onBackPressed())
                navigation.setSelectedItemId(R.id.home);
        } else if (!(current instanceof HomeActivity))
            navigation.setSelectedItemId(R.id.home);
        else
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyRequestMaker.destroy();
    }

    private void loadFragment(String frag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment curFrag = getSupportFragmentManager().getPrimaryNavigationFragment();
        if (curFrag != null) {
            fragmentTransaction.hide(curFrag);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(frag);
        if (fragment == null) {
            fragment = getFragment(frag);
            assert fragment != null;
            fragmentTransaction.add(R.id.frame, fragment, frag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    private Fragment getFragment(String frag) {
        switch (frag) {
            case HOME:
                if (home == null)
                    home = new HomeActivity();
                return home;
            case PROFILE:
                if (profile == null)
                    profile = new ProfileActivity();
                return profile;
            case SELL:
                if (sell == null)
                    sell = new SellProductActivity();
                return sell;
            default:
                return null;
        }
    }
}
