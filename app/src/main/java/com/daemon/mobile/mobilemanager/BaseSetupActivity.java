package com.daemon.mobile.mobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 10319 on 03/31/16.
 * 作为4个设置向导页面的基类
 */
public abstract class BaseSetupActivity extends Activity {

    private GestureDetector gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();//初始化页面
        initData();//初始化组件数据
        initGesture();//初始化手势滑动
        initEvent();//初始化事件

    }

    public void initData() {

    }

    public void initEvent() {//权限设为public，否则无法重写

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);//绑定onTouch事件
        return super.onTouchEvent(event);
    }

    private void initGesture() {
        //初始化手势监听，需绑定onTouchEvent才生效
        gd = new GestureDetector(new GestureDetector.OnGestureListener() {
            /**
             * 覆盖此方法完成手势切换效果
             * @param e1 初始坐标（开始按下屏幕的点）
             * @param e2 手指离开屏幕的点
             * @param velocityX x轴方向的速度 单位：pix/s
             * @param velocityY y轴方向的速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                float dx = e2.getX() - e1.getX();//x坐标间距
                if(velocityX > 50){//速度大于50pix/s才生效

                   if(Math.abs(dx) < 30){//滑动距离小于30pix无效
                       return false;
                   }else if(dx > 0){//往左移动
                        backPage(null);//不是组件的事件调用，因此不用传view。
                         }else{//往右滑动
                            nextPage(null);
                         }
                }

                return true;
            }



            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }


        });
    }

    public abstract void initView();//每个子类都要调用，因此设为抽象类。让子类必须覆盖此方法

    //onClick下一页
    public void nextPage(View view){
        //1.跳转页面
            nextActivity();
        //2.动画显示
            nextAnimation();

    }

    /**
     * 共有的页面跳转功能
     * @param type
     */
    public void startActivity(Class type){
        Intent next = new Intent(this,type);
        startActivity(next);
        finish();
    }
    public abstract void nextActivity();//抽象具体的功能调用
    public abstract void backActivity();

    //onClick上一页
    public void backPage(View view){
        //1.跳转页面
        backActivity();
        //2.动画显示
        backAnimation();

    }
    /**
     * 跳转上一页的动画
     */
    private void backAnimation() {
        //参数1：进来的动画；参数2：出去的动画
        overridePendingTransition(R.anim.backpage_in,R.anim.backpage_out);

    }

    /**
     * 跳转下一页的动画
     */
    public void nextAnimation(){
        overridePendingTransition(R.anim.nextpage_in,R.anim.nextpage_out);
    }

}


