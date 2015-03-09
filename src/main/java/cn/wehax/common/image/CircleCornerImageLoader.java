package cn.wehax.common.image;

import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import cn.wehax.util.ImageUtil;

/**
 * Created by mayuhan on 15/1/19.
 */
public class CircleCornerImageLoader extends ImageLoader {

    int radius = 10;
    public CircleCornerImageLoader(RequestQueue queue, ImageCache imageCache,int radius) {
        super(queue, imageCache);
        this.radius = radius;
    }



    @Override
    protected void onGetImageSuccess(String cacheKey, Bitmap response) {
        Bitmap circleBitmap = ImageUtil.getRoundedCornerBitmap(response,radius);
        super.onGetImageSuccess(cacheKey, circleBitmap);
    }
}
