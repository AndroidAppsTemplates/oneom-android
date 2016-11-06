package com.iam.oneom.env.widget.text;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class Text extends TextView {

    private float spacing = Spacing.NORMAL;
    private CharSequence originalText = "";

    public Text(Context context) {
        super(context);
        setTextStyle(font.font133);
    }

    public Text(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
//        TypefaceManager.getInstance().applyTypeface(this, context, attributeSet);
        setTextStyle(font.font133);
    }

    public Text(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public void setTextStyle(font font) {
        setTypeface(font.typeface(getContext()));
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }

    public CharSequence getSpacedText() {
        return originalText;
    }

    private void applySpacing() {
        if (this.originalText == null) return;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));
            if(i+1 < originalText.length()) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if(builder.toString().length() > 1) {
            for(int i = 1; i < builder.toString().length(); i+=2) {
                finalText.setSpan(new ScaleXSpan((spacing+1)/10), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }

    public class Spacing {
        public final static float NORMAL = 0;
    }
}