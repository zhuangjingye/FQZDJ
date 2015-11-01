package pi.cn.com.fqzdj.element;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import pi.cn.com.fqzdj.R;

/**
 * Created by pi on 15-10-30.
 */
public class ResultElement extends Element {

    public ResultElement(Activity activity,ImageView iv) {
        super(activity,iv);
    }

    @Override
    public void showElement() {
        iv.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = getTranslateAnimation(0);
        iv.startAnimation(translateAnimation);

    }

    @Override
    public void hideElement() {
        if (iv.getVisibility() == View.GONE) return;
        ScaleAnimation scaleAnimation = getScaleAnimation(1.0f,0.1f,1.0f,0.1f);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(scaleAnimation);
    }

    /**
     * 结果设置 0 剪刀，1石头 2布
     * @param status
     */
    public void setResult(int status) {
        switch (status) {
            case 0:
                iv.setImageResource(R.mipmap.scissors);
                break;
            case 1:
                iv.setImageResource(R.mipmap.stone);
                break;
            case 2:
                iv.setImageResource(R.mipmap.cloth);
                break;
        }
    }
    /**
     * 获得旋转动画
     * @return
     */
    public TranslateAnimation getTranslateAnimation(int x) {
        TranslateAnimation animation = new TranslateAnimation(0, -dip2px(activity,5), 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(100);
        animation.setRepeatCount(3);
        return animation;
    }
}
