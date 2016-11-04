package com.joncatanio.billme;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "BillMe";
    private Drawer drawer;
    private AccountHeader accountHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Dashboard").withIcon(android.R.drawable.ic_menu_manage).withSelectedIconColor(accent).withSelectedIconColorRes(R.color.accent),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Groups").withIcon(android.R.drawable.ic_menu_add).withSelectedIconColor(accent),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Account").withIcon(android.R.drawable.ic_menu_compass).withSelectedIconColor(accent),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Settings").withIcon(android.R.drawable.ic_menu_preferences).withSelectedIconColor(accent)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int)drawerItem.getIdentifier()) {
                            case 1:
                                Log.d(TAG, "1");
                                break;
                            case 2:
                                Log.d(TAG, "2");
                                break;
                            case 3:
                                Log.d(TAG, "3");
                                break;
                            case 4:
                                Log.d(TAG, "4");
                                break;
                            default:
                                Log.d(TAG, "default");
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }
}
