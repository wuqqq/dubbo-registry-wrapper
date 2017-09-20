/**
 * Copyright (C), 2011-2017, 微贷网.
 */
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
        String str = URL.decode(
                "dubbo%3A%2F%2F192.168.3.228%3A20880%2Fcom.weidai.ucore.facade.api.TenderInfoServiceFacade%3Fanyhost%3Dtrue%26application%3Ducore-test%26default.cluster%3Dfailfast%26default.retries%3D0%26default.timeout%3D10000%26dubbo%3D2.5.3%26interface%3Dcom.weidai.ucore.facade.api.TenderInfoServiceFacade%26logger%3Dslf4j%26methodSignatures%3D%255B%257B%2522methodName%2522%253A%2522save%2522%252C%2522paramTypes%2522%253A%255B%2522com.weidai.ucore.facade.domain.TenderInfoDO%2522%255D%252C%2522returnType%2522%253A%2522com.weidai.ucore.facade.dto.Result%253Cjava.lang.Void%253E%2522%257D%252C%257B%2522methodName%2522%253A%2522getByUid%2522%252C%2522paramTypes%2522%253A%255B%2522java.lang.Integer%2522%255D%252C%2522returnType%2522%253A%2522com.weidai.ucore.facade.dto.Result%253Ccom.weidai.ucore.facade.domain.TenderInfoDO%253E%2522%257D%252C%257B%2522methodName%2522%253A%2522updateByUid%2522%252C%2522paramTypes%2522%253A%255B%2522com.weidai.ucore.facade.domain.TenderInfoDO%2522%255D%252C%2522returnType%2522%253A%2522com.weidai.ucore.facade.dto.Result%253Cjava.lang.Void%253E%2522%257D%255D%26methods%3DgetByUid%2Csave%2CupdateByUid%26pid%3D7552%26revision%3D1.0%26side%3Dprovider%26timestamp%3D1505446665270%26version%3D1.0");
        String methodSignatures = URL.valueOf(str).getParameterAndDecoded(RegistryProtocolWrapper.METHOD_SIGNATURE);
        System.out.println(methodSignatures);
    }

}
