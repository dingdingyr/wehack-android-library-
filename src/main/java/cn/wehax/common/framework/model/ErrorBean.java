package cn.wehax.common.framework.model;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class ErrorBean implements IBaseBean{
    private final int code;

    private final String description;

    public ErrorBean(int code, String description){
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return "ErrorBean{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
