package com.example.administrator.secondlogin.activities;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.secondlogin.R;
import com.example.administrator.secondlogin.fragments.BaseFragment;
import com.example.administrator.secondlogin.fragments.HomeFragment;
import com.example.administrator.secondlogin.fragments.NewsFragment;
import com.example.administrator.secondlogin.fragments.ProfileFragment;
import com.example.administrator.secondlogin.fragments.fgm_cart;
import com.example.administrator.secondlogin.fragments.fgm_comment;
import com.example.administrator.secondlogin.fragments.fgm_credit;
import com.example.administrator.secondlogin.utils.FragmentHistory;
import com.example.administrator.secondlogin.utils.Utils;
import com.example.administrator.secondlogin.views.FragNavController;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    private FirebaseAuth mAuth;
    private boolean islogin = false;
    FirebaseUser currentUser;
    TextView mTextView;
    @BindView(R.id.content_frame)
    FrameLayout contentFrame;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MenuItem mMenuItem;

    private int[] mTabIconsSelected = {
            R.drawable.tab_home,
            R.drawable.tab_search,
            R.drawable.tab_share,
            R.drawable.tab_news,
            R.drawable.tab_profile};


    @BindArray(R.array.tab_name)
    String[] TABS;

    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;

    private FragNavController mNavController;

    private FragmentHistory fragmentHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
        mAuth = FirebaseAuth.getInstance();
        initTab();

        fragmentHistory = new FragmentHistory();
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
                .transactionListener(this)
                .rootFragmentListener(this, TABS.length)
                .build();
        switchTab(0);

        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentHistory.push(tab.getPosition());
                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                mNavController.clearStack();

                switchTab(tab.getPosition());
            }
        });

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.xml.main_menu, menu);
        mMenuItem = menu.findItem(R.id.menu_login_text);
        return true;
    }



    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initTab() {
        if (bottomTabLayout != null) {
            for (int i = 0; i < TABS.length; i++) {
                bottomTabLayout.addTab(bottomTabLayout.newTab());
                TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(getTabView(i));
            }
        }
    }


    private View getTabView(int position) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageDrawable(Utils.setDrawableSelector(MainActivity.this, mTabIconsSelected[position], mTabIconsSelected[position]));
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
   //     updateUI(currentUser);
    }

    @Override
    public void onStop() {

        super.onStop();
    }


    private void switchTab(int position) {
        mNavController.switchTab(position);


//        updateToolbarTitle(position);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_login_text:

                return true;

            case R.id.menu_login_icon:
            login();
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    private void login() {
        if (islogin == false) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {

            AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog);

            oDialog.setMessage("로그아웃 하시겠습니까?");
            oDialog.setTitle("일반 Dialog");

            oDialog.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            oDialog.setNeutralButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();

                    updateUI(null);
                }
            });
            oDialog.setCancelable(false);
            oDialog.show();

        }
    }


    private void updateUI(FirebaseUser user) {
// hideProgressDialog();
    //    MenuItem menuItem = mMenuItem.findItem(R.id.menu_login_text);
        if (user != null) {

            String email = user.getEmail();
            if(TextUtils.isEmpty(email)) {
            //    mTextView.setText(user.getDisplayName() + "님");
                mMenuItem.setTitle(user.getDisplayName() + "님");
            }
            else {
         //       mTextView.setText(user.getEmail() + "님");
                mMenuItem.setTitle(user.getEmail() + "님");
            }
            islogin = true;
        } else {
            islogin=false;
         //   mTextView.setText("로그인");
            mMenuItem.setTitle("로그인하세요");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentUser != null) {

            String email = currentUser.getEmail();
            if(TextUtils.isEmpty(email)) {
                //    mTextView.setText(user.getDisplayName() + "님");
                menu.findItem(R.id.menu_login_text).setTitle(currentUser.getDisplayName() + "님");
            }
            else {
                //       mTextView.setText(user.getEmail() + "님");
                menu.findItem(R.id.menu_login_text).setTitle(currentUser.getEmail() + "님");
            }
            islogin = true;
        } else {
            islogin=false;
            //   mTextView.setText("로그인");
            menu.findItem(R.id.menu_login_text).setTitle("로그인하세요");
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {


                if (fragmentHistory.getStackSize() > 1) {

                    int position = fragmentHistory.popPrevious();

                    switchTab(position);

                    updateTabSelection(position);

                } else {

                    switchTab(0);

                    updateTabSelection(0);

                    fragmentHistory.emptyStack();
                }
            }

        }
    }


    private void updateTabSelection(int currentTab){

        for (int i = 0; i <  TABS.length; i++) {
            TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
            if(currentTab != i) {
                selectedTab.getCustomView().setSelected(false);
            }else{
                selectedTab.getCustomView().setSelected(true);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }


    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {

            switch (index) {

                case FragNavController.TAB1:
                    System.out.println("TAB:0");
                    break;
                case FragNavController.TAB2:
                    fragment.requireFragmentManager().beginTransaction().replace(R.id.content_frame,new fgm_comment()).addToBackStack(null).commit();
                    break;
                case FragNavController.TAB3:
                    System.out.println("TAB:2");
                    fragment.requireFragmentManager().beginTransaction().replace(R.id.content_frame,new fgm_cart()).addToBackStack(null).commit();
                    break;
                case FragNavController.TAB4:
                    System.out.println("TAB:3");
                    fragment.requireFragmentManager().beginTransaction().replace(R.id.content_frame,new fgm_credit()).addToBackStack(null).commit();
                    break;
                case FragNavController.TAB5:
                    System.out.println("TAB:4");
                    break;


            }


            updateToolbar();

        }
    }

    private void updateToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setDisplayShowHomeEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }


    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {

            updateToolbar();

        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {

            case FragNavController.TAB1:
                return new HomeFragment();
            case FragNavController.TAB2:
                return new fgm_comment();
            case FragNavController.TAB3:
                return new fgm_cart();
            case FragNavController.TAB4:
                return new fgm_credit();
            case FragNavController.TAB5:
                return new NewsFragment();


        }
        throw new IllegalStateException("Need to send an index that we know");
    }


//    private void updateToolbarTitle(int position){
//
//
//        getSupportActionBar().setTitle(TABS[position]);
//
//    }


    public void updateToolbarTitle(String title) {


        getSupportActionBar().setTitle(title);

    }

}