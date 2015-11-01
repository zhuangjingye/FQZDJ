package pi.cn.com.fqzdj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by pi on 15-10-30.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager pager = null;

    private LinearLayout indicatorLl;

    private ArrayList<View> viewContainter;

    private Button loginBtn;

    private int[] images = {R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        pager = (ViewPager) findViewById(R.id.viewpager);
        indicatorLl = (LinearLayout) findViewById(R.id.indicatorLl);
        getViewContainter();
    }

    /**
     * 获得要显示等控件列表
     */
    private void getViewContainter() {
        viewContainter = new ArrayList<View>();
        for (int i=0;i<images.length;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageResource(images[i]);
            if(images.length - 1 == i) {
                View view = getGuideViewThree();
                loginBtn = (Button) view.findViewById(R.id.loginBtn);
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startMainActivity();
                        finish();
                    }
                });
                viewContainter.add(view);
            } else {
                viewContainter.add(imageView);
            }

        }

        pager.setAdapter(new MyPagerAdapter());
        pager.setCurrentItem(0, false);
        pager.setOnPageChangeListener(this);
        updateIndicatorLl(0);
    }

    /**
     * 根据当前等状态显示指示器
     *
     * @param selectId
     */
    private void updateIndicatorLl(int selectId) {
        indicatorLl.removeAllViews();
        if (images.length == 1) {
            return;
        }
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            if (i == selectId) {
                imageView.setImageResource(R.mipmap.focus_point_select);
            } else {
                imageView.setImageResource(R.mipmap.focus_point_default);
            }
            imageView.setPadding(5, 5, 5, 5);
            indicatorLl.addView(imageView);
        }

    }

    /**
     * 获得第三页导航
     */
    private View getGuideViewThree() {
        View view = LayoutInflater.from(this).inflate(R.layout.guide_three,null);
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateIndicatorLl(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void startMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * 法律援助适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        //viewpager中的组件数量
        @Override
        public int getCount() {
            return viewContainter.size();
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ((ViewPager) container).removeView(viewContainter.get(position));
        }

        //每次滑动的时候生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ((ViewPager) container).addView(viewContainter.get(position));
            return viewContainter.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
}
