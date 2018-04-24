package com.cloudoc.share.yybpg.selfeventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cloudoc.share.yybpg.selfeventbus.bus.DNBus;
import com.cloudoc.share.yybpg.selfeventbus.bus.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_1, new Fragment1())
                .replace(R.id.frame_2, new Fragment2()).commit();
        DNBus.getDefault().register(this);
    }

    /**
     * 订阅了 f1和f2的事件
     *
     * @param from
     */
    @Subscribe({Constants.NOTIFY_F1_3, Constants.NOTIFY_F2_3})
    public void test(String from) {
        Log.e("MainActivity", from);
    }


    public void notifyTest(View view) {
        //发出事件
        DNBus.getDefault().post(Constants.NOTIFY_TEST, "我是MainActivity的数据");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DNBus.getDefault().unregister(this);
    }
}
