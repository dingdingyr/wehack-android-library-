package cn.wehax.common.framework.data;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class DataStrategy {
    public static final int CACHE_POLICY_CACHE_ONLY = 0x100; // 仅本地缓存
    public static final int CACHE_POLICY_NETWORK_ONLY = CACHE_POLICY_CACHE_ONLY + 1; // 仅远程
    public static final int CACHE_POLICY_CACHE_THEN_NETWORK = CACHE_POLICY_NETWORK_ONLY + 1; // 先本地缓存后远程
    public static final int CACHE_POLICY_CACHE_ELSE_NETWORK = CACHE_POLICY_CACHE_THEN_NETWORK + 1; // 本地缓存优先（如果本地缓存数据存在直接使用本地缓存数据，否则使用远程）
    public static final int CACHE_POLICY_NETWORK_ELSE_CACHE = CACHE_POLICY_CACHE_ELSE_NETWORK + 1; // 远程优先（如果能够读取远程服务器数据直接使用远程服务器数据，否则使用本地缓存数据）
}
