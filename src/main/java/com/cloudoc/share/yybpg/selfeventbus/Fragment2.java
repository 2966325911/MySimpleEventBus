package com.cloudoc.share.yybpg.selfeventbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudoc.share.yybpg.selfeventbus.bus.SimpleEventBus;
import com.cloudoc.share.yybpg.selfeventbus.bus.Subscribe;

/**
 * @author Lance
 * @date 2018/4/9
 */

public class Fragment2 extends Fragment {

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SimpleEventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        SimpleEventBus.getDefault().register(this);

        View view = inflater.inflate(R.layout.fragment_2, container, false);
        view.findViewById(R.id.notify1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleEventBus.getDefault().post(Constants.NOTIFY_F2_1, "我是", "Fragment2", "的数据");
            }
        });
        view.findViewById(R.id.notify2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleEventBus.getDefault().post(Constants.NOTIFY_F2_2, "我是Fragment2的数据,第二个事件");
            }
        });
        view.findViewById(R.id.notify3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleEventBus.getDefault().post(Constants.NOTIFY_F2_3, "我是Fragment2的数据,第三个事件");
            }
        });
        return view;
    }

    @Subscribe(Constants.NOTIFY_TEST)
    private void test(String from) {
        Log.e("Fragment2", from);
    }

    @Subscribe(Constants.NOTIFY_F1_1)
    private void f2_1(String from) {
        Log.e("Fragment2", from);
    }

    @Subscribe(Constants.NOTIFY_F1_2)
    public void f2_2(String a, String b) {
        Log.e("Fragment2", String.format("a=%s,b=%s", a, b));
    }
}
