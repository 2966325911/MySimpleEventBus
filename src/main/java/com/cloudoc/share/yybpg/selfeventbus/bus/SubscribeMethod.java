package com.cloudoc.share.yybpg.selfeventbus.bus;

import java.lang.reflect.Method;

/**
 * @author : Vic
 * time   : 2018/04/24
 * desc   :
 */
public class SubscribeMethod {
    private String label;
    private Method method;
    private Class<?>[] paramsTypes;


    public SubscribeMethod(String label,Method method,Class<?>[] paramsTypes) {
        this.label = label;
        this.method = method;
        this.paramsTypes = paramsTypes;
    }

    public Method getMethod() {
        return method;
    }

    public String getLabel() {
        return label == null ? "" : label;
    }

    public Class<?>[] getParamsTypes() {
        return paramsTypes;
    }
}
