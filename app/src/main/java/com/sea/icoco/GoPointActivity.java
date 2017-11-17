package com.sea.icoco;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;


import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sea.icoco.ActivitySupporter.StepArcView;
import com.sea.icoco.Control.DataControler;
import com.sea.icoco.R;
import com.sea.icoco.step.UpdateUiCallBack;
import com.sea.icoco.step.service.StepService;
import com.sea.icoco.step.utils.SharedPreferencesUtils;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class GoPointActivity extends AppCompatActivity {
//    private Timer timer;
    private boolean isBind = false;
//    CircularProgressBar circularProgressBar; // https://github.com/lopspower/CircularProgressBar
    TextView point_text,speed_txv;
    private StepArcView stepArcView;
    private SharedPreferencesUtils sp;
    StepService stepService;
    DataControler dataControler = MainActivity.dataControler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_point);
        findView();
        initData();
        setupService();
    }
    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步數，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "20");
        point_text.setText(dataControler.userData.getBonusPoint());
        //设置当前步數为0
        stepArcView.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
//        tv_isSupport.setText("计步中...");
    }
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent,conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }
    private void findView() {
        stepArcView = (StepArcView) findViewById(R.id.stepArcView);
        point_text = (TextView) findViewById(R.id.point_text);
        speed_txv = (TextView) findViewById(R.id.speed_txv);
    }

    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            final String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "20");
            stepArcView.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());



            stepArcView.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //设置步數监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    speed_txv.setText(new DecimalFormat("#.##").format(dataControler.gpsData.getSpeed()));
                    if ((dataControler.gpsData.isKeepGoing() && dataControler.gpsData.getSpeed() <= 20.0) || !dataControler.gpsData.isSpeedLimit()){
                        Integer planWalk_QTY = Integer.parseInt((String) sp.getParam("planWalk_QTY", "20"));
                        if (stepCount >= planWalk_QTY){
                            stepService.setStepCount(stepCount-planWalk_QTY);
                            stepArcView.setCurrentCount(20,stepCount-planWalk_QTY);
                            dataControler.userData.setBonusPoint(Integer.parseInt(dataControler.userData.getBonusPoint())+1);
                            point_text.setText(dataControler.userData.getBonusPoint());
                        }
                        else{
                            stepArcView.setCurrentCount(planWalk_QTY, stepCount);
                        }
                    }else{
                        if (stepCount >= Integer.parseInt(planWalk_QTY)){
                            stepService.setStepCount(stepCount-Integer.parseInt(planWalk_QTY));
                            stepArcView.setCurrentCount(20,stepCount-Integer.parseInt(planWalk_QTY));
                        }
                    }


//                    Log.d("點數","最大步:"+planWalk_QTY);
                }

            });

        }
        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}
