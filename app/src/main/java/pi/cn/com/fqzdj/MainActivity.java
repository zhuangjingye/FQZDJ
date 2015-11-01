package pi.cn.com.fqzdj;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import pi.cn.com.fqzdj.camera.CameraInterface;
import pi.cn.com.fqzdj.camera.CameraSurfaceView;
import pi.cn.com.fqzdj.camera.DisplayUtil;
import pi.cn.com.fqzdj.element.ClothElement;
import pi.cn.com.fqzdj.element.RandomElement;
import pi.cn.com.fqzdj.element.ResultElement;
import pi.cn.com.fqzdj.element.ScissorsElement;
import pi.cn.com.fqzdj.element.StoneElement;

public class MainActivity extends Activity implements View.OnClickListener
        ,SensorEventListener {

    private ImageView randomIv;//随机

    private ImageView scissorsIv;//剪刀

    private ImageView stoneIv;//石头

    private ImageView clothIv;//布

    private ImageView resultIv;//结果

    private RandomElement randomElement;

    private ResultElement resultElement;

    private ScissorsElement scissorsElement;

    private StoneElement stoneElement;

    private ClothElement clothElement;

    private int resultStatus = -1;//结果状态

    private TextView centerView;//中心控件

    private SensorManager sensorManager = null;

    private Vibrator vibrator = null;

    private boolean isRuning = false;

    private SoundPool sp;

    private HashMap<Integer, Integer> map;

    private LinearLayout bg;

    private Bitmap myBitmap;

    private RelativeLayout mainRl;

    private RelativeLayout aboutRl;

    private TextView aboutEnterTV;

    private TextView backTV;

    private TextView coinTV;

    private Handler myHandler = new Handler(){
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    bg.setBackground(new BitmapDrawable(myBitmap));
                    break;
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        mainRl = (RelativeLayout) findViewById(R.id.mainRl);
        aboutRl = (RelativeLayout) findViewById(R.id.aboutRl);
        aboutRl.setOnClickListener(this);
        randomIv = (ImageView) findViewById(R.id.randomIv);
        startAnimation(randomIv);
        scissorsIv = (ImageView) findViewById(R.id.scissorsIv);
        stoneIv = (ImageView) findViewById(R.id.stoneIv);
        clothIv = (ImageView) findViewById(R.id.clothIv);
        resultIv = (ImageView) findViewById(R.id.resultIv);
        centerView = (TextView) findViewById(R.id.centerView);
        aboutEnterTV = (TextView) findViewById(R.id.aboutEnterTV);
        backTV = (TextView) findViewById(R.id.backTV);
        backTV.setOnClickListener(this);
        aboutEnterTV.setOnClickListener(this);
        randomIv.setOnClickListener(this);
        scissorsIv.setOnClickListener(this);
        stoneIv.setOnClickListener(this);
        clothIv.setOnClickListener(this);
        resultIv.setOnClickListener(this);
        centerView.setOnClickListener(this);
        randomElement = new RandomElement(this,randomIv);
        clothElement = new ClothElement(this,clothIv);
        stoneElement = new StoneElement(this,stoneIv);
        scissorsElement = new ScissorsElement(this,scissorsIv);
        resultElement = new ResultElement(this,resultIv);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        coinTV = (TextView) findViewById(R.id.coinTV);
        coinTV.setOnClickListener(this);
        bg = (LinearLayout) findViewById(R.id.bg);
        getBg();
        initSoundpool();
//        showMain();
        mainRl.setVisibility(View.VISIBLE);
        aboutRl.setVisibility(View.GONE);

    }




    /**
     * 获得背景
     */
    private void getBg() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);
                myBitmap = FastBlur.doBlur(bitmap,25,false);
                myHandler.sendEmptyMessage(1);
            }
        }.start();
    }



    /**
     * 初始化
     */
    private void initSoundpool() {
        sp = new SoundPool(5,// 同时播放的音效
                AudioManager.STREAM_MUSIC, 0);
        map = new HashMap<Integer, Integer>();
        map.put(1, sp.load(this, R.raw.stop, 1));
        map.put(2, sp.load(this, R.raw.throwing, 1));
    }

    /**
     *
     * @param sound 文件
     * @param number 循环次数
     */
    private void playSound(int sound, int number) {
        AudioManager am = (AudioManager) getSystemService(this.AUDIO_SERVICE);// 实例化
        float audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 音效最大值
        float audioCurrentVolum = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float audioRatio = audioCurrentVolum / audioMaxVolum;
        sp.play(map.get(sound),
                audioRatio,// 左声道音量
                audioRatio,// 右声道音量
                1, // 优先级
                number,// 循环播放次数
                1);// 回放速度，该值在0.5-2.0之间 1为正常速度
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 启动随机的动画
     * @param iv
     */
    private void startAnimation(ImageView iv) {
        AnimationDrawable animationDrawable = (AnimationDrawable) iv.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.randomIv:
                resultStatus();
                break;
            case R.id.clothIv:
                resultStatus = 2;
                randomStatus();
                break;
            case R.id.stoneIv:
                resultStatus = 1;
                randomStatus();
                break;
            case R.id.scissorsIv:
                resultStatus = 0;
                randomStatus();
                break;
            case R.id.resultIv:
                selectedStatus();
                break;
            case R.id.centerView:
                resultStatus = -1;
                randomStatus();
                break;
            case R.id.backTV:
                showMain();
                break;
            case R.id.aboutEnterTV:
                showAbout();
                break;
            case R.id.coinTV:
                startCoinActivity();
                break;
        }
    }

    /**
     * 开启activity
     */
    private void startCoinActivity() {
        Intent intent = new Intent();
        intent.setClass(this,CoinActivity.class);
        startActivity(intent);
    }
    /**
     * 随机的状态
     */
    private void randomStatus() {
        playSound(2,1);
        centerView.setClickable(false);
        randomElement.showElement();
        clothElement.hideElement();
        stoneElement.hideElement();
        scissorsElement.hideElement();
        resultElement.hideElement();
    }

    /**
     * 选择
     */
    private void selectedStatus() {
        centerView.setClickable(true);
        randomElement.hideElement();
        clothElement.showElement();
        stoneElement.showElement();
        scissorsElement.showElement();
        resultElement.hideElement();
    }

    /**
     * 显示结果
     */
    private void resultStatus() {
        if (resultStatus != -1) {
            resultElement.setResult(resultStatus);
        } else {
            Random random = new Random();
            resultElement.setResult(random.nextInt()%3);
        }
        playSound(1,1);
        randomElement.hideElement();
        clothElement.hideElement();
        stoneElement.hideElement();
        scissorsElement.hideElement();
        resultElement.showElement();
        resultStatus = -1;
    }

    /**
     * 同一个动作 短时间内不能再次执行
     */
    private void startNewThread() {
        isRuning = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isRuning = false;
            }
        }.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER)
        {
            if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17)) {
                Log.d("zpi", "============ values[0] = " + values[0]);
                Log.d("zpi", "============ values[1] = " + values[1]);
                Log.d("zpi", "============ values[2] = " + values[2]);
                if (isRuning) return;
                if (mainRl.getVisibility() == View.GONE) return;
                startNewThread();
                //摇动手机后，再伴随震动提示~~
                vibrator.vibrate(500);
                if (randomIv.getVisibility() == View.VISIBLE) {
                    resultStatus();
                } else {
                    reset();
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 复位
     */
    private void reset() {
        resultStatus = -1;
        randomStatus();
    }



    /**
     * 显示主页
     */
    private void showMain() {
//        mainRl.setVisibility(View.VISIBLE);
//        aboutRl.setVisibility(View.GONE);
        showView(mainRl);
        hideView(aboutRl);
    }

    /**
     * 显示关于
     */
    private void showAbout() {
//        mainRl.setVisibility(View.GONE);
//        aboutRl.setVisibility(View.VISIBLE);
        showView(aboutRl);
        hideView(mainRl);
    }

    /**
     * 显示控件
     */
    private void showView (View view) {
        ScaleAnimation scaleAnimation =new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);//设置动画持续时间
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(scaleAnimation);
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏
     * @param view
     */
    private void hideView(final View view) {
        ScaleAnimation scaleAnimation =new ScaleAnimation(1.0f,0.1f,  1.0f,0.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);//设置动画持续时间
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
