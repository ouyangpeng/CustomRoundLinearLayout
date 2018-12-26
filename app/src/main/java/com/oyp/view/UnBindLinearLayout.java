package com.oyp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class UnBindLinearLayout extends LinearLayout {
    private int circleStartY;
    private int smallCircleStartX;
    private int smallCircleRadius;
    private int smallCircleMargin;
    private int bigCircleRadius;
    private int leftBigCircleStartX;
    private int leftBigCircleStartY;
    private int rightBigCircleStartX;
    private int rightBigCircleStartY;

    public UnBindLinearLayout(Context context) {
        this(context, null);
    }

    public UnBindLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnBindLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public UnBindLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        circleStartY = SizeConvertUtil.dpTopx(context, 80);

        smallCircleStartX = SizeConvertUtil.dpTopx(context, 20);
        smallCircleRadius = SizeConvertUtil.dpTopx(context, 2);
        smallCircleMargin = SizeConvertUtil.dpTopx(context, 16);

        leftBigCircleStartX = SizeConvertUtil.dpTopx(context, 8);
        rightBigCircleStartX = SizeConvertUtil.dpTopx(context, 318);
        bigCircleRadius = SizeConvertUtil.dpTopx(context, 8);

        setWillNotDraw(false);//设置调用onDraw方法

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //制成一个白色的圆角矩形  作为背景
        RectF backGroundRectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint backGroundPaint = new Paint();
        backGroundPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(backGroundRectF, 30, 30, backGroundPaint);

        // 画2个半圆 和 18个小圆  作为分隔线
        Paint circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        // 1.画2个半圆
        circlePaint.setColor(Color.parseColor("#f1f1f1"));
        // 圆弧的外轮廓矩形区域
        RectF leftOval = new RectF(-bigCircleRadius, circleStartY - bigCircleRadius,
                bigCircleRadius, circleStartY + bigCircleRadius);
        /*
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
         * oval :指定圆弧的外轮廓矩形区域。
         * startAngle: 圆弧起始角度，单位为度。
         * sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         * useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。关键是这个变量，下面将会详细介绍。
         * paint: 绘制圆弧的画板属性，如颜色，是否填充等。
         */
        //左边的半圆
        canvas.drawArc(leftOval, -90, 180, true, circlePaint);

        //右边的半圆
        RectF rightOval = new RectF(getWidth() - bigCircleRadius, circleStartY - bigCircleRadius,
                getWidth() + bigCircleRadius, circleStartY + bigCircleRadius);
        canvas.drawArc(rightOval, 90, 180, true, circlePaint);


        // 2.画18个小圆  作为分隔线

        circlePaint.setColor(Color.parseColor("#dddddd"));
        for (int i = 0; i < 18; i++) {
            canvas.drawCircle(smallCircleStartX + i * smallCircleMargin, circleStartY, smallCircleRadius, circlePaint);
        }


        //让RelativeLayout绘制自己
        super.onDraw(canvas);
    }

}
