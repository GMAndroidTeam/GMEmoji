package com.example.library.emotionkeyboardview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.library.R;
import com.example.library.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * Created by zejian
 * Time  16/1/8 下午3:19
 * Email shinezejian@163.com
 * Description:自定义表情底部指示器
 */
public class EmojiIndicatorView extends LinearLayout {

    private Context         mContext;
    private ArrayList<ImageView> mImageViews;//所有指示器集合
    private int marginSize = 12;
    private int marginLeft;//间距

    public EmojiIndicatorView(Context context) {
        this(context, null);
    }

    public EmojiIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        marginLeft = DisplayUtils.dp2px(context, marginSize);
    }

    /**
     * 初始化指示器
     *
     * @param count 指示器的数量
     */
    public void initIndicator(int count) {
        mImageViews = new ArrayList<>();
        this.removeAllViews();
        LinearLayout.LayoutParams lp;
        for (int i = 0; i < count; i++) {
            ImageView v = new ImageView(mContext);
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i != 0)
                lp.leftMargin = marginLeft;
            v.setLayoutParams(lp);
            if (i == 0) {
                v.setImageResource(R.drawable.bg_point_indicator_selected);

            } else {
                v.setImageResource(R.drawable.bg_point_indicator_unselected);
            }
            mImageViews.add(v);
            this.addView(v);
        }
    }

    /**
     * 页面移动时切换指示器
     */
    public void playByStartPointToNext(int startPosition, int nextPosition) {
        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }
        final ImageView ViewStrat = mImageViews.get(startPosition);
        final ImageView ViewNext = mImageViews.get(nextPosition);
        ViewNext.setImageResource(R.drawable.bg_point_indicator_selected);
        ViewStrat.setImageResource(R.drawable.bg_point_indicator_unselected);
    }
}
