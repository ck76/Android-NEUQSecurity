package cn.ck.security.utils;

import java.util.List;

/**
 * Created by ck on 2018/9/20.
 */
public class ListUtil {

    /**
     * 判空
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list){
        return list == null || list.isEmpty();
    }
}
