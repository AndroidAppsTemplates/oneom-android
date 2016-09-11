package com.iam.oneom.env.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.iam.oneom.R;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.env.widget.text.font;

import java.util.ArrayList;

public class TagBar extends LinearLayout {

//    ArrayList<Text> tags;

    public TagBar(Context context) {
        super(context);

        init();
    }

    public TagBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void addTags(ArrayList<String> tags) {
        this.removeAllViewsInLayout();
//        if (this.tags.isEmpty()) {
            for (String tag : tags) {
                Text tagTextView = makeTag(tag);
//                this.tags.add(tagTextView);
                this.addView(tagTextView);
//            }
        }
    }

    private void init() {
//        this.tags = new ArrayList<>();
        setOrientation(HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = MarginLayoutParams.MATCH_PARENT;
            layoutParams.height = MarginLayoutParams.WRAP_CONTENT;
        } else {
            setLayoutParams(new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT));
        }
    }

    private Text makeTag(String text) {
        Text tv = new Text(getContext());
        tv.setText(text);
        tv.setBackgroundResource(R.drawable.tag_rounded_corners_background);
        tv.setTextStyle(font.font133sb);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
        tv.setLayoutParams(new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT));
        tv.setPadding(4, 4, 4, 4);
        tv.setTextColor(Decorator.TXTBLUE);
        return tv;
    }
}
