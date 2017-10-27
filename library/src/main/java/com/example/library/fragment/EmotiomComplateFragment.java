package com.example.library.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.library.R;
import com.example.library.adapter.EmotionGridViewAdapter;
import com.example.library.adapter.EmotionPagerAdapter;
import com.example.library.emotionkeyboardview.EmojiIndicatorView;
import com.example.library.utils.DisplayUtils;
import com.example.library.utils.EmotionUtils;
import com.example.library.utils.GlobalOnItemClickManagerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zejian
 * Time  16/1/5 下午4:32
 * Email shinezejian@163.com
 * Description:可替换的模板表情，gridview实现
 */
public class EmotiomComplateFragment extends BaseFragment {
    private EmotionPagerAdapter emotionPagerGvAdapter;
    private ViewPager           vp_complate_emotion_layout;
    private EmojiIndicatorView  ll_point_group;//表情面板对应的点列表
    private int                 emotion_map_type;

    public static final int EMOJI_SIZE = 27;//每一页放27个表情

    /**
     * 创建与Fragment对象关联的View视图时调用
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complate_emotion, container, false);
        initView(rootView);
        initListener();
        return rootView;
    }

    /**
     * 初始化view控件
     */
    protected void initView(View rootView) {
        vp_complate_emotion_layout = (ViewPager) rootView.findViewById(R.id.vp_complate_emotion_layout);
        ll_point_group = (EmojiIndicatorView) rootView.findViewById(R.id.ll_point_group);
        //获取map的类型
        emotion_map_type = args.getInt(FragmentFactory.EMOTION_MAP_TYPE);
        initEmotion();
    }

    /**
     * 初始化监听器
     */
    protected void initListener() {

        vp_complate_emotion_layout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ll_point_group.playByStartPointToNext(oldPagerPos, position);
                oldPagerPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化表情面板
     * 思路：获取表情的总数，按每行存放7个表情，每个表情的大小写死(现在是25dp)，
     * 而每个表情的高与宽应该是相等的，这里我们约定只存放4行
     * 每个面板最多存放7*4=28个表情，再减去一个删除键，即每个面板包含27个表情
     * 根据表情总数，循环创建多个容量为27的List，存放表情，对于大小不满27进行特殊处理即可。
     */
    private void initEmotion() {
        // item的上下间距
        int verticalSpacing = DisplayUtils.dp2px(getActivity(), 30);
        List<GridView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        // 遍历所有的表情的key
        for (String emojiName : EmotionUtils.getEmojiMap(emotion_map_type).keySet()) {
            emotionNames.add(emojiName);
            // 每27个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == EMOJI_SIZE) {
                GridView gv = createEmotionGridView(emotionNames, verticalSpacing);
                emotionViews.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }

        // 判断最后是否有不足27个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, verticalSpacing);
            emotionViews.add(gv);
        }

        //初始化指示器
        ll_point_group.initIndicator(emotionViews.size());
        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagerAdapter(emotionViews);
        vp_complate_emotion_layout.setAdapter(emotionPagerGvAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp_complate_emotion_layout.setLayoutParams(params);


    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int verticalSpacing) {
        // 创建GridView
        GridView gv = new GridView(getActivity());
        //设置点击背景透明
        gv.setSelector(android.R.color.transparent);
        //设置7列
        gv.setNumColumns(7);
        gv.setPadding(DisplayUtils.dp2px(getActivity(), 15), DisplayUtils.dp2px(getActivity(), 24), DisplayUtils.dp2px(getActivity(), 15), DisplayUtils.dp2px(getActivity(), 11));
//        gv.setHorizontalSpacing(horizontalSpacing);
        gv.setVerticalSpacing(verticalSpacing);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gv.setLayoutParams(params);
        int emotionNameSize = emotionNames.size();
        if (emotionNameSize < EMOJI_SIZE){
            // 如果表情有不足27个的情况,则填满27个为止
            for (int i = 0; i < EMOJI_SIZE - emotionNameSize; i++){
                emotionNames.add("");
            }
        }

        // 给GridView设置表情图片
        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(getActivity(), emotionNames, emotion_map_type);
        gv.setAdapter(adapter);
        //设置全局点击事件
        gv.setOnItemClickListener(GlobalOnItemClickManagerUtils.getInstance(getActivity()).getOnItemClickListener(emotion_map_type));
        return gv;
    }


}
