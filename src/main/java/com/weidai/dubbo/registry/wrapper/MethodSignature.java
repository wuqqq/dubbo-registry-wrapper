package com.weidai.dubbo.registry.wrapper;

import java.util.Arrays;
import java.util.List;

/**
 * @author wuqi 2017/9/19 0019.
 */
public class MethodSignature {
    private String methodName;

    private List<String> paramTypes;

    private String returnType;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(List<String> paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        String paramTypeStr = paramTypes == null ? "[]" : Arrays.toString(paramTypes.toArray());
        return "MethodSignature{" +
                "methodName='" + methodName + '\'' +
                ", paramTypes=" + paramTypeStr +
                ", returnType='" + returnType + '\'' +
                '}';
    }
}
