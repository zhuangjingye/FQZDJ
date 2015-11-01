package pi.cn.com.fqzdj.element;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by pi on 15-10-30.
 * 随机状态类
 */
public class RandomElement extends Element {
    public RandomElement(Activity activity,ImageView iv) {
        super(activity,iv);
    }

    @Override
    public void showElement() {
        if (iv.getVisibility() == View.VISIBLE) return;
        iv.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation = getScaleAnimation(0.1f,1.0f,0.1f,1.0f);
        iv.startAnimation(scaleAnimation);

    }

    @Override
    public void hideElement() {
        iv.setVisibility(View.GONE);

    }
}
