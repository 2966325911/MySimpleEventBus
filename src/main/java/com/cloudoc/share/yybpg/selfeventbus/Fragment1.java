package com.cloudoc.share.yybpg.selfeventbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudoc.share.yybpg.selfeventbus.bus.DNBus;
import com.cloudoc.share.yybpg.selfeventbus.bus.Subscribe;

/**
 * @author Lance
 * @date 2018/4/9
 */

public class Fragment1 extends Fragment {

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DNBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        DNBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        view.findViewById(R.id.notify1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DNBus.getDefault().post(Constants.NOTIFY_F1_1, "我是Fragment1的数据");
            }
        });
        view.findViewById(R.id.notify2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DNBus.getDefault().post(Constants.NOTIFY_F1_2, "我是Fragment1的数据", "我是第二个");
            }
        });
        view.findViewById(R.id.notify3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DNBus.getDefault().post(Constants.NOTIFY_F1_3, "我是Fragment1的数据", "我是第三个");
            }
        });
        return view;
    }

    @Subscribe(Constants.NOTIFY_TEST)
    private void test(String from) {
        Log.e("Fragment1", from);
    }

    @Subscribe(Constants.NOTIFY_F2_1)
    private void f2_1(String a, String b, String c) {
        Log.e("Fragment1", String.format("a=%s,b=%s,c=%s", a, b, c));
    }

    @Subscribe(Constants.NOTIFY_F2_2)
    public void f2_2(String from) {
        Log.e("Fragment1", from);
    }
}
