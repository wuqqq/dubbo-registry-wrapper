/**
 * Copyright (C), 2011-2017, 微贷网.
 */
package com.weidai.test;

import com.weidai.dubbo.registry.wrapper.RegistryProtocolWrapper;
import com.weidai.ucore.facade.api.UserServiceFacade;
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
        System.out.println(method.invoke(null, UserServiceFacade.class));
    }
}
