package pi.cn.com.fqzdj;

import android.app.Activity;
import android.app.Service;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import pi.cn.com.fqzdj.camera.CameraInterface;
import pi.cn.com.fqzdj.camera.CameraSurfaceView;
import pi.cn.com.fqzdj.camera.DisplayUtil;

/**
 * Created by pi on 15-10-31.
 */
public class CoinActivity extends Activity implements View.OnClickListener
        ,CameraInterface.CamOpenOverCallback,SensorEventListener {
    private TextView backTV;

    private CameraSurfaceView surfaceView = null;

    private Vibrator vibrator = null;

    private SensorManager sensorManager = null;

    private RelativeLayout coinRl;

    private ImageView coninIv;

    float previewRate = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread openThread = new Thread(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CameraInterface.getInstance().doOpenCamera(CoinActivity.this);
            }
        };
        openThread.start();
        setContentView(R.layout.activity_coin);
        backTV = (TextView) findViewById(R.id.backTV);
        coinRl = (RelativeLayout) findViewById(R.id.coinRl);
        backTV.setOnClickListener(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        initSuferView();
        coninIv = (ImageView) findViewById(R.id.coninIv);
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


    private void initSuferView () {
        surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        surfaceView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backTV:
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Thread stopThread = new Thread(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                CameraInterface.getInstance().doStopCamera();
            }
        };
        stopThread.start();
    }

    @Override
    public void cameraHasOpened() {
        SurfaceHolder holder = surfaceView.getSurfaceHolder();
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
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

                vibrator.vibrate(500);
                startThrow();

            }

        }
    }

    private void startThrow () {
        Rotate3d rotate = new Rotate3d();
        rotate.setDuration(1000);
        coninIv.startAnimation(rotate);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class Rotate3d extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Matrix matrix = t.getMatrix();
            Camera camera = new Camera();
            camera.save();
            camera.rotateY(180 * interpolatedTime);
            camera.getMatrix(matrix);
            camera.restore();
        }
    }
}
