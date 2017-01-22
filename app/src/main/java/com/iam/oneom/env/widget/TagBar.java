package com.iam.oneom.env.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.env.widget.text.font;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.ButterKnife;

public class TagBar extends LinearLayout {

    private static final String TAG = TagBar.class.getSimpleName();

    @BindInt(R.integer.tag_bar_text_size)
    int tagTextSize;
    @BindDimen(R.dimen.tag_bar_text_padding)
    int tagTextPadding;
    @BindDimen(R.dimen.tag_bar_text_margin)
    int tagTextMargin;
    @BindColor(R.color.tag_bar_text_color)
    int tagTextColor;

    List<LinearLayout> layouts;
    int currentLayout;
    int textWidthes;

    int width;

    public TagBar(Context context) {
        super(context);
        init();
    }

    public TagBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void addTags(ArrayList<String> tags) {
        if (layouts == null) {
            for (String tag : tags) {
                TextView tagTextView = makeTag(tag);
                addViewToLayout(tagTextView);
            }
        }
    }

    public void addViewToLayout(View child) {

        if (child == null) {
            return;
        }

        if (layouts == null) {
            layouts = new ArrayList<>();
            layouts.add(getNewLayout());
            currentLayout = 0;
        }

        if (width < textWidthes + child.getMeasuredWidth()) {
            layouts.add(getNewLayout());
            currentLayout++;
            addViewToLayout(child);
        } else {
            layouts.get(currentLayout).addView(child);
            textWidthes += child.getWidth();
        }

    }

    @NonNull
    private LinearLayout getNewLayout() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));
        addView(linearLayout);
        return linearLayout;
    }

    private void init() {
        ButterKnife.bind(this);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                width = getWidth();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        setOrientation(VERTICAL);
        measure(0, 0);
        width = getMeasuredWidth();
    }

    private TextView makeTag(String text) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setBackgroundResource(R.drawable.tag_rounded_corners_background);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, tagTextSize);
        tv.setPadding(2 * tagTextPadding, tagTextPadding, 2 * tagTextPadding, tagTextPadding);
        tv.setTextColor(tagTextColor);
        return tv;
    }
}
