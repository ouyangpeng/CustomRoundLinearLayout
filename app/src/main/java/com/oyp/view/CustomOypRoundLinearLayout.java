package com.oyp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


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

    private int mWidth;
    private int mHeight;
    private PorterDuffXfermode mXfermode;

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

        initPaint();
        initXfermode();
        initRectF();
    }

    private void initPaint() {
        backGroundPaint = new Paint();
        backGroundPaint.setColor(backgroundColor);

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(bigCircleColor);
    }

    private void initXfermode() {
        //禁用硬件加速   https://www.jianshu.com/p/78c36742d50f
        //从硬件加速不支持的函数列表中，我们可以看到AvoidXfermode，PixelXorXfermode是完全不支持的，而PorterDuffXfermode是部分不支持的。
        //如果你的APP跑在API 14版本以后，而你洽好要用那些不支持硬件加速的函数要怎么办？ 那就只好禁用硬件加速喽

        //该方法千万别放到onDraw()方法里面调用，否则会不停的重绘的，因为该方法调用了invalidate() 方法
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);
    }

    private void initRectF() {
        backGroundRectF = new RectF();
        leftOval = new RectF();
        rightOval = new RectF();
    }

    private void initWidthAndHeight() {
        mWidth = getWidth();
        mHeight = getHeight();
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
        initWidthAndHeight();

        //使用离屏绘制  存为新图层
        int layerID = canvas.saveLayer(0, 0, mWidth, mHeight, backGroundPaint, Canvas.ALL_SAVE_FLAG);

        // 绘制目标图
        drawLeftAndRightBigHalfCircle(canvas);
        // 设置图层混合模式
        backGroundPaint.setXfermode(mXfermode);
        // 绘制源图
        drawBackGroundAndSmallCircle(canvas);
        // 清除混合模式
        backGroundPaint.setXfermode(null);
        // 恢复保存的图层；
        canvas.restoreToCount(layerID);
        // ==========================    第四步、 让RelativeLayout绘制自己
        super.onDraw(canvas);
    }

    private void drawBackGroundAndSmallCircle(Canvas canvas) {
        // ========================== 绘制白色矩形
        backGroundRectF.set(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(backGroundRectF, backgroundRadius, backgroundRadius, backGroundPaint);

        // ==========================  绘制19个小圆  作为分隔线
        // 最后一个小圆和第一个小圆之间 有18段分隔空白
        smallCircleMargin = ((mWidth - smallCircleStartX) - (smallCircleStartX)) / (smallCircleCount - 1);
        circlePaint.setColor(smallCircleColor);
        for (int i = 0; i < smallCircleCount; i++) {
            canvas.drawCircle(smallCircleStartX + i * smallCircleMargin, circleStartY, smallCircleRadius, circlePaint);
        }
    }

    private void drawLeftAndRightBigHalfCircle(Canvas canvas) {
        // ========================== 绘制2个半圆
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
        rightOval.set(mWidth - bigCircleRadius, circleStartY - bigCircleRadius,
                mWidth + bigCircleRadius, circleStartY + bigCircleRadius);
        canvas.drawArc(rightOval, 90, 180, true, circlePaint);
    }
}
