package cn.wehax.common.container;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.wehax.common.R;
import roboguice.activity.RoboFragmentActivity;

/**
 * A BaseActivity that simply contains a single fragment. The intent
 * used to invoke this activity is forwarded to the fragment as arguments during
 * fragment instantiation. Derived activities should only need to implement onCreatePane()
 */
public abstract class SinglePaneFragmentContainer extends RoboFragmentActivity {
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepane_empty);

        if (savedInstanceState == null) {
            mFragment = onCreatePane();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.root_container, mFragment)
                    .commit();
        }
    }

    /**
     * 获取onCreatePane方法传入的Single Pane
     * @return
     */
    protected Fragment getFragment(){
       return mFragment;
    }

    protected abstract Fragment onCreatePane();
}
