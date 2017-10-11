package com.example.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.library.R;
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
		return emotionNames.size() + 1;
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
			viewHolder.iv_emoji.setImageResource(R.drawable.compose_emotion_delete);
		} else {
			String emotionName = emotionNames.get(position);
			viewHolder.iv_emoji.setImageResource(EmotionUtils.getImgByName(emotion_map_type, emotionName));
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView iv_emoji;
	}

}
