package cn.wehax.common.image;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;

import cn.wehax.util.ImageUtil;
import cn.wehax.util.StringUtil;

/**
 * Created by dss on 2014/12/13
 */
@Singleton
public class ImageManager implements IImageManager {
    private ImageLoader mImageLoader;

    private CircleImageLoader mCircleImageLoader;

    private int CACHE_MAX_SIZE = 10 * 1024 * 1024;

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
            ImageLoader.ImageCache imageCache = localBitmapCache;
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            mImageLoader = new ImageLoader(requestQueue, imageCache);
        }

        return mImageLoader;
    }

    public CircleImageLoader getCircleImageLoader() {
        if (mCircleImageLoader == null) {
            ImageLoader.ImageCache imageCache = localBitmapCache;
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            mCircleImageLoader = new CircleImageLoader(requestQueue, imageCache);
        }
        return mCircleImageLoader;
    }


    public int clearImageCache() {
        int clearSize = localBitmapCache.size;
        localBitmapCache.clear();
        return clearSize / 1024;
    }

    public int getImageCacheSize() {
        return localBitmapCache.size / 1024;
    }

    /**
     * ImageListener默认实现之一。
     * （1）Showing a default image until the network response is received, at which point
     * it will switch to either the actual image or the error image.
     * （2）根据指定的图片最大高宽，调整下载图片尺寸
     *
     * @param view              The imageView that the listener is associated with.
     * @param defaultImageResId Default image resource ID to use, or 0 if it doesn't exist.
     * @param errorImageResId   Error image resource ID to use, or 0 if it doesn't exist.
     */
    public ImageLoader.ImageListener getImageListener(final ImageView view, final ImageView border,
                                                      final int defaultImageResId, final int errorImageResId, final int maxHeight, final int maxWidth) {
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                try {
                    if (response.getBitmap() == null) {
                        throw new Exception("getBitmap == null");
                    }

                    Bitmap bmp = adjustImageSize(response.getBitmap(), maxHeight, maxWidth);
                    if (bmp == null) {
                        throw new Exception("getBitmap == null");
                    }

                    view.setLayoutParams(new ImageSwitcher.LayoutParams(bmp.getWidth(), bmp.getHeight()));
                    border.setLayoutParams(new ImageSwitcher.LayoutParams(bmp.getWidth(), bmp.getHeight()));
                    view.setImageBitmap(bmp);
                } catch (Exception e) {
                    if (defaultImageResId != 0) {
                        view.setImageResource(defaultImageResId);
                    }
                }
            }
        };
    }


    /**
     * 本地图片缓存
     */
    LruMemoryCache localBitmapCache = new LruMemoryCache(CACHE_MAX_SIZE);

    /**
     * 获取本地图片
     * 1.先读缓存
     * 2.读本地图片（根据指定的图片长宽最大值，将图片缩放至合理大小，并缓存图片）
     *
     * @param path      本地图片路径，同时也是获取缓存的键值
     * @param maxHeight 图片最大高度
     * @param maxWidth  图片最大宽度
     * @return
     */
    public Bitmap getLocalBitmap(String path, int maxHeight, int maxWidth) {
        try {
            // 如果已缓存直接返回缓存
            Bitmap bmp = localBitmapCache.getBitmap(path);
            if (bmp != null) {
                return bmp;
            }

            // 未缓存，读取本地图片
            bmp = readLocalBitmap(path);
            if (bmp == null) {
                return null;
            }

            // 缩放图片至合理大小
            bmp = adjustImageSize(bmp, maxHeight, maxWidth);
            if (bmp == null) {
                return null;
            }

            // 缓存
            localBitmapCache.putBitmap(path, bmp);

            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取本地图片
     * 先读缓存，后本地
     *
     * @param path 本地图片路径，同时也是获取缓存的键值
     * @return
     */
    public Bitmap getLocalBitmap(String path) {
        try {
            // 如果已缓存直接返回缓存
            Bitmap bmp = localBitmapCache.getBitmap(path);
            if (bmp != null) {
                return bmp;
            }

            // 未缓存，读取本地图片
            bmp = readLocalBitmap(path);
            if (bmp == null) {
                return null;
            }

            // 缓存
            localBitmapCache.putBitmap(path, bmp);

            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 调整图片尺寸
     *
     * @param bmp
     * @param maxHeight
     * @param maxWidth
     * @return
     */
    private Bitmap adjustImageSize(Bitmap bmp, int maxHeight, int maxWidth) {
        if (bmp == null) {
            return null;
        }

        int bmpHeight = bmp.getHeight();
        int bmpWidth = bmp.getWidth();

        // 如果图片不需要缩放，直接返回原图
        if (bmpHeight <= maxHeight && bmpWidth <= maxWidth) {
            return bmp;
        }

        float bmpRatio = (float) bmpWidth / bmpHeight; // 图片宽高比
        float scaleHeight = 0f; // 图片缩放后高度
        float scaleWidth = 0f; // 图片缩放后宽度

        // 根据指定的图片最大宽高，计算图片合适的缩放尺寸
        if (maxHeight == maxWidth) {
            if (bmpRatio == 1) { // 原图是正方形
                scaleWidth = scaleHeight = maxHeight;
            } else if (bmpRatio > 1) { // 原图宽大于高
                scaleWidth = maxHeight;
                scaleHeight = maxHeight / bmpRatio;
            } else { // 原图高大于宽
                scaleWidth = maxHeight * bmpRatio;
                scaleHeight = maxHeight;
            }
            scaleWidth /= bmpWidth;
            scaleHeight /= bmpHeight;
        } else {
            // TODO 处理maxLength!=maxWidth情况

        }

        return ImageUtil.scaleImage(bmp, scaleWidth, scaleHeight);
    }

    /**
     * 获取本地图片
     *
     * @param path
     * @return
     */
    private Bitmap readLocalBitmap(String path) {
        if (StringUtil.isNullOrEmpty(path))
            return null;

        if (!new File(path).exists()) {
            return null;
        }

        return BitmapFactory.decodeFile(path);
    }

}
