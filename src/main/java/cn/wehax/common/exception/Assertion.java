package cn.wehax.common.exception;

import cn.wehax.common.BuildConfig;

/**
 * Created by Terry on 14/11/30.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class Assertion {
    public static void assertThat(boolean bool) throws AssertionError{
        if(BuildConfig.DEBUG && !bool) throw new AssertionError();
    }
}
