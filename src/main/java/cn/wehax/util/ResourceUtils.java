package cn.wehax.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import roboguice.util.Ln;

public class ResourceUtils {

    /**
     * 从assets 文件夹中获取文件并读取数据
     */
    public static String getTextFromAssets(final Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
            in.close();
        } catch (Exception e) {
            Ln.e("Assert:" + fileName);
            Ln.e(e);
        }
        return result;
    }

    /**
     * 拷贝assets文件到指定位置
     *
     * @param ctx
     * @param fileName 文件名（必须是assets中的文件）
     * @param targetPath 目标位置
     * @return
     */
    public static boolean copyAssetFile(final Context ctx, String fileName, String targetPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = ctx.getAssets().open(fileName);
            out = new FileOutputStream(targetPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (Exception ex) {
            Ln.e(ex);
            return false;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {

            }
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {

            }
        }
        return true;
    }

    public static Drawable loadImageFromAsserts(final Context ctx, String fileName) {
        try {
            InputStream is = ctx.getResources().getAssets().open(fileName);
            return Drawable.createFromStream(is, null);
        } catch (IOException e) {
            if (e != null) {
                Ln.e("Assert:" + fileName);
                Ln.e(e);
            }
        } catch (OutOfMemoryError e) {
            if (e != null) {
                Ln.e("Assert:" + fileName);
                Ln.e(e);
            }
        } catch (Exception e) {
            if (e != null) {
                Ln.e("Assert:" + fileName);
                Ln.e(e);
            }
        }
        return null;
    }

    /**
     * 从Asset从加载图片
     */
    public static void loadImageFromAsserts(final Context ctx, ImageView view, String fileName) {
        try {
            if (ctx != null && !StringUtil.isNullOrEmpty(fileName)) {
                InputStream is = ctx.getResources().getAssets().open(fileName);
                view.setImageDrawable(Drawable.createFromStream(is, null));
            }
        } catch (IOException e) {
            if (e != null) {
                Ln.e("Assert:" + fileName);
                Ln.e(e);
            }
        } catch (OutOfMemoryError e) {
            if (e != null) {
                Ln.e("Assert:" + fileName);
                Ln.e(e);
            }
        } catch (Exception e) {
            if (e != null) {
                Ln.e("Assert:" + fileName);
                Ln.e(e);
            }
        }
    }
}
