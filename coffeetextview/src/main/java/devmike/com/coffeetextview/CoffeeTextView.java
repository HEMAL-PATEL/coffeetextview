package devmike.com.coffeetextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorSpace;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.InvocationTargetException;

import devmike.com.coffeetextview.R;

public class CoffeeTextView extends RelativeLayout {

    private  TypedArray ta;
    private TextView mTitleText,
            mCaptionText;
    private ImageView mIcon;
    private int alignPosition;
    private AttributeSet attrs;
    private Rect clip = new Rect();
    private static final int TOP = RelativeLayout.BELOW; //Below of icon
    private static final int BOTTOM = RelativeLayout.BELOW; //Below text_layout
    private static final int START = RelativeLayout.END_OF; //End of Icon
    private static final int END = RelativeLayout.END_OF; //End of text_layout

    public CoffeeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs =attrs;
        init(attrs);
    }


    private void init(AttributeSet set){

        inflate(getContext(), R.layout.coffee_tv_layout, this);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        mTitleText = findViewById(R.id.title);
        mCaptionText = findViewById(R.id.caption);
        mIcon = findViewById(R.id.icon);

        ta = getContext().obtainStyledAttributes(set, R.styleable.CoffeeTextView);

        int titleIdId = findAttrById(R.styleable.CoffeeTextView_ct_title_text);
        int titleTfId = findAttrById(R.styleable.CoffeeTextView_ct_title_typeface);
        float titleSize = ta.getDimension(R.styleable.CoffeeTextView_ct_title_size, 12);
        int color = ta.getColor(R.styleable.CoffeeTextView_ct_title_color, Color.BLACK);

        int mCaptionTfId = findAttrById(R.styleable.CoffeeTextView_ct_caption_typeface);
        int mCaptionTextId = findAttrById(R.styleable.CoffeeTextView_ct_caption_text);
        float mCaptionSize = ta.getDimension(R.styleable.CoffeeTextView_ct_caption_size, 10);
        int mCaptionColorId = ta.getColor(R.styleable.CoffeeTextView_ct_caption_color, Color.BLACK);

        //int iconRes = findAttrById(R.styleable.CoffeeTextView_ct_icon);
        float mTitleShadowDy = ta.getFloat(R.styleable.CoffeeTextView_ct_title_shadowDy, 0);
        float mTitleShadowDx = ta.getFloat(R.styleable.CoffeeTextView_ct_title_shadowDx, 0);
        float mTitleShadowRadius = ta.getFloat(R.styleable.CoffeeTextView_ct_title_shadowRadius, 0);
        int mTitleBottomMargin = ta.getDimensionPixelSize(R.styleable.CoffeeTextView_ct_title_bottom_space, 0);
        int mTitleShadowColor = ta.getColor(R.styleable.CoffeeTextView_ct_title_shadow_color, 0);
        float mCaptionShadowDy = ta.getFloat(R.styleable.CoffeeTextView_ct_caption_shadowDy, 0);
        float mCaptionShadowDx = ta.getFloat(R.styleable.CoffeeTextView_ct_caption_shadowDx, 0);
        float mCaptionShadowRadius = ta.getFloat(R.styleable.CoffeeTextView_ct_caption_shadowRadius, 0);
        int mCaptionShadowColor = ta.getColor(R.styleable.CoffeeTextView_ct_caption_shadow_color, 0);
        int captionVisible = ta.getInt(R.styleable.CoffeeTextView_ct_caption_visibility, VISIBLE);
        //float mCaptionShadowColor = ta.getDimensionPixelSize(R.styleable.CoffeeTextView_ct_caption_bottom_space, 0);

        int titleAnimRes = findAttrById(R.styleable.CoffeeTextView_ct_title_animation);
        int captionAnimRes = findAttrById(R.styleable.CoffeeTextView_ct_caption_animation);

        setTitleShadow(mTitleShadowRadius, mTitleShadowDx, mTitleShadowDy, mTitleShadowColor);
        setCaptionShadow(mCaptionShadowRadius, mCaptionShadowDx, mCaptionShadowDy, mCaptionShadowColor);
        setTitleMarginBottom(mTitleBottomMargin);

        setTitleText(titleIdId);
        setTitleFont(titleTfId);
        setTitleSize(titleSize);

        if (color ==Color.BLACK){
            mTitleText.setTextColor(color);
        }else {
            setTitleColor(color);
        }

        //Set caption properties
        setCaptionFont(mCaptionTfId);
        setCaptionText(mCaptionTextId);

        setCaptionSize(mCaptionSize);
        setCaptionColor(mCaptionColorId);

        setCaptionVisibility(captionVisible);

        animateCaption(captionAnimRes);
        animateTitle(titleAnimRes);

        ta.recycle();
    }

    public Animation animateCaption(@AnimRes int animResId){
        if (animResId ==0)return loadAnimation(R.anim.normal);
        Animation anim =loadAnimation(animResId);
        mCaptionText.setAnimation(anim);
        return anim;
    }

    public Animation animateTitle(@AnimRes int animResId){
        Animation anim = null;
        try {
            if (animResId == 0) return loadAnimation(R.anim.normal);
            anim =loadAnimation(animResId);
            mTitleText.setAnimation(anim);
        }catch (Resources.NotFoundException e){

            throw new Resources.NotFoundException(animResId +" does not exists.");
        }


        notifyView();
       return anim;
    }

    private void notifyView(){
        mTitleText.invalidate();
        mTitleText.requestLayout();
        invalidate();
    }

    private Animation loadAnimation(int animResId){

        return AnimationUtils.loadAnimation(getContext(), animResId);
    }


    @Override
    public void onDraw(Canvas canvas){
        canvas.getClipBounds(clip);
        Log.d(CoffeeTextView.class.getSimpleName(), "onDraw clip height: " + clip.height());
    }


    public void setCaptionVisibility(int visibility){
        mCaptionText.setVisibility(visibility);
    }

    private float dipToPixels(float dipValue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public void setCaptionShadow(float radius, float dx, float dy, int color){
        try {
            mCaptionText.setShadowLayer(radius, dx, dy, color);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    private void setAlignIcon(int alignIcon){

        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        if (alignIcon == TOP || alignIcon == START){
            layoutParams.addRule(alignIcon, R.id.icon);
        }else {
            layoutParams.addRule(alignIcon, R.id.text_layout);
        }
        mIcon.setLayoutParams(layoutParams);
    }

    public void setTitleMarginBottom(int bottomMargin){
        mTitleText.setPadding(0, 0, 0, bottomMargin);
    }


    public void setTitleShadow(float radius, float dx, float dy, @ColorInt int color){
        mTitleText.setShadowLayer(radius, dx, dy, color);
    }


    private void setIcon(@DrawableRes int icon){
        if (icon ==0)return;
        mIcon.setImageResource(icon);
    }

    public void setCaptionColor(int color){
        mCaptionText.setTextColor(color);
        notifyView();
    }

    public void setCaptionSize(float dimen){
        mCaptionText.setTextSize(Utils.exFloat(dimen, getContext()));
        notifyView();
    }

    public void setCaptionSize(@DimenRes int dimen){
        mCaptionText.setTextSize(Utils.exFloat(dimen, getContext()));
        notifyView();
    }

    public void setCaptionText(@StringRes int id){
        mCaptionText.setText(id);
        notifyView();
    }

    public void setCaptionText(String text){
        mCaptionText.setText(text);
        notifyView();
    }


    public void setTitleColor(@ColorInt int color) {
        mTitleText.setTextColor(color);
        notifyView();
    }


    public void setTitleSize(float dimen){
        mTitleText.setTextSize(Utils.exFloat(dimen, getContext()));
        notifyView();
    }

    public void setTitleSize(@DimenRes int dimen){
        mTitleText.setTextSize(Utils.exFloat(dimen, getContext()));
        notifyView();
    }

    public void setTitleText(@StringRes int titleId){
        mTitleText.setText(titleId);
        notifyView();
    }

    public void setTitleText(String title){
        mTitleText.setText(title);
        notifyView();
    }

    public void setTitleFont(@StringRes int resId){
        setFont(resId, true);
        notifyView();
    }

    public void setCaptionFont(@StringRes int resid){
        setFont(resid, false);
        notifyView();
    }

    private void setFont(@StringRes int titleTfId, boolean isTitle){
        if (titleTfId ==0)return;
        String font = getResources().getString(titleTfId);
        font = "fonts".concat("/" + font);
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), font);
        if (isTitle){
            mTitleText.setTypeface(tf);
        }else{
            mCaptionText.setTypeface(tf);
        }
        notifyView();
    }

    private int findAttrById(@StyleableRes int id){
        return ta.getResourceId(id, 0);
    }


    public TextView getCaptionText() {
        return mCaptionText;
    }

    public TextView getTitleText() {
        return mTitleText;
    }
}
