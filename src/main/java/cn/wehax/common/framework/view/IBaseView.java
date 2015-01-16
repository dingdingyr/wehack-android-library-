package cn.wehax.common.framework.view;

/**
 * Created by Terry on 14/12/10.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IBaseView {

    public void showErrorMessage(String err);

    public void showErrorMessage(int idRes);

    public void showWaitingDialog(String msg);

    public void showWaitingDialog(int idRes);

    public void hideWaitingDialog();


}
