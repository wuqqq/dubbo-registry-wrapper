package com.weidai.test;

import com.alibaba.dubbo.common.URL;
import com.weidai.dubbo.registry.wrapper.RegistryProtocolWrapper;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author wuqi 2017/9/15 0015.
 */
public class RegistryProtocolWrapperTest {

    @Test
    public void getMethodSignaturesTest() throws Exception {
        Method method = RegistryProtocolWrapper.class.getDeclaredMethod("getMethodSignatures", Class.class);
        method.setAccessible(true);
        System.out.println(method.invoke(null, Foo.class));
    }

    @Test
    public void test2() {
        String str = URL.decode("");
        String methodSignatures = URL.valueOf(str).getParameterAndDecoded(RegistryProtocolWrapper.METHOD_SIGNATURE);
        System.out.println(methodSignatures);
    }

    @Test
    public void test3(){
        Method method = Foo.class.getMethods()[0];
        System.out.println(method.toGenericString());
    }
}
