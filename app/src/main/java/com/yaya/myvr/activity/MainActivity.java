package com.yaya.myvr.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.fragment.BrandFragment;
import com.yaya.myvr.fragment.FindFragment;
import com.yaya.myvr.fragment.HomeFragment;
import com.yaya.myvr.fragment.MineFragment;
import com.yaya.myvr.util.LogUtils;

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
    private SparseArray<BaseFragment> fragments = new SparseArray<>();
    private int currId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.e(TAG, "onCreate....");
        super.onCreate(savedInstanceState);

        selectTab(R.id.tv_home);
    }

    /**
     * 选中某个Tab
     *
     * @param id
     */
    private void selectTab(int id) {
        if (id == currId) {
            return;
        }
        // 底部处理
        setBottomNavSelected(id);
        // Fragment处理
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 隐藏上一个Fragment
        for (int i = 0; i < fragments.size(); i++) {
            int key = fragments.keyAt(i);
            if (key == currId) {
                transaction.hide(fragments.valueAt(i));
            }
        }
        // 显示目的Fragment
        if (fragments.get(id) == null) {
            switch (id) {
                case R.id.tv_home:
                    fragments.put(id, new HomeFragment());
                    break;
                case R.id.tv_brand:
                    fragments.put(id, new BrandFragment());
                    break;
                case R.id.tv_find:
                    fragments.put(id, new FindFragment());
                    break;
                case R.id.tv_mine:
                    fragments.put(id, new MineFragment());
                    break;
            }
            transaction.add(R.id.fl_main, fragments.get(id));
        } else {
            transaction.show(fragments.get(id));
        }

        transaction.commit();
        currId = id;
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
        currId = savedInstanceState.getInt("position");
        selectTab(currId);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtils.e(TAG, "onSaveInstanceState...");
        // 记录当前的position
        outState.putInt("position", currId);
    }

}
