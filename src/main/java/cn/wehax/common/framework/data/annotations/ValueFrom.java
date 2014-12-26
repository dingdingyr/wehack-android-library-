package cn.wehax.common.framework.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Terry on 14/12/22.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueFrom {
    String dataKey();
}
