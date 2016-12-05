package com.joncatanio.billme;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity
    implements DashboardFragment.OnFragmentInteractionListener, GroupsFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener, NewBillFragment.OnFragmentInteractionListener, NewGroupFragment.OnFragmentInteractionListener {
    private static final String TAG = "BillMe";
    private Drawer drawer;
    private AccountHeader accountHeader;
    private FragmentManager fragmentManager;

    private static final int DASHBOARD = 0;
    private static final int GROUPS = 1;
    private static final int ACCOUNT = 2;
    private static final int SETTINGS = 3;
    private static final int LOGOUT = 4;

    public static final String NEW_BILL = "newBill";
    public static final String NEW_GROUP = "newGroup";
    private Integer currentFragment;

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return currentFragment;
    }

    @Override
    public Object getLastCustomNonConfigurationInstance() {
        return super.getLastCustomNonConfigurationInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        new ProfileDrawerItem()
                            .withName("Mark McKinney")
                            .withEmail("markp.mckinney@gmail.com")
                )
                .build();

        int accent = getResources().getColor(R.color.colorAccent);

        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(DASHBOARD).withName("Dashboard").withIcon(android.R.drawable.ic_menu_manage).withSelectedIconColor(accent).withSelectedIconColorRes(R.color.accent),
                        new PrimaryDrawerItem().withIdentifier(GROUPS).withName("Groups").withIcon(android.R.drawable.ic_menu_add).withSelectedIconColor(accent),
                        new PrimaryDrawerItem().withIdentifier(ACCOUNT).withName("Account").withIcon(android.R.drawable.ic_menu_compass).withSelectedIconColor(accent),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(SETTINGS).withName("Settings").withIcon(android.R.drawable.ic_menu_preferences).withSelectedIconColor(accent),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIdentifier(LOGOUT).withName("Log Out").withIcon(android.R.drawable.ic_menu_delete).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        switch ((int)drawerItem.getIdentifier()) {
                            case DASHBOARD:
                                Log.d(TAG, "dashboard");
                                currentFragment = DASHBOARD;
                                fragmentTransaction.replace(R.id.fragmentLayout, new DashboardFragment());
                                break;
                            case GROUPS:
                                Log.d(TAG, "groups");
                                currentFragment = GROUPS;
                                fragmentTransaction.replace(R.id.fragmentLayout, new GroupsFragment());
                                break;
                            case ACCOUNT:
                                Log.d(TAG, "account");
                                currentFragment = ACCOUNT;
                                fragmentTransaction.replace(R.id.fragmentLayout, new AccountFragment());
                                break;
                            case SETTINGS:
                                Log.d(TAG, "settings");
                                currentFragment = SETTINGS;
                                fragmentTransaction.replace(R.id.fragmentLayout, new SettingsFragment());
                                break;
                            case LOGOUT:
                                Log.d(TAG, "logout");
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Log Out")
                                        .setMessage("Are you sure you want to log out?")
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                BillMeApi.resetAuthToken(MainActivity.this);
                                                finish();
                                            }
                                        })
                                        .setNegativeButton(R.string.no, null)
                                        .show();
                                break;
                            default:
                                Log.d(TAG, "default");
                                fragmentTransaction.replace(R.id.fragmentLayout, new DashboardFragment());
                                break;
                        }
                        fragmentTransaction.commit();
                        return false;
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            drawer = drawerBuilder
                    .withToolbar(toolbar)
                    .withActionBarDrawerToggle(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .build();
        } else {
            drawer = drawerBuilder.buildView();
            FrameLayout drawerLayout = (FrameLayout) findViewById(R.id.drawerLayout);
            drawerLayout.addView(drawer.getSlider());
        }

        Integer frag = (Integer) getLastCustomNonConfigurationInstance();
        if (frag != null) {
            currentFragment = frag;
        } else {
            currentFragment = DASHBOARD;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (currentFragment) {
            case DASHBOARD:
                Log.d(TAG, "dashboard");
                currentFragment = DASHBOARD;
                fragmentTransaction.add(R.id.fragmentLayout, new DashboardFragment());
                break;
            case GROUPS:
                Log.d(TAG, "groups");
                currentFragment = GROUPS;
                fragmentTransaction.add(R.id.fragmentLayout, new GroupsFragment());
                break;
            case ACCOUNT:
                Log.d(TAG, "account");
                currentFragment = ACCOUNT;
                fragmentTransaction.add(R.id.fragmentLayout, new AccountFragment());
                break;
            case SETTINGS:
                Log.d(TAG, "settings");
                currentFragment = SETTINGS;
                fragmentTransaction.add(R.id.fragmentLayout, new SettingsFragment());
                break;
            default:
                Log.d(TAG, "default");
                fragmentTransaction.add(R.id.fragmentLayout, new DashboardFragment());
                break;
        }
        fragmentTransaction.commit();
        drawer.setSelection(currentFragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        switch (uri.getFragment()) {
            case NEW_BILL:
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout, new NewBillFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case NEW_GROUP:
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                fragmentTransaction2.replace(R.id.fragmentLayout, new NewGroupFragment());
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}