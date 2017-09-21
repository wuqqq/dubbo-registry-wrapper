/**
 * Copyright (C), 2011-2017, 微贷网.
 */
package com.weidai.test;

import java.util.List;
import java.util.Map;

/**
 * @author wuqi 2017/9/20 0020.
 */
public interface Foo {
    <T> Map<String[], List<Integer>> test(Map<String, List<String>> foo, String[] bar, List<T> var4, String... var3);
}
