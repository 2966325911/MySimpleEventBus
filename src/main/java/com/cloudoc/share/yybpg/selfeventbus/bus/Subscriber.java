package com.cloudoc.share.yybpg.selfeventbus.bus;

/**
 * @author : Vic
 * time   : 2018/04/24
 * desc   :
 */
public class Subscriber {
    private Object subscribe;
    private SubscribeMethod subscribeMethod ;

    public Subscriber(Object subscribe, SubscribeMethod subscribeMethod) {
        this.subscribe = subscribe;
        this.subscribeMethod = subscribeMethod;
    }

    public Object getSubscribe() {
        return subscribe;
    }

    public SubscribeMethod getSubscribeMethod() {
        return subscribeMethod;
    }
}
