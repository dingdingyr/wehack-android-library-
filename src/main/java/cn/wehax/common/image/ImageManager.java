package cn.wehax.common.image;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import static cn.wehax.common.exception.Assertion.assertThat;

/**
 * Created by dss on 2014/12/13
 */
@Singleton
public class ImageManager implements IImageManager {
    private ImageLoader mImageLoader;

    private CircleImageLoader mCircleImageLoader;

    @Inject
    private Application context;

    /**
     * 返回一个有效的ImageLoader
     * 注：该ImageLoader类来自于Volley库
     *
     * @return
     */
    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            assertThat(context != null);
            ImageLoader.ImageCache imageCache = new LruMemoryCache();
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            mImageLoader = new ImageLoader(requestQueue, imageCache);
        }

        return mImageLoader;
    }

    public CircleImageLoader getmCircleImageLoader() {
        if (mCircleImageLoader == null) {
            assertThat(context != null);
            ImageLoader.ImageCache imageCache = new LruMemoryCache();
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            mCircleImageLoader = new CircleImageLoader(requestQueue, imageCache);
        }

        return mCircleImageLoader;
    }

}
