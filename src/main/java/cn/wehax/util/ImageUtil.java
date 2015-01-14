package cn.wehax.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.wehax.common.R;

/**
 * Created by howe on 14/12/18.
 * Email:howejee@gmail.com
 */
public class ImageUtil {

    public static String getLocalImagePath(Context context){
        File file = new File(Environment.getExternalStorageDirectory()+"/android/data/"+context.getPackageName()+"/photo");
        if(!file.exists()){
            file.mkdirs();
        }
        return file.getPath();
    }

    public static String getImageName(int index){
        return "img_"+index+".png";
    }

    public static void chooseImage(Activity activity,int requestCode){
        chooseImage(activity,requestCode,false);
    }

    public static void chooseImage(Activity activity,int requestCode,boolean isModifyImage){
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.setType("image/*");
        if(isModifyImage) {
            initModifyImageConfig(pickIntent);
        }


        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(isModifyImage) {
            initModifyImageConfig(takePhotoIntent);
        }

        Intent chooserIntent = Intent.createChooser(
                pickIntent, activity.getString(R.string.title_choose_photo_source));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
        activity.startActivityForResult(chooserIntent, requestCode);
    }

    private static void initModifyImageConfig(final Intent intent){
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
    }

    public static Bitmap  doImageFromPick(Activity activity,Intent data,String imagePath){
        ContentResolver resolver = activity.getContentResolver();
        Bitmap bitmap = null;
        try {
            Uri uri = data.getData();
            byte[] mContent = readStream(resolver.openInputStream(Uri.parse(uri.toString())));
            if (mContent == null) {
                return null;
            }
            bitmap = ImageUtil.getSmallBitmap(mContent, 480, 800);
            saveImage(bitmap,imagePath);
        }catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap  doImageFromCamera(Intent data,String imagePath){
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        saveImage(bitmap,imagePath);
        return bitmap;
    }

    private static void  saveImage(Bitmap bitmap ,String imagePath){
        FileOutputStream out = null;
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }

            out = new FileOutputStream(file, false);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    out = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 头像文件名
     *
     * @param sid
     * @return
     */
    public static String createAvatarFileName(String sid) {
        return "avatar_" + sid + ".png";
    }


    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(byte[] bytes, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        //保证是方形，并且从中心画
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int w;
        int deltaX = 0;
        int deltaY = 0;
        if (width <= height) {
            w = width;
            deltaY = height - w;
        } else {
            w = height;
            deltaX = width - w;
        }
        final Rect rect = new Rect(deltaX, deltaY, w, w);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        //圆形，所有只用一个

        int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
