package com.example.crashtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "hftest";
    private Button buttonANR, buttonCrash, buttonAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void initView(){
        buttonANR = findViewById(R.id.button_anr);
        buttonCrash = findViewById(R.id.button_crash);
        buttonAlertDialog = findViewById(R.id.button_alertdialog);
        buttonAlertDialog.setOnClickListener(this);

        buttonANR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("crashTest", "anr");
                Toast.makeText(MainActivity.this,R.string.toast_anr_text, Toast.LENGTH_SHORT).show();

                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String array[] = null;
                String str = array[3];
                Log.d("crashTest", "crash");
            }
        });

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_alertdialog:
                tipDialog();
//                getUsbInfo();
                break;
                default:
                    break;
        }
    }

    //MotionEvent模拟点击
    private void setSimulateClick(View view, float x, float y) {

        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    public void tipDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示：");
        builder.setMessage("这是一个普通对话框，");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(true);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        //设置中立按钮
        builder.setNeutralButton("等待", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Button PositiveButton=((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                int[] location = new int[2];
                PositiveButton.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                System.out.println("x:"+x+"y:"+y);

                Toast.makeText(MainActivity.this, "你选择了等待" + "x:"+x+"y:"+y, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        //对话框显示的监听事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.e(TAG, "对话框显示了");
            }
        });
        //对话框消失的监听事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e(TAG, "对话框消失了");
            }
        });
        dialog.show();
      //setSimulateClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE),701,317);
    }
    public void tipDialog2(){
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Warning:");
            builder.setMessage("Shut down the device?");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(true);            //点击对话框以外的区域是否让对话框消失

            //设置正面按钮
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            //设置反面按钮
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();      //创建AlertDialog对象
            //对话框显示的监听事件
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Log.e(TAG, "对话框显示了");
                }
            });
            //对话框消失的监听事件
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.e(TAG, "对话框消失了");
                }
            });
            dialog.show();
            //setSimulateClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE),701,317);
        }

    }
    private void shutDown(){
        String ACTION_REQUEST_SHUTDOWN
                = "com.android.internal.intent.action.REQUEST_SHUTDOWN";
        String EXTRA_KEY_CONFIRM = "android.intent.extra.KEY_CONFIRM";

        Intent intent = new Intent(ACTION_REQUEST_SHUTDOWN);
        intent.putExtra(EXTRA_KEY_CONFIRM, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void getUsbInfo(){
        UsbManager mUsbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
        final TextView usb = buttonAlertDialog;

        StringBuilder builder = new StringBuilder();
        builder.append(mUsbManager.getDeviceList().size()).append("\n");
        Log.e(TAG, "LLL = " + mUsbManager.getDeviceList().size());
        for(Map.Entry<String, UsbDevice> entry : mUsbManager.getDeviceList().entrySet()){
            builder.append(entry.getKey() + " " +entry.getValue().getProductName()).append("\n");

        }
        usb.setText(builder.toString());
    }


}
