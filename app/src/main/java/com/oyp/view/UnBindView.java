package com.oyp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class UnBindView extends View {
    /**
     * 宝贝头像
     */
    private Bitmap babyHeadImage;
    /**
     * 宝贝头像 大小
     */
    private int babyHeadImageSize;
    /**
     * 宝贝头像 上边距
     */
    private int babyHeadImageMarginTop;
    /**
     * 宝贝头像 左边距
     */
    private int babyHeadImageMarginleft;
    /**
     * 宝贝头像 右边距
     */
    private int babyHeadImageMarginright;

    /**
     * 宝贝名称提示（张子馨的二维码）
     */
    private String babyNameNote;
    /**
     * 宝贝名称提示 文字颜色
     */
    private int babyNameNoteColor;
    /**
     * 宝贝名称提示 文字大小
     */
    private int babyNameNoteSize;

    /**
     * 绑定号提示（绑定号：xxxxxxxxxx）
     */
    private String bindNumberNote;
    /**
     * 绑定号提示 文字颜色
     */
    private int bindNumberNoteColor;
    /**
     * 绑定号提示 文字大小
     */
    private int bindNumberNoteSize;


    /**
     * 大圆 的 半径
     */
    private int bigCircularSize;
    /**
     * 小圆 的 半径
     */
    private int smallCircularSize;
    /**
     * 小圆 与 小圆 之间的间距
     */
    private int circularMarginSize;
    /**
     * 大圆 与 小圆 的原点 纵坐标距离顶部的距离
     */
    private int circularCenterMargintop;

    /**
     * 二维码图片
     */
    private Bitmap qrImageSrc;
    /**
     * 二维码图片 左上角的纵坐标距离顶部的距离
     */
    private int qrImageMarginTop;
    /**
     * 二维码图片大小
     */
    private int qrImageMarginSize;


    /**
     * 扫码提示（扫码绑定手表）
     */
    private String scanNote;
    /**
     * 扫码提示 文字颜色
     */
    private int scanNoteColor;
    /**
     * 扫码提示 文字大小
     */
    private int scanNoteSize;
    /**
     * 扫码提示 纵坐标距离上面的距离
     */
    private int scanNoteMarginTop;


    public UnBindView(Context context) {
        this(context, null);
    }

    public UnBindView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnBindView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public UnBindView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTypeArray(context, attrs);
    }

    private void initTypeArray(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnBindView);
        final BitmapDrawable bitmapdraw = (BitmapDrawable) typedArray.getDrawable(R.styleable.UnBindView_baby_head_image_src);
        if (bitmapdraw != null) {
            babyHeadImage = bitmapdraw.getBitmap();
        }
//        else {
//            //默认的头像
//            babyHeadImage = WatchHeadUtils.getHeadBitMapByWatchId(context.getApplicationContext(), null, R.drawable.bab_head_30k);
//        }

        babyHeadImageSize = typedArray.getDimensionPixelOffset(R.styleable.UnBindView_baby_head_image_size,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_baby_head_image_size));
        babyHeadImageMarginTop = typedArray.getDimensionPixelOffset(R.styleable.UnBindView_baby_head_image_margin_top,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_baby_head_image_margin_top));
        babyHeadImageMarginleft = typedArray.getDimensionPixelOffset(R.styleable.UnBindView_baby_head_image_margin_left,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_baby_head_image_margin_left));
        babyHeadImageMarginright = typedArray.getDimensionPixelOffset(R.styleable.UnBindView_baby_head_image_margin_right,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_baby_head_image_margin_right));

        babyNameNote = typedArray.getString(R.styleable.UnBindView_baby_name_note);
        babyNameNoteColor = typedArray.getColor(R.styleable.UnBindView_baby_name_note_color,
                getResources().getColor(R.color.unbindview_baby_name_note_color));
        babyNameNoteSize = typedArray.getDimensionPixelSize(R.styleable.UnBindView_baby_name_note_size,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_baby_name_note_size));

        bindNumberNote = typedArray.getString(R.styleable.UnBindView_bind_number_note);
        bindNumberNoteColor = typedArray.getColor(R.styleable.UnBindView_bind_number_note_color,
                getResources().getColor(R.color.unbindview_bind_number_note_color));
        bindNumberNoteSize = typedArray.getDimensionPixelSize(R.styleable.UnBindView_bind_number_note_size,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_bind_number_note_size));

        bigCircularSize = typedArray.getDimensionPixelSize(R.styleable.UnBindView_big_circular_size,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_big_circular_size));
        smallCircularSize = typedArray.getDimensionPixelSize(R.styleable.UnBindView_small_circular_size,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_small_circular_size));
        circularMarginSize = typedArray.getDimensionPixelSize(R.styleable.UnBindView_circular_margin_size,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_circular_margin_size));
        circularCenterMargintop = typedArray.getDimensionPixelSize(R.styleable.UnBindView_circular_center_margin_top,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_circular_center_margin_top));

        final BitmapDrawable qrBitmapdraw = (BitmapDrawable) typedArray.getDrawable(R.styleable.UnBindView_qr_image_src);
        if (qrBitmapdraw != null) {
            qrImageSrc = qrBitmapdraw.getBitmap();
        }
        qrImageMarginSize = typedArray.getDimensionPixelSize(R.styleable.UnBindView_qr_image_margin_size,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_qr_image_margin_size));
        qrImageMarginTop = typedArray.getDimensionPixelSize(R.styleable.UnBindView_qr_image_margin_top,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_qr_image_margin_top));


        scanNote = typedArray.getString(R.styleable.UnBindView_scan_note);
        scanNoteColor = typedArray.getColor(R.styleable.UnBindView_scan_note_color,
                getResources().getColor(R.color.unbindview_scan_note_color));
        scanNoteSize = typedArray.getDimensionPixelSize(R.styleable.UnBindView_scan_note_size,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_scan_note_size));
        scanNoteMarginTop = typedArray.getDimensionPixelSize(R.styleable.UnBindView_scan_note_margin_top,
                getResources().getDimensionPixelOffset(R.dimen.unbindview_scan_note_margin_top));

        //回收typedArray
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //制成一个白色的圆角矩形
        RectF backgroupRectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint _paint = new Paint();
        _paint.setColor(Color.WHITE);
        //todo 下面的 圆角半径 到时候 放到自定义属性中
//        public void drawRoundRect (RectF rect, float rx, float ry, Paint paint)
//        rect：RectF对象。
//        rx：x方向上的圆角半径。
//        ry：y方向上的圆角半径。
//        paint：绘制时所使用的画笔。
        canvas.drawRoundRect(backgroupRectF, 30, 30, _paint);

        _paint.setColor(babyNameNoteColor);
        _paint.setTextSize(babyNameNoteSize);
        canvas.drawText(babyNameNote,90,40,_paint);

        _paint.setColor(bindNumberNoteColor);
        _paint.setTextSize(bindNumberNoteSize);
        canvas.drawText(bindNumberNote,90,70,_paint);


    }

}
