# 一、背景
今天，美工要求修改某个APP的页面，修改的样式大致如下所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227174616176.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

我们分析下上面的效果，四周是圆角，中间是两个半圆和十几个小圆组成的分隔符。然后其他地方就是一些图片和文字信息。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227174837238.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

因此我决定自定义一个LinearLayout实现四周圆角并且中间有小圆来进行分隔的LinearLayout。
下面来说说具体的实现思路吧。

# 二、实现自定义LinearLayout

实现的空白效果如下，这个自定义LinearLayout上还没有任何其他的字View。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227175119968.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

要实现这个效果，我们得分如下几步骤来实现：
1、绘制四周圆角矩形的白色背景
2、绘制两个半圆
3、绘制18个小圆

## 2.1 绘制四周矩形的白色背景

效果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227175606488.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

代码如下：

```java
public class CustomOypRoundLinearLayout extends LinearLayout {
    /**
     * 背景的圆角
     */
    private int backgroundRadius;

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
        backgroundRadius = SizeConvertUtil.dpTopx(context, 15);
        //想要重写onDraw，就要调用setWillNotDraw（false）

        //ViewGroup默认情况下，出于性能考虑，会被设置成WILL_NOT_DROW，这样，ondraw就不会被执行了。
        // 如果我们想重写一个viewgroup的ondraw方法，有两种方法：
        // 1，构造函数中，给viewgroup设置一个颜色。
        // 2，构造函数中，调用setWillNotDraw（false），去掉其WILL_NOT_DRAW flag。
        // 在viewgroup初始化的时候，它调用了一个私有方法：initViewGroup，它里面会有一句setFlags（WILLL_NOT_DRAW,DRAW_MASK）;
        // 相当于调用了setWillNotDraw（true），所以说，对于ViewGroup，他就认为是透明的了，
        // 如果我们想要重写onDraw，就要调用setWillNotDraw（false）
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //制成一个白色的圆角矩形  作为背景  画2个半圆 和 18个小圆  作为分隔线
        // ==========================    第一步、绘制白色矩形
        RectF backGroundRectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint backGroundPaint = new Paint();
        backGroundPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(backGroundRectF, backgroundRadius, backgroundRadius, backGroundPaint);

        // ==========================    第四步、 让RelativeLayout绘制自己
        super.onDraw(canvas);
    }
}
```
如上代码所示，我们绘制了一个四周圆角15dp的矩形作为LinearLayout的背景。

+ 关于 `setWillNotDraw(false);`方法
 `setWillNotDraw(false);`方法是一定要调用的，如果不调用或者设置为 `setWillNotDraw(true);`的话，自定义的onDraw()方法是不会调用的。那么就会变成下面所示，什么东西都没有！

>原因是： ViewGroup默认情况下，出于性能考虑，会被设置成WILL_NOT_DROW，这样，ondraw就不会被执行了。
 如果我们想重写一个viewgroup的ondraw方法，有两种方法：1，构造函数中，给viewgroup设置一个颜色。 2，构造函数中，调用setWillNotDraw（false），去掉其WILL_NOT_DRAW flag。在viewgroup初始化的时候，它调用了一个私有方法：initViewGroup，它里面会有一句setFlags（WILLL_NOT_DRAW,DRAW_MASK）; 相当于调用了setWillNotDraw（true），
 所以说，对于ViewGroup，他就认为是透明的了，如果我们想要重写onDraw，就要调用setWillNotDraw（false）
 
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227180024508.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

上面转换dp为px的工具类SizeConvertUtil代码如下：

```java
package com.oyp.view;

import android.content.Context;

/**
 *  尺寸转换工具
 * </p>
 * created by OuyangPeng at 2018/12/27 下午 06:38
 * @author OuyangPeng
 */
public class SizeConvertUtil {
    public static int dpTopx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int pxTodp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int pxTosp(Context context, float px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    public static int spTopx(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }
}

```

## 2.2 绘制两个大圆
效果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/2018122718243666.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

代码如下：

