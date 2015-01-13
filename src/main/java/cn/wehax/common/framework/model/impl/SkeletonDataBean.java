package cn.wehax.common.framework.model.impl;

import com.j256.ormlite.field.DatabaseField;

import cn.wehax.common.framework.model.IDataBean;

/**
 * Created by howe on 15/1/12.
 * Email:howejee@gmail.com
 */
public abstract class SkeletonDataBean implements IDataBean {

    @DatabaseField
    protected boolean isComplete;

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }
}
