package cn.wehax.common.framework.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.wehax.common.framework.model.IBaseBean;

/**
 * Created by Terry on 14/12/12.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class GenericAdapter<T extends IBaseBean> extends BaseAdapter {

    private final IRenderer<T> renderer;
    List<T> data;

    public GenericAdapter(List<T> data, IRenderer<T> renderer) {
        this.data = data;
        this.renderer = renderer;
        assert (data != null);
        assert (renderer != null);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return renderer.getItemViewType(data.get(position), position);
    }

    @Override
    public int getViewTypeCount() {
        return renderer.getViewTypeCount();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            if (i < 0 || i >= data.size())
                throw new IndexOutOfBoundsException();
            view = renderer.bind(i, data.get(i), view, viewGroup);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return view;

    }


}
