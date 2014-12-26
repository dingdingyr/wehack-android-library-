package cn.wehax.common.view;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface INeedLoginView extends IBaseView {

    void showNeedLogin();

    void hideNeedLogin();

    void moveToChooseLoginView(int requestCode);
}
