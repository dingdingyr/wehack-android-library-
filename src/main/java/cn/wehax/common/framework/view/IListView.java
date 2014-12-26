package cn.wehax.common.framework.view;

import android.widget.BaseAdapter;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IListView extends IContentView {
    void setAdapter(BaseAdapter adapter);
}
