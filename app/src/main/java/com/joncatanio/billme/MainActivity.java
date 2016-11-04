package com.joncatanio.billme;

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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BillMe";
    private Drawer drawer;
    private AccountHeader accountHeader;

    private static final int DASHBOARD = 0;
    private static final int GROUPS = 1;
    private static final int ACCOUNT = 2;
    private static final int SETTINGS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        new PrimaryDrawerItem().withIdentifier(SETTINGS).withName("Settings").withIcon(android.R.drawable.ic_menu_preferences).withSelectedIconColor(accent)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int)drawerItem.getIdentifier()) {
                            case DASHBOARD:
                                Log.d(TAG, "dashboard");
                                break;
                            case GROUPS:
                                Log.d(TAG, "groups");
                                break;
                            case ACCOUNT:
                                Log.d(TAG, "account");
                                break;
                            case SETTINGS:
                                Log.d(TAG, "settings");
                                break;
                            default:
                                Log.d(TAG, "default");
                                break;
                        }
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