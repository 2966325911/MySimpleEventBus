package com.cloudoc.share.yybpg.selfeventbus.bus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author : Vic
 * time   : 2018/04/24
 * desc   :
 */
public class SimpleEventBus {

    private static SimpleEventBus defaultInstance;
    /**
     */
    private static final Map<Class<?>, List<SubscribeMethod>> METHOD_CACHE = new
            HashMap<>();

    /**
     * 订阅集合
     * 发送事件的时候 通过Key(标签)查找所有对应的订阅者
     * key 为 订阅的标签 , value为 [订阅者(函数所在的对象)、[订阅的标签、订阅者(函数)、(函数)参数]]
     */
    private static final Map<String, List<Subscriber>> SUBSCRIBES = new
            HashMap<>();

    /**
     * 对应对象中所有需要回调的标签 方便注销
     * key是订阅者(函数)所在类对象， value是该类中所有的订阅标签
     */
    private static final Map<Class<?>, List<String>> REGISTERS = new
            HashMap<>();

    public static SimpleEventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (SimpleEventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new SimpleEventBus();
                }
            }
        }
        return defaultInstance;
    }

    public void clear() {
        METHOD_CACHE.clear();
        SUBSCRIBES.clear();
        REGISTERS.clear();
    }

    /**
     * 先删除注册集合 获得该对象所有的订阅标签
     * 查找订阅集合 删除对应对象上的订阅者
     *
     * @param subscriber
     */
    public void unregister(Object subscriber) {
        //找到该对象中所有的订阅标签
        List<String> lables = REGISTERS.remove(subscriber.getClass());
        if (null != lables) {
            for (String lable : lables) {
                //根据标签查找记录
                List<Subscriber> subscribers = SUBSCRIBES.get(lable);
                if (null != subscribers) {
                    Iterator<Subscriber> iterator = subscribers.iterator();
                    while (iterator.hasNext()) {
                        Subscriber subscription = iterator.next();
                        //对象是同一个 则删除
                        if (subscription.getSubscribe() == subscriber) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    public void register(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        //找到被Subscribe注解的函数 并记录缓存
        List<SubscribeMethod> subscriberMethods = findSubscribe(subscriberClass);

        //为了方便注销
        List<String> labels = REGISTERS.get(subscriberClass);
        if (null == labels) {
            labels = new ArrayList<>();
        }

        //加入注册集合  key:标签 value:对应标签的所有函数
        for (SubscribeMethod subscriberMethod : subscriberMethods) {
            String label = subscriberMethod.getLabel();
            if (!labels.contains(label)) {
                labels.add(label);
            }
            List<Subscriber> subscribers = SUBSCRIBES.get(label);
            if (subscribers == null) {
                subscribers = new ArrayList<>();
                SUBSCRIBES.put(label, subscribers);
            }
            Subscriber newSubscriber = new Subscriber(subscriber, subscriberMethod);
            subscribers.add(newSubscriber);
        }

        REGISTERS.put(subscriberClass, labels);
    }

    /**
     * 找到被Subscribe注解的函数 并记录缓存
     *
     * @param subscriberClass
     */
    private List<SubscribeMethod> findSubscribe(Class<?> subscriberClass) {
        List<SubscribeMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (null == subscriberMethods) {
            subscriberMethods = new ArrayList<>();
            //遍历函数
            Method[] methods = subscriberClass.getDeclaredMethods();
            for (Method method : methods) {
                Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                if (null != subscribeAnnotation) {
                    //注解上的标签
                    String[] values = subscribeAnnotation.value();
                    //参数
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (String value : values) {
                        method.setAccessible(true);
                        SubscribeMethod subscriberMethod = new SubscribeMethod(value, method,
                                parameterTypes);
                        subscriberMethods.add(subscriberMethod);
                    }
                }
            }
            //缓存

            METHOD_CACHE.put(subscriberClass, subscriberMethods);
        }
        return subscriberMethods;
    }

    /**
     * 发送事件给所有订阅者
     *
     * @param label
     * @param params
     */
    public void post(String label, Object... params) {
        //获得所有对应的订阅者
        List<Subscriber> subscribers = SUBSCRIBES.get(label);
        for (Subscriber subscriber : subscribers) {
            //组装参数 执行函数
            SubscribeMethod subscriberMethod = subscriber.getSubscribeMethod();
            Class<?>[] parameterTypes = subscriberMethod.getParamsTypes();
            Object[] realParams = new Object[parameterTypes.length];
            if (null != params) {
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (i < params.length && parameterTypes[i].isInstance(params[i])) {
                        realParams[i] = params[i];
                    } else {
                        realParams[i] = null;
                    }
                }
            }
            try {
                subscriberMethod.getMethod().invoke(subscriber.getSubscribe(), realParams);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
