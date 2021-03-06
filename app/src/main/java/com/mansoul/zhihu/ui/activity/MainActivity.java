package com.mansoul.zhihu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.mansoul.zhihu.R;
import com.mansoul.zhihu.ui.fragment.LeftMenuFragment;
import com.mansoul.zhihu.ui.fragment.MainNewsFragment;
import com.mansoul.zhihu.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG_MENU = "FRAGMENT_TAG_MENU";
    private static final String FRAGMENT_TAG_MAIN = "FRAGMENT_TAG_MAIN";

    @BindView(R.id.fl_menu)
    FrameLayout fl_menu;
    @BindView(R.id.fl_main)
    FrameLayout fl_main;

    public Toolbar mToolbar;
    public DrawerLayout mDrawer;

    private MainNewsFragment mainNewsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化toolbar,导航栏
        initView();

        initFragment();

    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction(); //开启事务
        transaction.replace(R.id.fl_menu, new LeftMenuFragment(), FRAGMENT_TAG_MENU); //用fragment替换布局
        transaction.replace(R.id.fl_main, openMainNewsFragment(), FRAGMENT_TAG_MAIN);
        transaction.commit();
    }

    public MainNewsFragment openMainNewsFragment() {
        if (mainNewsFragment == null) {
            LogUtils.i("新建了MainNewsFragment");
            mainNewsFragment = new MainNewsFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_main, mainNewsFragment);
        transaction.commit();
        return mainNewsFragment;
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("首页");
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public interface FragmentBackListener {

        void onBackForward();
    }


    public void setOnBackListener(FragmentBackListener mBackListener) {
        this.mBackListener = mBackListener;
    }

    public boolean isInterception() {
        return isInterception;
    }

    public void setInterception(boolean isInterception) {
        this.isInterception = isInterception;
    }

    private FragmentBackListener mBackListener;
    private boolean isInterception = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isInterception()) {
                if (mBackListener != null) {
                    mBackListener.onBackForward();
                    return false;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
