package cn.wehax.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.wehax.common.R;

/**
 * Created by mayuhan on 15/1/31.
 */
public class ConfirmDialog extends Dialog {

    public ConfirmDialog(Context context, int theme) {
        super(context, theme);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public ConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private Button positiveBtn;
    private Button negativeBtn;
    private View.OnClickListener positive;
    private View.OnClickListener negative;
    private TextView titleText;
    private TextView msgText;
    private String title;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dialog_layout);
        init();
    }

    public void setPositiveListener(final OnClickListener listener) {
        if (listener != null) {
            positive = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(ConfirmDialog.this, DialogInterface.BUTTON_POSITIVE);
                }
            };

        }

    }

    public void setNegativeListener(final OnClickListener listener) {
        if (listener != null) {
            negative = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(ConfirmDialog.this, DialogInterface.BUTTON_NEGATIVE);
                }
            };
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private void init() {
        positiveBtn = (Button) findViewById(R.id.positive_btn);
        negativeBtn = (Button) findViewById(R.id.negative_btn);
        titleText = (TextView) findViewById(R.id.confirm_dialog_title);
        msgText = (TextView) findViewById(R.id.confirm_dialog_msg);

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (title != null) {
                    titleText.setText(title);
                }
                if (msg != null) {
                    msgText.setText(msg);
                }

                if (positive != null) {
                    positiveBtn.setOnClickListener(positive);
                }

                if (negative != null) {
                    negativeBtn.setOnClickListener(negative);
                }
            }
        });
    }
}
