package com.example.library.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.library.R;
import com.example.library.fragment.EmotiomComplateFragment;
import com.example.library.utils.DisplayUtils;
import com.example.library.utils.EmotionUtils;

import java.util.List;

/**
 * Created by zejian
 * Time  16/1/7 下午4:46
 * Email shinezejian@163.com
 * Description:
 */
public class EmotionGridViewAdapter extends BaseAdapter {

	private Context      context;
	private List<String> emotionNames;
	private int          emotion_map_type;

	public EmotionGridViewAdapter(Context context, List<String> emotionNames,int emotion_map_type) {
		this.context = context;
		this.emotionNames = emotionNames;
		this.emotion_map_type = emotion_map_type;
	}

	@Override
	public int getCount() {
		// +1 最后一个为删除按钮
		// 每一页最多放27个表情,1个回退按钮故return 28
		return EmotiomComplateFragment.EMOJI_SIZE + 1;
	}

	@Override
	public String getItem(int position) {
		return emotionNames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {

			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(this.context).inflate(R.layout.griditem_emoji, null);

			viewHolder.iv_emoji = (ImageView) convertView.findViewById(R.id.iv_emoji);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		//判断是否为最后一个item
		if (position == getCount() - 1) {
			//最后一个item为删除键宽高要单独设置
			viewHolder.iv_emoji.setImageResource(R.drawable.compose_emotion_delete);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisplayUtils.dp2px(context, 26), DisplayUtils.dp2px(context, 24));
			layoutParams.setMargins(0, DisplayUtils.dp2px(context, 4), 0 ,0);
			layoutParams.gravity = Gravity.CENTER;
			viewHolder.iv_emoji.setLayoutParams(layoutParams);
		} else {
			String emotionName = emotionNames.get(position);
			if (EmotionUtils.getImgByName(emotion_map_type, emotionName) == -1){
				// 为-1说明没匹配到对应的表情图片, 说明该位置为我们自己填充的空数据,则显示一个透明色占位即可
				viewHolder.iv_emoji.setImageResource(R.color.transparent);
			}else {
				viewHolder.iv_emoji.setImageResource(EmotionUtils.getImgByName(emotion_map_type, emotionName));
			}
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView iv_emoji;
	}

}
