package cn.wehax.common.container;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.wehax.common.view.impl.SkeletonBaseActivity;
import cn.wehax.library.R;
import cn.wehax.library.utils.DensityUtil;

/**
 * Created by mayuhan on 14/12/11.
 */
public class TitleFragmentContainer extends SkeletonBaseActivity {

    private final static int TITLE_HEIGHT_DP = 80;

    View mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleBar = getLayoutInflater().inflate(R.layout.title_bar,null);
        ViewGroup.LayoutParams lp;
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        if(rootView instanceof FrameLayout){
            lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtil.dp2px(this,TITLE_HEIGHT_DP));
            mTitleBar.setLayoutParams(lp);
            rootView.addView(mTitleBar,rootView.getChildCount(),lp);
        }
        else if(rootView instanceof LinearLayout){
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtil.dp2px(this,TITLE_HEIGHT_DP));
            mTitleBar.setLayoutParams(lp);
            rootView.addView(mTitleBar,0,lp);
        }
        else if(rootView instanceof RelativeLayout){
            //TODO
//            lp = new
        }
        else{


        }
    }

}
