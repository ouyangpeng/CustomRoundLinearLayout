package com.oyp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 一个白色四周圆角、中间两个半圆、十几个小圆分隔的LinearLayout
 * </p>
 * created by OuyangPeng at 2018/12/27 下午 05:28
 *
 * @author OuyangPeng
 */
public class CustomOypRoundLinearLayout extends LinearLayout {
    /**
     * 大圆、小圆的圆心的Y坐标
     */
    private int circleStartY;
    /**
     * 第一个小圆的圆心的X坐标
     */
    private int smallCircleStartX;
    /**
     * 小圆的个数
     */
    private int smallCircleCount;
    /**
     * 小圆的半径
     */
    private int smallCircleRadius;
    /**
     * 小圆之间的间距
     */
    private int smallCircleMargin;
    /**
     * 大圆的半径
     */
    private int bigCircleRadius;
    /**
     * 背景的圆角
     */
    private int backgroundRadius;
    /**
     * 背景的背景颜色
     */
    private int backgroundColor;
    /**
     * 小圆的颜色
     */
    private int smallCircleColor;
    /**
     * 大圆的颜色
     */
    private int bigCircleColor;


    private RectF backGroundRectF;
    private Paint backGroundPaint;
    private Paint circlePaint;

    private RectF leftOval;
    private RectF rightOval;

    public CustomOypRoundLinearLayout(Context context) {
        this(context, null);
    }

    public CustomOypRoundLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomOypRoundLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public CustomOypRoundLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        //初始化自定义属性
        initTypeArray(context, attrs);
        //想要重写onDraw，就要调用setWillNotDraw（false）
        //ViewGroup默认情况下，出于性能考虑，会被设置成WILL_NOT_DROW，这样，ondraw就不会被执行了。
        // 如果我们想重写一个viewgroup的ondraw方法，有两种方法：
        // 1，构造函数中，给viewgroup设置一个颜色。
        // 2，构造函数中，调用setWillNotDraw（false），去掉其WILL_NOT_DRAW flag。
        // 在viewgroup初始化的时候，它调用了一个私有方法：initViewGroup，它里面会有一句setFlags（WILLL_NOT_DRAW,DRAW_MASK）;
        // 相当于调用了setWillNotDraw（true），所以说，对于ViewGroup，他就认为是透明的了，
        // 如果我们想要重写onDraw，就要调用setWillNotDraw（false）
        setWillNotDraw(false);


        backGroundRectF = new RectF();

        backGroundPaint = new Paint();
        backGroundPaint.setColor(backgroundColor);

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(bigCircleColor);

        leftOval = new RectF();
        rightOval = new RectF();

    }

    private void initTypeArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomOypRoundLinearLayout);
        //个数
        smallCircleCount = typedArray.getInteger(R.styleable.CustomOypRoundLinearLayout_smallCircleCount,
                getResources().getInteger(R.integer.smallCircleCount));
        //尺寸
        circleStartY = typedArray.getDimensionPixelOffset(R.styleable.CustomOypRoundLinearLayout_circleStartY,
                getResources().getDimensionPixelOffset(R.dimen.circleStartY));
        smallCircleStartX = typedArray.getDimensionPixelOffset(R.styleable.CustomOypRoundLinearLayout_smallCircleStartX,
                getResources().getDimensionPixelOffset(R.dimen.smallCircleStartX));
        smallCircleRadius = typedArray.getDimensionPixelOffset(R.styleable.CustomOypRoundLinearLayout_smallCircleRadius,
                getResources().getDimensionPixelOffset(R.dimen.smallCircleRadius));
        bigCircleRadius = typedArray.getDimensionPixelOffset(R.styleable.CustomOypRoundLinearLayout_bigCircleRadius,
                getResources().getDimensionPixelOffset(R.dimen.bigCircleRadius));
        backgroundRadius = typedArray.getDimensionPixelOffset(R.styleable.CustomOypRoundLinearLayout_backgroundRadius,
                getResources().getDimensionPixelOffset(R.dimen.backgroundRadius));
        //颜色
        backgroundColor = typedArray.getColor(R.styleable.CustomOypRoundLinearLayout_backgroundColor,
                getResources().getColor(R.color.backgroundColor));
        smallCircleColor = typedArray.getColor(R.styleable.CustomOypRoundLinearLayout_smallCircleColor,
                getResources().getColor(R.color.smallCircleColor));
        bigCircleColor = typedArray.getColor(R.styleable.CustomOypRoundLinearLayout_bigCircleColor,
                getResources().getColor(R.color.bigCircleColor));
        //回收typedArray
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //制成一个白色的圆角矩形  作为背景  画2个半圆 和 18个小圆  作为分隔线
        // ==========================    第一步、绘制白色矩形
        backGroundRectF.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(backGroundRectF, backgroundRadius, backgroundRadius, backGroundPaint);

        // ==========================    第二步、绘制2个半圆

        // 圆弧的外轮廓矩形区域
        leftOval.set(-bigCircleRadius, circleStartY - bigCircleRadius,
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
        rightOval.set(getWidth() - bigCircleRadius, circleStartY - bigCircleRadius,
                getWidth() + bigCircleRadius, circleStartY + bigCircleRadius);
        canvas.drawArc(rightOval, 90, 180, true, circlePaint);

        // ==========================    第三步、绘制19个小圆  作为分隔线
        // 最后一个小圆和第一个小圆之间 有18段分隔空白
        smallCircleMargin = ((getWidth() - smallCircleStartX) - (smallCircleStartX)) / (smallCircleCount - 1);
        circlePaint.setColor(smallCircleColor);
        for (int i = 0; i < smallCircleCount; i++) {
            canvas.drawCircle(smallCircleStartX + i * smallCircleMargin, circleStartY, smallCircleRadius, circlePaint);
        }
        // ==========================    第四步、 让RelativeLayout绘制自己
        super.onDraw(canvas);
    }
}
