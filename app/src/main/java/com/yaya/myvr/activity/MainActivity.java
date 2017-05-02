package com.yaya.myvr.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.fragment.BrandFragment;
import com.yaya.myvr.fragment.FindFragment;
import com.yaya.myvr.fragment.HomeFragment;
import com.yaya.myvr.fragment.MineFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    @BindView(R.id.tv_find)
    TextView tvFind;
    @BindView(R.id.tv_mine)
    TextView tvMine;

    private static final String CURRENT_ID = "currentId";
    private int currentId;
    private HomeFragment homeFragment;
    private BrandFragment brandFragment;
    private FindFragment findFragment;
    private MineFragment mineFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFragment(savedInstanceState);
    }

    // 初始化Fragment
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentId = R.id.tv_home;
        if (savedInstanceState != null) {
            currentId = savedInstanceState.getInt(CURRENT_ID);
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
            brandFragment = (BrandFragment) getSupportFragmentManager().findFragmentByTag("brand");
            findFragment = (FindFragment) getSupportFragmentManager().findFragmentByTag("find");
            mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("mine");
        } else {
            homeFragment = new HomeFragment();
            brandFragment = new BrandFragment();
            findFragment = new FindFragment();
            mineFragment = new MineFragment();
            transaction.add(R.id.fl_main, homeFragment, "home")
                    .add(R.id.fl_main, brandFragment, "brand")
                    .add(R.id.fl_main, findFragment, "find")
                    .add(R.id.fl_main, mineFragment, "mine")
                    .commit();
        }
        setBottomNavSelected(currentId);
        switchFragmentTo(currentId);
    }

    /**
     * 切换Fragment
     *
     * @param id
     */
    private void switchFragmentTo(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.tv_home:
                transaction.show(homeFragment)
                        .hide(brandFragment)
                        .hide(findFragment)
                        .hide(mineFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.tv_brand:
                transaction.show(brandFragment)
                        .hide(homeFragment)
                        .hide(findFragment)
                        .hide(mineFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.tv_find:
                transaction.show(findFragment)
                        .hide(homeFragment)
                        .hide(brandFragment)
                        .hide(mineFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.tv_mine:
                transaction.show(mineFragment)
                        .hide(homeFragment)
                        .hide(brandFragment)
                        .hide(findFragment)
                        .commitAllowingStateLoss();
                break;
        }
    }

    /**
     * 设置底部导航栏选中状态
     *
     * @param id
     */
    private void setBottomNavSelected(int id) {
        currentId = id;
        switch (id) {
            case R.id.tv_home:
                tvHome.setSelected(true);
                tvBrand.setSelected(false);
                tvFind.setSelected(false);
                tvMine.setSelected(false);
                break;
            case R.id.tv_brand:
                tvHome.setSelected(false);
                tvBrand.setSelected(true);
                tvFind.setSelected(false);
                tvMine.setSelected(false);
                break;
            case R.id.tv_find:
                tvHome.setSelected(false);
                tvBrand.setSelected(false);
                tvFind.setSelected(true);
                tvMine.setSelected(false);
                break;
            case R.id.tv_mine:
                tvHome.setSelected(false);
                tvBrand.setSelected(false);
                tvFind.setSelected(false);
                tvMine.setSelected(true);
                break;
        }
    }

    @OnClick(R.id.tv_home)
    void selectHome() {
        setBottomNavSelected(R.id.tv_home);
        switchFragmentTo(R.id.tv_home);
    }

    @OnClick(R.id.tv_brand)
    void selectBrand() {
        setBottomNavSelected(R.id.tv_brand);
        switchFragmentTo(R.id.tv_brand);
    }

    @OnClick(R.id.tv_find)
    void selectFind() {
        setBottomNavSelected(R.id.tv_find);
        switchFragmentTo(R.id.tv_find);
    }

    @OnClick(R.id.tv_mine)
    void selectMine() {
        setBottomNavSelected(R.id.tv_mine);
        switchFragmentTo(R.id.tv_mine);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CURRENT_ID, currentId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
