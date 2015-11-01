package pi.cn.com.fqzdj.element;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by pi on 15-10-30.
 * 布类
 */
public class ClothElement extends Element {
    public ClothElement(Activity activity,ImageView iv) {
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
}
