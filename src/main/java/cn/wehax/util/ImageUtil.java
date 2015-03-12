package cn.wehax.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.wehax.common.R;
import roboguice.util.Ln;

/**
 * Created by howe on 14/12/18.
 * Email:howejee@gmail.com
 */
public class ImageUtil {

    public static String getLocalImagePath(Context context) {
        File file = new File(Environment.getExternalStorageDirectory() + "/android/data/" + context.getPackageName() + "/photo");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    public static String getImageName(String name){
        return "img_" + name + ".png";
    }

    public static String getImageName(int index) {
        return "img_" + index + ".png";
    }

    /**
     * 用当前时间给取得的图片命名
     *
     */
    public static String getImageNameWithTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
        return dateFormat.format(date) + ".jpg";
    }


    public static void moveToImageCrop(Activity activity,Uri uri,int requestCode){
        Intent intent;
        intent = new Intent("com.android.camera.action.CROP");// 打开图片裁减工具
        if (uri != null) {
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("return-data", true);
        }
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 处理从相册获取的图片
     * @param activity
     * @param uri
     * @param imagePath
     * @return
     */
    public static Bitmap doImageFromPick(Activity activity, Uri uri, String imagePath) {

        Bitmap bitmap = null;

        try {

            byte[] array = getImageByteArrayFromUri(activity,uri);
            Ln.e("PHOTO 2.Albums array="+array);
            if (array == null) {
                return null;
            }
            bitmap = convertBitmapFromByteArray(array, 480, 800);

            Ln.e("PHOTO 2.Albums bitmap="+bitmap);

            saveImage(bitmap, imagePath);
        } catch (Exception e) {
            e.printStackTrace();
            Ln.e("PHOTO Albums Exception="+e.toString());
        }

        return bitmap;
    }


    /**
     * 通过Uri获取图片真实路径
     * @param activity
     * @param uri
     * @param dir
     * @return
     */
    public static String getImagePathFromUri(Activity activity,Uri uri,String dir) {
        String path = "";

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        try {

            if (isKitKat && DocumentsContract.isDocumentUri(activity, uri)) {
                path = getImagePathFromUriInHeightApi(activity, uri);
            } else {
                path = getImagePathFromUriInLowApi(activity, uri);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if(TextUtils.isEmpty(path)){
            path = createImagePathBySave(activity,uri,dir);
        }
        return path;
    }

    private static String getImagePathFromUriInHeightApi(Activity activity,Uri uri)
            throws Exception{
        String path = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                new String[] { id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            path = cursor.getString(columnIndex);
        }

        cursor.close();

        return path;
    }

    private static String getImagePathFromUriInLowApi(Activity activity,Uri uri)
            throws Exception{

        Cursor cursor = null;
        String path = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};

            cursor = activity.getContentResolver().query(uri,
                    projection, null, null, null);

            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            path = cursor.getString(column_index);
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 另存一份uri所指向的图片，并返回新图片路径
     * @param activity
     * @param uri 
     * @param dir 需要另存的图片所在目录，不含图片名称
     * @return
     */
    private static String createImagePathBySave(Activity activity,Uri uri,String dir){
        try{

            byte[] array = getImageByteArrayFromUri(activity,uri);

            if (array == null) {
                return null;
            }
            Bitmap bitmap = convertBitmapFromByteArray(array, 480, 800);

            if(bitmap != null){
                //通过图片MD5值来区分,避免重复保存图片
                String md5 = MD5Util.getFileMD5String(array);

                StringBuffer path = new StringBuffer(dir);
                path.append("/");
                path.append(md5);
                path.append(".png");

                saveImage(bitmap,path.toString());

                bitmap.recycle();

                return path.toString();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getImageByteArrayFromUri(Activity activity,Uri uri) throws Exception{

        ContentResolver resolver = activity.getContentResolver();

        return readByteArryFromStream(resolver.openInputStream(uri));
    }


    /**
     * 处理充相机获取的图片
     * @param data
     * @param imagePath
     * @return
     */
    public static Bitmap doImageFromCamera(Intent data, String imagePath) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        Ln.e("PHOTO 1.photo    uri=" + data.getData() + " path=" + imagePath);
        saveImage(bitmap, imagePath);
        return bitmap;
    }

    public static boolean saveImage(Bitmap bitmap, String imagePath) {
        FileOutputStream out = null;

        try {
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }

            out = new FileOutputStream(file, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            Ln.e("PHOTO save success:"+imagePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Ln.e("PHOTO saveImage   Execption=" +e.toString());
            return false;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
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


    public static byte[] readByteArryFromStream(InputStream inStream) throws Exception {
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
    public static Bitmap convertBitmapFromByteArray(byte[] bytes, int reqWidth, int reqHeight) {
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

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap){
        return getRoundedCornerBitmap(bitmap,10);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int radius) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //得到画布
        Canvas canvas = new Canvas(output);
        //将画布的四角圆化
        final int color = Color.RED;
        final Paint paint = new Paint();
        //得到与图像相同大小的区域 由构造的四个值决定区域的位置以及大小
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        }
        //值越大角度越明显
        final float roundPx = radius;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
        canvas.drawRoundRect(rectF, roundPx,roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    /**
     * 根据原图生成圆形图，要求SDK 18，若低于18会有明显锯齿
     *
     * @param bitmap
     * @return
     */
    public static Bitmap toOvalBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        }
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return output;
    }

    /**
     * 缩放图片
     *
     * @param org
     * @param scaleWidth  宽度缩放比例 (targetWidth/srcWidth,targetWidth=目标图片宽度，srcWidth=原始图片宽度）
     * @param scaleHeight 高度缩放比例
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // TODO org手动释放？
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

}
