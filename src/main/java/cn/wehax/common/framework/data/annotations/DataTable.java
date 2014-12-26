package cn.wehax.common.framework.data.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTable {
    String name();
}
