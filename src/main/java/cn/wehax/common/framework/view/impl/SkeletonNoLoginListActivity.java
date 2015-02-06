package cn.wehax.common.framework.view.impl;

import android.os.Bundle;
import android.view.View;

import cn.wehax.common.R;
import cn.wehax.common.framework.view.IListView;

/**
 * Created by Terry on 14/12/19.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public abstract class SkeletonNoLoginListActivity extends SkeletonBaseActivity implements IListView {

    View loadingView;

    View reloadView;

    View contentView;


    protected abstract void onReloadData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingView = findViewById(R.id.load_view_loading);
        reloadView = findViewById(R.id.load_view_reload);
        contentView = findViewById(R.id.load_view_content);
        if(reloadView != null){
            reloadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onReloadData();
                }
            });
        }

    }

    @Override
    public void showLoadingView() {

        if(loadingView!= null){
            loadingView .setVisibility(View.VISIBLE);
        }
        if(reloadView != null){
            reloadView.setVisibility(View.GONE);
        }

        if(contentView != null){
            contentView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showContentView() {
        if(loadingView!= null){
            loadingView .setVisibility(View.GONE);
        }
        if(reloadView != null){
            reloadView.setVisibility(View.GONE);
        }

        if(contentView != null){
            contentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showReloadView() {
        if(loadingView!= null){
            loadingView .setVisibility(View.GONE);
        }
        if(reloadView != null){
            reloadView.setVisibility(View.VISIBLE);
        }

        if(contentView != null){
            contentView.setVisibility(View.GONE);
        }
    }

}
