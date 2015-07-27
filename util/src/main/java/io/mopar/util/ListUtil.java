package io.mopar.util;

import java.util.List;

/**
 * Created by hadyn on 7/26/2015.
 */
public class ListUtil {

    /**
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T first(List<T> list) {
        return list.get(0);
    }
}
