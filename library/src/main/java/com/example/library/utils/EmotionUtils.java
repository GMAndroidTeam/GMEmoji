
package com.example.library.utils;


import com.example.library.R;

import java.util.LinkedHashMap;


/**
 * @author : zejian
 * @time : 2016年1月5日 上午11:32:33
 * @email : shinezejian@163.com
 * @description :表情加载类,可自己添加多种表情，分别建立不同的map存放和不同的标志符即可
 */
public class EmotionUtils {

	/**
	 * 表情类型标志符
	 */
	public static final int EMOTION_CLASSIC_TYPE = 0x0001;//经典表情

	/**
	 * key-表情文字;
	 * value-表情图片资源
	 */
	public static LinkedHashMap<String, Integer> EMPTY_MAP;
	public static LinkedHashMap<String, Integer> EMOTION_CLASSIC_MAP;


	static {
		EMPTY_MAP = new LinkedHashMap<>();
		EMOTION_CLASSIC_MAP = new LinkedHashMap<>();

		EMOTION_CLASSIC_MAP.put("[微笑]", R.drawable.emoji_1f603);
		EMOTION_CLASSIC_MAP.put("[冷笑]", R.drawable.emoji_1f605);
		EMOTION_CLASSIC_MAP.put("[可爱]", R.drawable.emoji_1f60a);
		EMOTION_CLASSIC_MAP.put("[色]", R.drawable.emoji_1f60d);
		EMOTION_CLASSIC_MAP.put("[大快朵颐]", R.drawable.emoji_1f60b);
		EMOTION_CLASSIC_MAP.put("[扎眼]", R.drawable.emoji_1f609);
		EMOTION_CLASSIC_MAP.put("[困惑]", R.drawable.emoji_1f616);
		EMOTION_CLASSIC_MAP.put("[轻松]", R.drawable.emoji_1f60c);
		EMOTION_CLASSIC_MAP.put("[哭脸]", R.drawable.emoji_1f622);
		EMOTION_CLASSIC_MAP.put("[憨笑]", R.drawable.emoji_1f606);
		EMOTION_CLASSIC_MAP.put("[笑流泪]", R.drawable.emoji_1f602);
		EMOTION_CLASSIC_MAP.put("[眯笑]", R.drawable.emoji_1f601);
		EMOTION_CLASSIC_MAP.put("[调戏]", R.drawable.emoji_1f618);
		EMOTION_CLASSIC_MAP.put("[鬼脸]", R.drawable.emoji_1f61c);
		EMOTION_CLASSIC_MAP.put("[伪笑]", R.drawable.emoji_1f60f);
		EMOTION_CLASSIC_MAP.put("[莫言]", R.drawable.emoji_1f612);
		EMOTION_CLASSIC_MAP.put("[我晕]", R.drawable.emoji_1f635);
		EMOTION_CLASSIC_MAP.put("[不乐观]", R.drawable.emoji_1f630);
		EMOTION_CLASSIC_MAP.put("[笑眼]", R.drawable.emoji_1f604);
		EMOTION_CLASSIC_MAP.put("[生无可恋]", R.drawable.emoji_1f625);
		EMOTION_CLASSIC_MAP.put("[白笑]", R.drawable.emoji_263a);
		EMOTION_CLASSIC_MAP.put("[亲亲]", R.drawable.emoji_1f61a);
		EMOTION_CLASSIC_MAP.put("[闭眼]", R.drawable.emoji_1f61d);
		EMOTION_CLASSIC_MAP.put("[担心]", R.drawable.emoji_1f629);
		EMOTION_CLASSIC_MAP.put("[沉思]", R.drawable.emoji_1f614);
		EMOTION_CLASSIC_MAP.put("[沮丧]", R.drawable.emoji_1f61e);
		EMOTION_CLASSIC_MAP.put("[痛哭]", R.drawable.emoji_1f62b);
		EMOTION_CLASSIC_MAP.put("[惊悚]", R.drawable.emoji_1f631);
		EMOTION_CLASSIC_MAP.put("[害怕]", R.drawable.emoji_1f628);
		EMOTION_CLASSIC_MAP.put("[哭了]", R.drawable.emoji_1f62a);
		EMOTION_CLASSIC_MAP.put("[大哭]", R.drawable.emoji_1f62d);
		EMOTION_CLASSIC_MAP.put("[吃惊]", R.drawable.emoji_1f632);
		EMOTION_CLASSIC_MAP.put("[坚强]", R.drawable.emoji_1f623);
		EMOTION_CLASSIC_MAP.put("[愤怒]", R.drawable.emoji_1f620);
		EMOTION_CLASSIC_MAP.put("[胜利]", R.drawable.emoji_1f624);
		EMOTION_CLASSIC_MAP.put("[流汗]", R.drawable.emoji_1f613);
		EMOTION_CLASSIC_MAP.put("[感冒]", R.drawable.emoji_1f637);
		EMOTION_CLASSIC_MAP.put("[红脸]", R.drawable.emoji_1f633);
		EMOTION_CLASSIC_MAP.put("[怒火]", R.drawable.emoji_1f621);
	}

	/**
	 * 根据名称获取当前表情图标R值
	 *
	 * @param EmotionType 表情类型标志符
	 * @param imgName     名称
	 * @return
	 */
	public static int getImgByName(int EmotionType, String imgName) {
		Integer integer = null;
		switch (EmotionType) {
			case EMOTION_CLASSIC_TYPE:
				integer = EMOTION_CLASSIC_MAP.get(imgName);
				break;
			default:
				LogUtils.e("the emojiMap is null!! Handle Yourself ");
				break;
		}
		return integer == null ? -1 : integer;
	}

	/**
	 * 根据类型获取表情数据
	 *
	 * @param EmotionType
	 * @return
	 */
	public static LinkedHashMap<String, Integer> getEmojiMap(int EmotionType) {
		LinkedHashMap EmojiMap = null;
		switch (EmotionType) {
			case EMOTION_CLASSIC_TYPE:
				EmojiMap = EMOTION_CLASSIC_MAP;
				break;
			default:
				EmojiMap = EMPTY_MAP;
				break;
		}
		return EmojiMap;
	}
}
