package com.joncatanio.billme;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.joncatanio.billme.model.Bill;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
    implements DashboardFragment.OnFragmentInteractionListener, GroupsFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener, NewBillFragment.OnFragmentInteractionListener, NewGroupFragment.OnFragmentInteractionListener {
    private static final String TAG = "BillMe";
    private Drawer drawer;
    private AccountHeader accountHeader;
    private BillObserver billObserver;
    private BillAdapter billAdapter;
    private FragmentManager fragmentManager;

    private static final int DASHBOARD = 0;
    private static final int GROUPS = 1;
    private static final int ACCOUNT = 2;
    private static final int SETTINGS = 3;

    public static final String NEW_BILL = "newBill";
    public static final String NEW_GROUP = "newGroup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fetchContent();
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
                        new PrimaryDrawerItem().withIdentifier(SETTINGS).withName("Settings").withIcon(android.R.drawable.ic_menu_preferences).withSelectedIconColor(accent)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        switch ((int)drawerItem.getIdentifier()) {
                            case DASHBOARD:
                                Log.d(TAG, "dashboard");
                                fragmentTransaction.replace(R.id.fragmentLayout, new DashboardFragment());
                                break;
                            case GROUPS:
                                Log.d(TAG, "groups");
                                fragmentTransaction.replace(R.id.fragmentLayout, new GroupsFragment());
                                break;
                            case ACCOUNT:
                                Log.d(TAG, "account");
                                fragmentTransaction.replace(R.id.fragmentLayout, new AccountFragment());
                                break;
                            case SETTINGS:
                                Log.d(TAG, "settings");
                                fragmentTransaction.replace(R.id.fragmentLayout, new SettingsFragment());
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

        fragmentManager.beginTransaction().add(R.id.fragmentLayout, new DashboardFragment()).commit();
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

    /*private void fetchContent() {
        String authToken = BillMeApi.getAuthToken(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.bill_recycler_view);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        billObserver = (BillObserver) getLastNonConfigurationInstance();

        if (billObserver == null) {
            billObserver = new BillObserver();
            billAdapter = new BillAdapter(billObserver.getBills());
            billObserver.bind(this);

            BillMeApi.get()
                    .getBills(authToken)
                    .flatMap(new Func1<List<Bill>, Observable<Bill>>() {
                        @Override
                        public Observable<Bill> call(List<Bill> bills) {
                            return Observable.from(bills);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(billObserver);
        } else {
            billAdapter = new BillAdapter(billObserver.getBills());
            billObserver.bind(this);
        }

        recyclerView.setAdapter(billAdapter);
    }*/

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        billObserver.unbind();
        return billObserver;
    }

    private static class BillObserver implements Observer<Bill> {
        private MainActivity mActivity;

        private ArrayList<Bill> bills = new ArrayList<>();

        private void bind(MainActivity activity) {
            mActivity = activity;
        }

        private void unbind() {
            mActivity = null;
        }

        public ArrayList<Bill> getBills() {
            return bills;
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            HttpException httpErr = (HttpException) e;

            if (httpErr.code() == 403) {
                // The user has an invalid/expired token, make them log in.
                Intent intent = new Intent(mActivity.getApplicationContext(), LoginActivity.class);
                mActivity.startActivity(intent);
            } else {
                Log.e("BillObserver", e.getMessage());
            }
        }

        @Override
        public void onNext(Bill bill) {
            int index = bills.size();
            bills.add(bill);
            mActivity.billAdapter.notifyItemInserted(index);
        }
    }
}