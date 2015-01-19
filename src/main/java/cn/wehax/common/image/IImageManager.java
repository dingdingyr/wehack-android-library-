package cn.wehax.common.image;

import com.android.volley.toolbox.ImageLoader;

public interface IImageManager {

    /**
     * 获取ImageLoader
     *
     * @return
     */
    public ImageLoader getImageLoader();

    /**
     * 获取圆形图loader
     * @return
     */
    public ImageLoader getCircleImageLoader();


}
