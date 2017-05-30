package com.iam.oneom.env.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.iam.oneom.util.Decorator;

public class CircleView extends View {

    private float BORDER_WIDTH = 0;
    private float SHADOW_WIDTH = 0;

    Paint mainPaint;
    Paint outerPaint;

    protected int cx;
    protected int cy;
    protected int r;

    public CircleView(Context context, int diameter) {
        super(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                diameter,
                diameter
        );
        setLayoutParams(params);

        r = cy = cx = diameter / 2;
        setMainParams();
    }

    @SuppressWarnings("ResourceType")
    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] attrsArray = new int[] {
                android.R.attr.layout_width,
                android.R.attr.layout_height,
        };

        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        int w = ta.getDimensionPixelSize(0, ViewGroup.LayoutParams.MATCH_PARENT);

        int h = ta.getDimensionPixelSize(1, ViewGroup.LayoutParams.MATCH_PARENT);
        cx = w /2;
        cy = h /2;
        r = cx > cy ? cy : cx;
        ta.recycle();

        setMainParams();
    }

    public void setColor(int color) {
        mainPaint.setColor(color);
        invalidate();
    }

    public LinearLayout.LayoutParams getLParams() {
        return (LinearLayout.LayoutParams)getLayoutParams();
    }

    public void setMargins(int margin){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.setMargins(margin/2, margin, margin/2, margin);
        setLayoutParams(params);
        invalidate();
    }

    public void hideBorder(){
        BORDER_WIDTH = 0;
        invalidate();
    }

    public void setBorder(int width, int color) {
        BORDER_WIDTH = Decorator.dipToPixels(getContext(), width);
        outerPaint.setColor(color);
        invalidate();
    }

    public void setShadowLayer(float x, float dx, float dy, int color) {
        SHADOW_WIDTH = x;
        outerPaint.setShadowLayer(x, dx, dy, color);
        setLayerType(LAYER_TYPE_SOFTWARE, outerPaint);
//        shadowPaint.setColor(centerColor);
//        shadowPaint.setShader(new RadialGradient(cx, cy, r + SHADOW_WIDTH, centerColor, endColor, Shader.TileMode.MIRROR));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(cx, cy, r, shadowPaint);
        for (int i = 5; i > 0; i--) {
            canvas.drawCircle(cx, cy, r - SHADOW_WIDTH, outerPaint);
            canvas.drawCircle(cx, cy, r - BORDER_WIDTH - SHADOW_WIDTH, mainPaint);
        }
    }


    private void setMainParams() {
        mainPaint = new Paint();
        mainPaint.setColor(Decorator.WHITE);
        outerPaint = new Paint();
        outerPaint.setColor(Decorator.BLACK);
        int padding = (int) Decorator.dipToPixels(getContext(), 4);
        setPadding(padding, padding, padding, padding);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cx = w /2;
        cy = h /2;
        r = cx > cy ? cy : cx;
        invalidate();
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Размер по умолчанию, если ограничения не были установлены.
        int result = 500;

        if (specMode == MeasureSpec.AT_MOST) {
            // Рассчитайте идеальный размер вашего
            // элемента в рамках максимальных значений.
            // Если ваш элемент заполняет все доступное
            // пространство, верните внешнюю границу.
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // Если ваш элемент может поместиться внутри этих границ, верните это значение.
            result = specSize;
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Размер по умолчанию, если ограничения не были установлены.
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            // Рассчитайте идеальный размер вашего
            // элемента в рамках максимальных значений.
            // Если ваш элемент заполняет все доступное
            // пространство, верните внешнюю границу.
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // Если ваш элемент может поместиться внутри этих границ, верните это значение.
            result = specSize;
        }
        return result;
    }
}
