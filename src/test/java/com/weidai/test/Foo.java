package com.weidai.test;

import java.util.List;
import java.util.Map;

/**
 * @author wuqi 2017/9/20 0020.
 */
public interface Foo {
    <T> Map<T[][], List<Integer[]>> test(Map<String, List<String>> var1, String[][] var2, List<T> var3, T[][] var5, String[]... varArg);
}
