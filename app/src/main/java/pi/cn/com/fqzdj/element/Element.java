package pi.cn.com.fqzdj.element;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by pi on 15-10-30.
 */
public abstract class Element {
    protected ImageView iv;
    protected Activity activity;
    public Element(Activity activity,ImageView iv) {
        this.iv = iv;
        this.activity = activity;
    }
    public abstract void showElement();
    public abstract void hideElement();

    /**
     * 获得旋转动画
     * @return
     */
    public RotateAnimation getRoateRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(500);
        return rotateAnimation;
    }

    /**
     * 获得平移动画
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    public TranslateAnimation getTranslate(int startX,int endX,int startY,int endY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(startX, endX,startY,endY);
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new OvershootInterpolator());
        return translateAnimation;
    }

    /**
     * 中心坐标
     * @return
     */
    public int[] getCenterPosition() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        int mScreenHeight = dm.heightPixels;
        int d = dip2px(activity,70);
        int[] position = {mScreenWidth/2 - d,mScreenHeight/2 - d};
        return position;
    }

    /**
     * 获得缩放动画
     * @return
     */
    public ScaleAnimation getScaleAnimation(float startX,float endX,float startY,float endY) {
        ScaleAnimation scaleAnimation =new ScaleAnimation(startX, endX, startY, endY,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);//设置动画持续时间
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        return scaleAnimation;

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