```java
public class CustomOypRoundLinearLayout extends LinearLayout {
    /**
     * 大圆、小圆的圆心的Y坐标
     */
    private int circleStartY;

    /**
     * 大圆的半径
     */
    private int bigCircleRadius;
    /**
     * 背景的圆角
     */
    private int backgroundRadius;

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
        circleStartY = SizeConvertUtil.dpTopx(context, 80);

        bigCircleRadius = SizeConvertUtil.dpTopx(context, 8);
        backgroundRadius = SizeConvertUtil.dpTopx(context, 15);
        //想要重写onDraw，就要调用setWillNotDraw（false）

        //ViewGroup默认情况下，出于性能考虑，会被设置成WILL_NOT_DROW，这样，ondraw就不会被执行了。
        // 如果我们想重写一个viewgroup的ondraw方法，有两种方法：
        // 1，构造函数中，给viewgroup设置一个颜色。
        // 2，构造函数中，调用setWillNotDraw（false），去掉其WILL_NOT_DRAW flag。
        // 在viewgroup初始化的时候，它调用了一个私有方法：initViewGroup，它里面会有一句setFlags（WILLL_NOT_DRAW,DRAW_MASK）;
        // 相当于调用了setWillNotDraw（true），所以说，对于ViewGroup，他就认为是透明的了，
        // 如果我们想要重写onDraw，就要调用setWillNotDraw（false）
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //制成一个白色的圆角矩形  作为背景  画2个半圆 和 18个小圆  作为分隔线
        // ==========================    第一步、绘制白色矩形
        RectF backGroundRectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint backGroundPaint = new Paint();
        backGroundPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(backGroundRectF, backgroundRadius, backgroundRadius, backGroundPaint);

        // ==========================    第二步、绘制2个半圆
        Paint circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
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
        RectF rightOval = new RectF(canvas.getWidth() - bigCircleRadius, circleStartY - bigCircleRadius,
                getWidth() + bigCircleRadius, circleStartY + bigCircleRadius);
        canvas.drawArc(rightOval, 90, 180, true, circlePaint);

        // ==========================    第四步、 让RelativeLayout绘制自己
        super.onDraw(canvas);
    }
}
```
## 2.3 绘制一排小圆
绘制玩大圆之后，要绘制一排小圆，小圆的圆心y坐标和大圆一样的。

效果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227182737170.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

代码如下：

```java
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

/**
  * 一个白色四周圆角、中间两个半圆、十几个小圆分隔的LinearLayout
  * </p>
  * created by OuyangPeng at 2018/12/27 下午 05:28
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
        smallCircleCount = 19;
        circleStartY = SizeConvertUtil.dpTopx(context, 80);

        smallCircleStartX = SizeConvertUtil.dpTopx(context, 18);
        smallCircleRadius = SizeConvertUtil.dpTopx(context, 2);

        bigCircleRadius = SizeConvertUtil.dpTopx(context, 8);
        backgroundRadius = SizeConvertUtil.dpTopx(context, 15);
        //想要重写onDraw，就要调用setWillNotDraw（false）

        //ViewGroup默认情况下，出于性能考虑，会被设置成WILL_NOT_DROW，这样，ondraw就不会被执行了。
        // 如果我们想重写一个viewgroup的ondraw方法，有两种方法：
        // 1，构造函数中，给viewgroup设置一个颜色。
        // 2，构造函数中，调用setWillNotDraw（false），去掉其WILL_NOT_DRAW flag。
        // 在viewgroup初始化的时候，它调用了一个私有方法：initViewGroup，它里面会有一句setFlags（WILLL_NOT_DRAW,DRAW_MASK）;
        // 相当于调用了setWillNotDraw（true），所以说，对于ViewGroup，他就认为是透明的了，
        // 如果我们想要重写onDraw，就要调用setWillNotDraw（false）
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //制成一个白色的圆角矩形  作为背景  画2个半圆 和 18个小圆  作为分隔线
        // ==========================    第一步、绘制白色矩形
        RectF backGroundRectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint backGroundPaint = new Paint();
        backGroundPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(backGroundRectF, backgroundRadius, backgroundRadius, backGroundPaint);

        // ==========================    第二步、绘制2个半圆
        Paint circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
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
        RectF rightOval = new RectF(canvas.getWidth() - bigCircleRadius, circleStartY - bigCircleRadius,
                getWidth() + bigCircleRadius, circleStartY + bigCircleRadius);
        canvas.drawArc(rightOval, 90, 180, true, circlePaint);

        // ==========================    第三步、绘制19个小圆  作为分隔线
        // 最后一个小圆和第一个小圆之间 有18段分隔空白
        smallCircleMargin = ((canvas.getWidth() - smallCircleStartX) - (smallCircleStartX)) / (smallCircleCount - 1);
        circlePaint.setColor(Color.parseColor("#dddddd"));
        for (int i = 0; i < smallCircleCount; i++) {
            canvas.drawCircle(smallCircleStartX + i * smallCircleMargin, circleStartY, smallCircleRadius, circlePaint);
        }
        // ==========================    第四步、 让RelativeLayout绘制自己
        super.onDraw(canvas);
    }
}
```


