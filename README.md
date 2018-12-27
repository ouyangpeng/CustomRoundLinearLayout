# CustomRoundLinearLayout
实现一个四周圆角并且中间有小圆来进行分隔的自定义LinearLayout



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
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomOypRoundLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomOypRoundLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomOypRoundLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

# 三、总结

1、我们遇到这种需求，可以考虑自定义LinearLayout，来实现特定的背景
2、要重写自定义LinearLayout的 `onDraw(Canvas canvas)`方法的话，在构造函数里面要调用  `setWillNotDraw(false);`方法，不会 `onDraw(Canvas canvas)`方法不会被调用。
3、本文没有使用自定义属性，因为目前该自定义的LinearLayout使用的场景只有一个，而且没有什么变更，所以暂时未使用自定义属性，直接写死的。如果后面有变更的话，还是抽取出自定义属性比较好。

------

![](https://img-blog.csdn.net/20150708201910089)

>作者：欧阳鹏 欢迎转载，与人分享是进步的源泉！
转载请保留原文地址：https://blog.csdn.net/qq446282412/article/details/85209203
☞ 本人QQ: 3024665621
☞ QQ交流群: 123133153
☞ github.com/ouyangpeng
☞ oypcz@foxmail.com
如果本文对您有所帮助，欢迎您扫码下图所示的支付宝和微信支付二维码对本文进行打赏。
--------------------- 
作者：欧阳鹏 
来源：CSDN 
原文：https://blog.csdn.net/qq446282412/article/details/85295859
版权声明：本文为博主原创文章，转载请附上博文链接！

![](https://img-blog.csdn.net/20170413233715262?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvb3V5YW5nX3Blbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
