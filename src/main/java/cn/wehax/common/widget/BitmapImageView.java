package cn.wehax.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.wehax.util.StringUtil;
import roboguice.util.Ln;


/**
 * 专门用来处理Bitmap类型的ImageView，onDetachedFromWindow时释放，防止内存泄露
 *
 * @author tangjun
 */
public class BitmapImageView extends ImageView {

    private Bitmap mBitmap;
    private String mPath;

    public BitmapImageView(Context context) {
        super(context);
    }

    public BitmapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImagePath(String path) {
        release();
        if (StringUtil.isNotEmpty(path)) {
            try {
                mBitmap = BitmapFactory.decodeFile(path);
                if (mBitmap != null && !mBitmap.isRecycled()) {
                    mPath = path;
                    setImageBitmap(mBitmap);
                }
            } catch (OutOfMemoryError e) {
                Ln.e(e);
            } catch (Exception e) {
                Ln.e(e);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (StringUtil.isNotEmpty(mPath)) {
            setImagePath(mPath);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    public void release() {
        if (mBitmap != null) {
            if (!mBitmap.isRecycled())
                mBitmap.recycle();
            mBitmap = null;
        }
    }
}