## 2.4 绘制LinearLayout本身上的其他view
在onDraw(Canvas canvas)方法最后一行，调用super.onDraw(canvas);即可绘制LinearLayout本身上的其他view。

```java
// ==========================    第四步、 让RelativeLayout绘制自己
super.onDraw(canvas);
```

## 2.5 在自定义的CustomOypRoundLinearLayout上添加其它view

效果
![在这里插入图片描述](https://img-blog.csdnimg.cn/2018122718313262.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

代码如下：
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#f1f1f1"
    tools:context=".MainActivity">
    <com.oyp.view.CustomOypRoundLinearLayout
        android:id="@+id/layout"
        android:layout_width="320dp"
        android:layout_height="416dp"
        android:layout_centerInParent="true"
        android:layout_marginTop="40dp"
        android:orientation="vertical">

        <RelativeLayout
            android:id="@+id/rl_note"
            android:layout_width="match_parent"
            android:layout_height="75dp">

            <ImageView
                android:id="@+id/babyHead"
                android:layout_width="56dp"
                android:layout_height="56dp"
                android:layout_marginLeft="16dp"
                android:layout_marginTop="11dp"
                android:layout_marginRight="15dp"
                android:src="@drawable/oyp" />

            <TextView
                android:id="@+id/babyNameNote"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="21dp"
                android:layout_toRightOf="@+id/babyHead"
                android:text="欧阳鹏的博客"
                android:textColor="#000000"
                android:textSize="17sp" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_below="@id/babyNameNote"
                android:layout_marginTop="4dp"
                android:layout_toRightOf="@+id/babyHead"
                android:text="https://blog.csdn.net/ouyang_peng/"
                android:textColor="#666666"
                android:textSize="14sp" />
        </RelativeLayout>

        <ImageView
            android:layout_width="240dp"
            android:layout_height="240dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="40dp"
            android:src="@drawable/qr2" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="16dp"
            android:text="一个人，如果你不逼自己一把，你根本不知道自己有多优秀！"
            android:textColor="#888888"
            android:textSize="10sp" />
    </com.oyp.view.CustomOypRoundLinearLayout>
</RelativeLayout>
```

MainActivity代码如下：

```java
package com.oyp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

```
这样我们就实现了美工的新的UI需求了！

# 三、抽取自定义属性

## 3.1 自定义属性定义 

```xml
 <!--自定义View的自定义属性-->
    <declare-styleable name="CustomOypRoundLinearLayout">
        <!-- 大圆的颜色 -->
        <attr name="bigCircleColor" format="color" />
        <!-- 小圆的颜色 -->
        <attr name="smallCircleColor" format="color" />
        <!-- 背景的背景颜色 -->
        <attr name="backgroundColor" format="color" />


        <!--  背景的圆角 -->
        <attr name="backgroundRadius" format="dimension" />
        <!--  大圆的半径 -->
        <attr name="bigCircleRadius" format="dimension" />
        <!--  小圆之间的间距 -->
        <attr name="smallCircleMargin" format="dimension" />
        <!--  小圆的半径 -->
        <attr name="smallCircleRadius" format="dimension" />
        <!--  小圆的个数 -->
        <attr name="smallCircleCount" format="dimension" />
        <!--  第一个小圆的圆心的X坐标 -->
        <attr name="smallCircleStartX" format="dimension" />
        <!--  大圆、小圆的圆心的Y坐标 -->
        <attr name="circleStartY" format="dimension" />
    </declare-styleable>
```

## 3.2 默认的颜色值

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="backgroundColor">#ffffffff</color>
    <color name="smallCircleColor">#dddddd</color>
    <color name="bigCircleColor">#f1f1f1</color>
</resources>
```
## 3.3 默认的尺寸值

```xml
<?xml version="1.0" encoding="utf-8"?>
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <integer name="smallCircleCount">19</integer>
    
    <dimen name="circleStartY">80dp</dimen>
    <dimen name="smallCircleStartX">18dp</dimen>
    <dimen name="smallCircleRadius">2dp</dimen>
    <dimen name="bigCircleRadius">8dp</dimen>
    <dimen name="backgroundRadius">15dp</dimen>
</resources>

```

## 3.4 使用自定义属性

```java
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

        circlePaint.setColor(bigCircleColor);
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



```


## 3.6 测试一下

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:background="#f1f1f1"
    tools:context=".MainActivity">

    <com.oyp.view.CustomOypRoundLinearLayout
        android:id="@+id/layout"
        android:layout_width="320dp"
        android:layout_height="416dp"
        android:layout_centerInParent="true"
        android:layout_marginTop="40dp"
        app:backgroundColor="#87CEFA"
        app:bigCircleColor="#8B0000"
        app:smallCircleColor="#000000"
        android:orientation="vertical">

        <RelativeLayout
            android:id="@+id/rl_note"
            android:layout_width="match_parent"
            android:layout_height="75dp">

            <ImageView
                android:id="@+id/babyHead"
                android:layout_width="56dp"
                android:layout_height="56dp"
                android:layout_marginLeft="16dp"
                android:layout_marginTop="11dp"
                android:layout_marginRight="15dp"
                android:src="@drawable/oyp" />

            <TextView
                android:id="@+id/babyNameNote"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="21dp"
                android:layout_toRightOf="@+id/babyHead"
                android:text="欧阳鹏的博客"
                android:textColor="#000000"
                android:textSize="17sp" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_below="@id/babyNameNote"
                android:layout_marginTop="4dp"
                android:layout_toRightOf="@+id/babyHead"
                android:text="https://blog.csdn.net/ouyang_peng/"
                android:textColor="#666666"
                android:textSize="14sp" />
        </RelativeLayout>

        <ImageView
            android:layout_width="240dp"
            android:layout_height="240dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="40dp"
            android:src="@drawable/qr2" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="16dp"
            android:text="一个人，如果你不逼自己一把，你根本不知道自己有多优秀！"
            android:textColor="#888888"
            android:textSize="10sp" />
    </com.oyp.view.CustomOypRoundLinearLayout>
</RelativeLayout>
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227205215896.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

上面将颜色值改为自定义属性之后，效果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227205258385.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

# 四、扩展：使两个半圆与圆角矩形之间镂空
如下图所示：当绘制两个大半圆的颜色值和整个Activity的背景颜色不一致的时候，两个半圆就很难看。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181227205258385.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190402161753249.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9vdXlhbmdwZW5nLmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

> 如何使两个半圆与圆角矩形之间镂空呢？
我们使用 setXfermode 设置混合模式 来重新写下代码，如下所示：

```java

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


```

效果如下所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019040216252545.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9vdXlhbmdwZW5nLmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
如图所示，两个半圆与圆角矩形之间镂空了，看起来更加好看。

白色矩形和作为分隔线的19个小圆  作为 源图
2个半圆 作为目标图

然后使用 PorterDuff.Mode.XOR 模式
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190402171300195.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9vdXlhbmdwZW5nLmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

# 五、总结

1、我们遇到这种需求，可以考虑自定义LinearLayout，来实现特定的背景
2、要重写自定义LinearLayout的 `onDraw(Canvas canvas)`方法的话，在构造函数里面要调用  `setWillNotDraw(false);`方法，不会 `onDraw(Canvas canvas)`方法不会被调用。
3、使用setXfermode 设置混合模式。



# 六、源代码

+ https://github.com/ouyangpeng/CustomRoundLinearLayout

# 七、参考资料
+ https://www.jianshu.com/p/78c36742d50f
+ https://blog.csdn.net/tianjian4592/article/details/44783283
+ 
------

![](https://img-blog.csdn.net/20150708201910089)

>作者：欧阳鹏 欢迎转载，与人分享是进步的源泉！
转载请保留原文地址：https://blog.csdn.net/ouyang_peng/article/details/85295859
☞ 本人QQ: 3024665621
☞ QQ交流群: 123133153
☞ github.com/ouyangpeng
☞ oypcz@foxmail.com
如果本文对您有所帮助，欢迎您扫码下图所示的支付宝和微信支付二维码对本文进行打赏。


![](https://img-blog.csdn.net/20170413233715262?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvb3V5YW5nX3Blbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
