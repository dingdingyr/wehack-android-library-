package cn.wehax.onefamily.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewStub;

import java.util.ArrayList;
import java.util.List;

import cn.wehax.library.R;

import static cn.wehax.library.Assertion.assertThat;

public abstract class BottomTabActivity extends BaseActivity implements IBottomTabView, View.OnClickListener {


    ViewStub bottomRadioGroup;


    List<BaseFragment> mainFragmentList;

    int totalFragmentCount = 0;

    int currentFragmentIndex = -1;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomRadioGroup = (ViewStub) findViewById(R.id.main_bottom_tab_container);

        fragmentManager = getSupportFragmentManager();

        mainFragmentList = new ArrayList<>();

        setupPresenter();

        initFragmentsAndTabs();


    }

    protected abstract void setupPresenter();

    @Override
    public int getCurrentTabIndex() {
        return currentFragmentIndex;
    }

    @Override
    public int getTotalTabIndex() {
        return totalFragmentCount;
    }

    @Override
    public void switchTab(int tabIndex) {


        assertThat(tabIndex >= 0 && tabIndex < totalFragmentCount);

        if (tabIndex == currentFragmentIndex)
            return;

        Fragment currentFragment = mainFragmentList.get(tabIndex);

        assertThat(currentFragment != null);
        fragmentManager.beginTransaction()
                .replace(R.id.main_content_container, currentFragment)
                .commit();

        currentFragmentIndex = tabIndex;


    }

    @Override
    public void addCenterFragment(BaseFragment fragment) {
        assertThat(fragment != null);

        mainFragmentList.add(fragment);

        totalFragmentCount++;
    }

    @Override
    public void setupBottomLayout(int resLayoutId, final OnBottomInflateComplete callback) {
        bottomRadioGroup.setLayoutResource(resLayoutId);
        bottomRadioGroup.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub viewStub, View view) {
                callback.setupClickListener(view, BottomTabActivity.this);

            }
        });
        bottomRadioGroup.inflate();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < mainFragmentList.size(); i++) {
            fragmentManager.putFragment(outState, String.valueOf(i), mainFragmentList.get(i));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //TODO: load fragments

    }

    @Override
    public BaseFragment getCenterFragmentAtIndex(int tabIndex) {
        assertThat(tabIndex >= 0 && tabIndex < totalFragmentCount);
        return mainFragmentList.get(tabIndex);

    }


    @Override
    public abstract void onClick(View view);

    protected abstract void initFragmentsAndTabs();
}
