package cn.wehax.common.volley;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
@Singleton
public class RequestManager {

    Application mApplication;
    private RequestQueue mRequestQueue;

    @Inject
    RequestManager(Provider<Application> provider){
        mApplication = provider.get();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mApplication);
        }
        return mRequestQueue;
    }
}
