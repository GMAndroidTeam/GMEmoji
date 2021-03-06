package com.example.library.emotionkeyboardview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.library.utils.MobileBrandUtils;

/**
 * author : zejian
 * time : 2016年1月5日 上午11:14:27
 * email : shinezejian@163.com
 * description :源码来自开源项目https://github.com/dss886/Android-EmotionInputDetector
 * 本人仅做细微修改以及代码解析
 */
public class EmotionKeyboard {

    private static final String SHARE_PREFERENCE_NAME = "EmotionKeyboard";
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";

    private Activity mActivity;
    private InputMethodManager mInputManager;//软键盘管理类
    private SharedPreferences sp;
    private View mEmotionLayout;//表情布局
    private EditText mEditText;//输入框
    private View mContentView;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪


    public boolean isInputMethodOpen = false;//软件盘是否打开
    private OnEditContentTouchListener mOnEditContentTouchListener;
    private OnEmojiImageClickListener mOnEmojiImageClickListener;
    private OnGetSoftHeightListener mOnGetSoftHeightListener;
    private int mSoftInputHeight = 0;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    private int minSoftHeight =187;
    //获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
    private int firstDefaultSoftHeight = 787;


    private EmotionKeyboard() {

    }

    /**
     * 外部静态调用
     *
     * @param activity
     * @return
     */
    public static EmotionKeyboard with(Activity activity) {
        EmotionKeyboard emotionInputDetector = new EmotionKeyboard();
        emotionInputDetector.mActivity = activity;
        emotionInputDetector.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        emotionInputDetector.sp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return emotionInputDetector;
    }

    /**
     * 绑定内容view，此view用于固定bar的高度，防止跳闪
     *
     * @param contentView
     * @return
     */
    public EmotionKeyboard bindToContent(View contentView) {
        mContentView = contentView;
        return this;
    }

