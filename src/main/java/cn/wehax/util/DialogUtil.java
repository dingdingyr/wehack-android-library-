package cn.wehax.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import cn.wehax.common.R;
import cn.wehax.common.widget.ConfirmDialog;


/**
 * Created by mayuhan on 14/12/13.
 */
public class DialogUtil {
    public static void showSingleChoiceListDialog(Context context, String title, String[] items, DialogInterface.OnClickListener onItemClickCallBack) {
        Dialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(items, onItemClickCallBack)
                .create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public static void showConfirmDialog(final Context context,
                                         String title,
                                         String msg,
                                         DialogInterface.OnClickListener positive,
                                         DialogInterface.OnClickListener negative,boolean cancelled) {

        ConfirmDialog dialog = new ConfirmDialog(context, R.style.filter_dialog_style);
        dialog.setTitle(title);
        dialog.setMsg(msg);
        dialog.setPositiveListener(positive);
        dialog.setCanceledOnTouchOutside(cancelled);
        dialog.setCancelable(cancelled);
        if(negative == null){
            dialog.setNegativeListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else{
            dialog.setNegativeListener(negative);
        }

        dialog.show();
    }

    public static void showConfirmDialog(Context context,
                                         int titleRes,
                                         int msgRes,
                                         DialogInterface.OnClickListener positive,
                                         DialogInterface.OnClickListener negative,boolean cancelled) {
        showConfirmDialog(context, context.getString(titleRes), context.getString(msgRes), positive, negative,cancelled);
    }
}
