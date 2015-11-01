package pi.cn.com.fqzdj.element;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import pi.cn.com.fqzdj.R;

/**
 * Created by pi on 15-10-30.
 * 剪刀类
 */
public class ScissorsElement extends Element {
    public ScissorsElement(Activity activity,ImageView iv) {
        super(activity,iv);
    }

    @Override
    public void showElement() {
        iv.setVisibility(View.VISIBLE);
        int[] startPosition = getCenterPosition();
        int[] endPosition = new int[2];
        iv.getLocationOnScreen(endPosition);
        TranslateAnimation translateAnimation = getTranslate(startPosition[0]-endPosition[0],0,startPosition[1]-endPosition[1],0);
        iv.startAnimation(translateAnimation);
    }

    @Override
    public void hideElement() {
        if (iv.getVisibility() == View.GONE) return;
        int[] startPosition = getCenterPosition();
        int[] endPosition = new int[2];
        iv.getLocationOnScreen(endPosition);
        TranslateAnimation translateAnimation = getTranslate(0,startPosition[0]-endPosition[0],0,startPosition[1]-endPosition[1]);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        iv.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
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

    }

    /**
     * 选转
     * @param iv
     */
    private void roate(ImageView iv) {
        Animation operatingAnim = AnimationUtils.loadAnimation(activity, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        iv.startAnimation(operatingAnim);
    }
}
