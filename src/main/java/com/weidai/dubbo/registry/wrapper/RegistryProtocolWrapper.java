/**
 * Copyright (C), 2011-2017, 微贷网.
 */
package com.weidai.dubbo.registry.wrapper;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 在dubbo协议的provider url中拼上接口所有参数的完整函数签名，可用于对dubbo接口的泛化调用测试
 * 
 * @author wuqi 2017/9/14 0014.
 */
public class RegistryProtocolWrapper implements Protocol {

    private static final Logger logger = LoggerFactory.getLogger(RegistryProtocolWrapper.class);

    public static final String DUBBO_PROTOCOL = "dubbo";

    public static final String METHOD_SIGNATURE = "methodSignatures";

    private final Protocol protocol;

    public RegistryProtocolWrapper(Protocol protocol) {
        Assert.notNull(protocol, "protocol == null");
        this.protocol = protocol;
    }

    @Override
    public int getDefaultPort() {
        return protocol.getDefaultPort();
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        if (Constants.REGISTRY_PROTOCOL.equals(invoker.getUrl().getProtocol())) {
            try {
                URL registryUrl = invoker.getUrl();
                String providerUrlStr = registryUrl.getParameterAndDecoded(Constants.EXPORT_KEY);
                Assert.hasLength(providerUrlStr, String.format("The registry export url is null! registry: %s", registryUrl));
                URL providerUrl = URL.valueOf(providerUrlStr);
                if (DUBBO_PROTOCOL.equals(providerUrl.getProtocol())) {
                    providerUrl = providerUrl.addParameter(METHOD_SIGNATURE, URL.encode(getMethodSignatures(invoker.getInterface())));
                    registryUrl = registryUrl.removeParameter(Constants.EXPORT_KEY).addParameter(Constants.EXPORT_KEY, URL.encode(providerUrl.toFullString()));
                    Field urlField = invoker.getClass().getSuperclass().getDeclaredField("url");
                    urlField.setAccessible(true);
                    urlField.set(invoker, registryUrl);
                }
            } catch (Exception e) {
                logger.error("set method signatures failed", e);
            }
        }
        return protocol.export(invoker);
    }

    /**
     * 生成接口所有method的完整签名
     * 
     * @param interfaceClazz
     * @return
     */
    private static String getMethodSignatures(Class<?> interfaceClazz) {
        Method[] methods = interfaceClazz.getMethods();
        String jsonStr = "[]";
        if (methods.length > 0) {
            List<MethodSignature> list = new ArrayList<>(methods.length);
            for (Method method : methods) {
                MethodSignature methodSignature = new MethodSignature();
                methodSignature.setMethodName(method.getName());
                Type[] paramTypes = method.getGenericParameterTypes();
                if (paramTypes.length > 0) {
                    List<String> paramTypeList = new ArrayList<>(paramTypes.length);
                    for (Type type : paramTypes) {
                        paramTypeList.add(getCompleteTypeSignature(type));
                    }
                    // 处理varArgs，eg: String...
                    if (method.isVarArgs()) {
                        String lastParamType = paramTypeList.remove(paramTypeList.size() - 1);
                        lastParamType = lastParamType.substring(0, lastParamType.length() - 2) + "...";
                        paramTypeList.add(lastParamType);
                    }
                    methodSignature.setParamTypes(paramTypeList);
                }
                methodSignature.setReturnType(getCompleteTypeSignature(method.getGenericReturnType()));
                list.add(methodSignature);
            }
            jsonStr = JSON.toJSONString(list);
        }
        return jsonStr;
    }

    private static String getCompleteTypeSignature(Type type) {
        StringBuilder builder = new StringBuilder();
        // noinspection Duplicates
        if (type instanceof ParameterizedType) {
            builder.append(((Class<?>) ((ParameterizedType) type).getRawType()).getName());
            Type[] subArgTypes = ((ParameterizedType) type).getActualTypeArguments();
            appendArgTypes(subArgTypes, builder);
        } else {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                builder.append(clazz.getComponentType().getName()).append("[]");
            } else {
                builder.append(clazz.getName());
            }
        }
        return builder.toString();
    }

    /**
     * 递归查找泛型类型
     * 
     * @param argTypes
     * @param builder
     */
    private static void appendArgTypes(Type[] argTypes, StringBuilder builder) {
        boolean first = true;
        for (Type type : argTypes) {
            if (first) {
                builder.append("<");
                first = false;
            } else {
                builder.append(", ");
            }
            // noinspection Duplicates
            if (type instanceof ParameterizedType) {
                builder.append(((Class<?>) ((ParameterizedType) type).getRawType()).getName());
                Type[] subArgTypes = ((ParameterizedType) type).getActualTypeArguments();
                appendArgTypes(subArgTypes, builder);
            } else {
                Class<?> clazz = (Class<?>) type;
                if (clazz.isArray()) {
                    builder.append(clazz.getComponentType().getName()).append("[]");
                } else {
                    builder.append(clazz.getName());
                }
            }
        }
        builder.append(">");
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return protocol.refer(type, url);
    }

    @Override
    public void destroy() {
        protocol.destroy();
    }
}
