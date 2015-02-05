package cn.wehax.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import cn.wehax.common.R;

/**
 * Created by howe on 15/2/5.
 * Email:howejee@gmail.com
 */
public class EnvironmentUtil {


    public static boolean isSDCardAvailability(Context context){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            Toast.makeText(context, R.string.sdcard_not_exist,Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