    /**
     * 绑定编辑框
     *
     * @param editText
     * @return
     */
    public EmotionKeyboard bindToEditText(EditText editText) {
        mEditText = editText;
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isInputMethodOpen = true;
                if (mOnEditContentTouchListener != null) {
                    mOnEditContentTouchListener.onEditContentTouch();
                }
                if (event.getAction() == MotionEvent.ACTION_UP && mEmotionLayout.isShown()) {
                    lockContentHeight(); //显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                    //软件盘显示后，释放内容高度
                    unlockContentHeightDelayed();
                }
                return false;
            }
        });
        return this;
    }

    /**
     * 绑定表情按钮
     *
     * @param emotionButton
     * @return
     */
    public EmotionKeyboard bindToEmotionButton(final ImageView emotionButton, final int emojiImage, final int keyboradImage) {
        emotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEmotionLayout.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                    unlockContentHeightDelayed();//软件盘显示后，释放内容高度
                    emotionButton.setImageResource(emojiImage);
                } else {
                    if (isSoftInputShown()) {//同上
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                        emotionButton.setImageResource(keyboradImage);
                    } else {
                        showEmotionLayout();//两者都没显示，直接显示表情布局
                        emotionButton.setImageResource(keyboradImage);
                        if (mOnEmojiImageClickListener != null) {
                            mOnEmojiImageClickListener.onEmojiImageClick();
                        }
                    }
                }
            }
        });
        return this;
    }

    /**
     * 设置表情内容布局
     *
     * @param emotionView
     * @return
     */
    public EmotionKeyboard setEmotionView(View emotionView) {
        mEmotionLayout = emotionView;
        return this;
    }

    public interface OnEditContentTouchListener {
        void onEditContentTouch();
    }

    public interface OnEmojiImageClickListener {
        void onEmojiImageClick();
    }

    public interface OnGetSoftHeightListener {
        int onGetSoftHeight();
    }

    public void setOnEmojiImageClickListener(OnEmojiImageClickListener onEmojiImageClickListener) {
        mOnEmojiImageClickListener = onEmojiImageClickListener;
    }

    public void setOnEditContentTouchListener(OnEditContentTouchListener onEditContentTouchListener) {
        mOnEditContentTouchListener = onEditContentTouchListener;
    }

    public void setOnGetSoftHeightListener(OnGetSoftHeightListener onGetSoftHeightListener) {
        mOnGetSoftHeightListener = onGetSoftHeightListener;
    }

    public EmotionKeyboard build() {
        //设置软件盘的模式：SOFT_INPUT_ADJUST_RESIZE  这个属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        //从而方便我们计算软件盘的高度
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //隐藏软件盘
        hideSoftInput();
        minSoftHeight = getSoftButtonsBarHeight() + (int)(getStatusBarHeight()*2.5f);
        return this;
    }

    /**
     * 点击返回键时先隐藏表情布局
     *
     * @return
     */
    public boolean interceptBackPress() {
        if (mEmotionLayout.isShown()) {
            hideEmotionLayout(false);
            return true;
        }
        return false;
    }


    private void showEmotionLayout() {
        int softInputHeight = 0;
        if (null != mOnGetSoftHeightListener) {
            //先取登录时存的键盘高度
            softInputHeight = mOnGetSoftHeightListener.onGetSoftHeight();
        }
        //如果是登录过的用户是进入不到登录页的，所以取不到键盘高度，所以还需要重新获取
        if (softInputHeight <= 0) {
            softInputHeight = getKeyBoardHeight();
        }
        hideSoftInput();
        mEmotionLayout.getLayoutParams().height = softInputHeight;
        mEmotionLayout.setVisibility(View.VISIBLE);
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mEditText.requestFocus();
            }
        });
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    public void hideEmotionLayout(boolean showSoftInput) {
        if (mEmotionLayout.isShown()) {
            mEmotionLayout.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
                return;
            }
            isInputMethodOpen = false;
        }
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
                mContentView.requestLayout();
            }
        }, 200L);
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    public void showSoftInput() {
        isInputMethodOpen = true;
        mEditText.requestFocus();
        setOnGlobalLayoutListener();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mEditText, 0);
            }
        });
    }

    private void setOnGlobalLayoutListener() {
        if (mSoftInputHeight <= minSoftHeight) {
            if(mOnGlobalLayoutListener==null){
                mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int supportSoftInputHeight = getSupportSoftInputHeight();
                        if (supportSoftInputHeight > minSoftHeight && supportSoftInputHeight != firstDefaultSoftHeight) {
                            mSoftInputHeight = supportSoftInputHeight;
                        }
                        if (mSoftInputHeight > minSoftHeight) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mActivity.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
                            }
                        }
                    }
                };
            }
            mActivity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        isInputMethodOpen = false;
        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 是否显示软件盘
     *
     * @return
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() > minSoftHeight;
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }

        if (softInputHeight < 0) {
            Log.w("gengmei", "EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > minSoftHeight) {
            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }


    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        int statusBarHeight = getStatusBarHeight();
        mActivity.getWindow().getDecorView().getHeight();
        int navigationBarHeight = realHeight - usableHeight;
        if(MobileBrandUtils.getRomType() == MobileBrandUtils.ROM_TYPE.EMUI){
            navigationBarHeight = navigationBarHeight - statusBarHeight;
        }else if(MobileBrandUtils.getRomType() == MobileBrandUtils.ROM_TYPE.MIUI){
            if(Settings.Global.getInt(mActivity.getContentResolver(), "force_fsg_nav_bar", 0) != 0){
                //开启手势，不显示虚拟键
                return 0;
            }
        }
        if (navigationBarHeight > 0) {
            return navigationBarHeight;
        } else {
            return 0;
        }
    }

    private int getStatusBarHeight() {
        int height = 0;
        int resourceId = mActivity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = mActivity.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
     *
     * @return
     */
    public int getKeyBoardHeight() {
        if (mSoftInputHeight > minSoftHeight) {
            return mSoftInputHeight;
        }
        int softHeight = sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 0);
        if (softHeight <= minSoftHeight) {
            int cacheHeight = getSupportSoftInputHeight();
            if (cacheHeight <= minSoftHeight) {
                //容错处理。如果还是取不到高度只能给一个定值
                return firstDefaultSoftHeight;
            }
            softHeight = cacheHeight;
        }
        return softHeight;
//        return sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 787);

    }

    public boolean isNavigationBarShow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = mActivity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y!=size.y;
        }else {
            boolean menu = ViewConfiguration.get(mActivity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if(menu || back) {
                return false;
            }else {
                return true;
            }
        }
    }

}
