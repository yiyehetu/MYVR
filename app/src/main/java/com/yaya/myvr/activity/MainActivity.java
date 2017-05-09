package com.yaya.myvr.activity;

import android.os.Bundle;
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

    private static final String TAG = MainActivity.class.getSimpleName();
    private HomeFragment homeFragment;
    private BrandFragment brandFragment;
    private FindFragment findFragment;
    private MineFragment mineFragment;

    private int currPositon = -1;

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

        selectTab(R.id.tv_home);
    }

    /**
     * 选中某个Tab
     *
     * @param id
     */
    private void selectTab(int id) {
        // 底部处理
        setBottomNavSelected(id);
        // Fragment处理
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (id) {
            case R.id.tv_home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_main, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case R.id.tv_brand:
                if (brandFragment == null) {
                    brandFragment = new BrandFragment();
                    transaction.add(R.id.fl_main, brandFragment);
                } else {
                    transaction.show(brandFragment);
                }
                break;
            case R.id.tv_find:
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    transaction.add(R.id.fl_main, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case R.id.tv_mine:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.fl_main, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                break;
        }
        transaction.commit();
        currPositon = id;
    }

    /**
     * 隐藏所有fragment
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (brandFragment != null) {
            transaction.hide(brandFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    /**
     * 设置底部导航栏选中状态
     *
     * @param id
     */
    private void setBottomNavSelected(int id) {
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
        selectTab(R.id.tv_home);
    }

    @OnClick(R.id.tv_brand)
    void selectBrand() {
        selectTab(R.id.tv_brand);
    }

    @OnClick(R.id.tv_find)
    void selectFind() {
        selectTab(R.id.tv_find);
    }

    @OnClick(R.id.tv_mine)
    void selectMine() {
        selectTab(R.id.tv_mine);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        LogUtils.e(TAG, "onRestoreInstanceState...");
        currPositon = savedInstanceState.getInt("position");
        selectTab(currPositon);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        LogUtils.e(TAG, "onSaveInstanceState...");
        // 记录当前的position
        outState.putInt("position", currPositon);
    }
}
