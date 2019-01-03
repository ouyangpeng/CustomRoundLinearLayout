package com.oyp.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * 生成二维码类
 * </p>
 * created by OuyangPeng at 2019/1/3 上午 11:55
 *
 * @author OuyangPeng
 */
public class CreateQRUtils {
    /**
     * 字符编码：UTF-8
     */
    private final static String CHARSET_UTF_8 = "utf-8";
    private final static String TAG = "CreateQRUtils";

    public CreateQRUtils() {
    }

    /**
     * 生成液态二维码
     * 源代码来自：https://github.com/Y-bao/QRCodeYbao/blob/master/Zxing/src/com/ybao/zxing/CreateDCode.java
     *
     * @param content 二维码原始内容
     * @param size    二维码大小
     * @param psR     二维码平滑角度
     * @return 液态二维码
     */
    public static Bitmap createQRCodeSmooth(String content, int size, float psR) {
        try {

            // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败

            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            //矫错等级
            //分为四个等级：L/M/Q/H, 等级越高，容错率越高，识别速度降低。例如一个角被损坏，容错率高的也许能够识别出来。通常为H
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //编码集，通常有中文，设置为 utf-8
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET_UTF_8);
            //默认为4, 实际效果并不是填写的值，一般默认值就行
            hints.put(EncodeHintType.MARGIN, 0);
            // 生成QR二维码数据——这里只是得到一个由true和false组成的数组
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);

            Rect codeRect = new Rect();
            int cellWidth = checkParam(matrix, codeRect);

            int width = matrix.getWidth();
            int height = matrix.getHeight();

            Paint bPaint = new Paint();
            bPaint.setColor(Color.BLACK);
            bPaint.setStyle(Paint.Style.FILL);
            bPaint.setAntiAlias(true);
            Paint wPaint = new Paint();
            wPaint.setColor(Color.WHITE);
            wPaint.setStyle(Paint.Style.FILL);
            wPaint.setAntiAlias(true);
            int hcellWidth = cellWidth / 2;
            int R = (int) (hcellWidth * psR);
            int startXp = codeRect.left + hcellWidth;
            int startYp = codeRect.top + hcellWidth;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            for (int x = startXp; x <= codeRect.right; x += cellWidth) {
                for (int y = startYp; y <= codeRect.bottom; y += cellWidth) {
                    int brL = x - hcellWidth;
                    int brT = y - hcellWidth;
                    int brR = x + hcellWidth;
                    int brB = y + hcellWidth;
                    int pX = x - cellWidth;
                    int nX = x + cellWidth;
                    int pY = y - cellWidth;
                    int nY = y + cellWidth;
                    boolean l = false;
                    boolean t = false;
                    boolean r = false;
                    boolean b = false;
                    if (pX >= codeRect.left) {
                        l = matrix.get(pX, y);
                    }
                    if (pY >= codeRect.top) {
                        t = matrix.get(x, pY);
                    }
                    if (nX <= codeRect.right) {
                        r = matrix.get(nX, y);
                    }
                    if (nY <= codeRect.bottom) {
                        b = matrix.get(x, nY);
                    }
                    if (matrix.get(x, y)) {
                        boolean tl = false;
                        boolean tr = false;
                        boolean br = false;
                        boolean bl = false;
                        if (pX >= codeRect.left && pY >= codeRect.top) {
                            tl = matrix.get(pX, pY);
                        }
                        if (nX <= codeRect.right && pY >= codeRect.top) {
                            tr = matrix.get(nX, pY);
                        }
                        if (nX <= codeRect.right && nY <= codeRect.bottom) {
                            br = matrix.get(nX, nY);
                        }
                        if (pX >= codeRect.left && nY <= codeRect.bottom) {
                            bl = matrix.get(pX, nY);
                        }
                        Path path = new Path();
                        if (!tl && !t && !l) {
                            path.moveTo(brL, brT + R);
                            path.arcTo(new RectF(brL, brT, brL + 2 * R, brT + 2 * R), -180, 90, false);
                        } else {
                            path.moveTo(brL, brT);
                        }
                        if (!tr && !t && !r) {
                            path.lineTo(brR - R, brT);
                            path.arcTo(new RectF(brR - 2 * R, brT, brR, brT + 2 * R), -90, 90, false);
                        } else {
                            path.lineTo(brR, brT);
                        }
                        if (!br && !b && !r) {
                            path.lineTo(brR, brB - R);
                            path.arcTo(new RectF(brR - 2 * R, brB - 2 * R, brR, brB), 0, 90, false);
                        } else {
                            path.lineTo(brR, brB);
                        }
                        if (!bl && !b && !l) {
                            path.lineTo(brL + R, brB);
                            path.arcTo(new RectF(brL, brB - 2 * R, brL + 2 * R, brB), 90, 90, false);
                        } else {
                            path.lineTo(brL, brB);
                        }
                        path.close();
                        canvas.drawPath(path, bPaint);
                    } else {
                        if (t && l) {
                            Path path = new Path();
                            path.moveTo(brL, brT + R);
                            path.lineTo(brL, brT);
                            path.lineTo(brL + R, brT);
                            path.arcTo(new RectF(brL, brT, brL + 2 * R, brT + 2 * R), -90, -90, false);
                            path.close();
                            canvas.drawPath(path, bPaint);
                        }
                        if (t && r) {
                            Path path = new Path();
                            path.moveTo(brR - R, brT);
                            path.lineTo(brR, brT);
                            path.lineTo(brR, brT + R);
                            path.arcTo(new RectF(brR - 2 * R, brT, brR, brT + 2 * R), 0, -90, false);
                            path.close();
                            canvas.drawPath(path, bPaint);
                        }
                        if (b && r) {
                            Path path = new Path();
                            path.moveTo(brR, brB - R);
                            path.lineTo(brR, brB);
                            path.lineTo(brR - R, brB);
                            path.arcTo(new RectF(brR - 2 * R, brB - 2 * R, brR, brB), 90, -90, false);
                            path.close();
                            canvas.drawPath(path, bPaint);
                        }
                        if (b && l) {
                            Path path = new Path();
                            path.moveTo(brL + R, brB);
                            path.lineTo(brL, brB);
                            path.lineTo(brL, brB - R);
                            path.arcTo(new RectF(brL, brB - 2 * R, brL + 2 * R, brB), 180, -90, false);
                            path.close();
                            canvas.drawPath(path, bPaint);
                        }
                    }
                }
            }
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }


    /**
     * 获取二维码位置信息
     * <p>
     * 原文：https://blog.csdn.net/oyuanwa/article/details/51197476
     * <p>
     * 通过遍历 BitMatrix（也可以使用图片的像素点，可以直接用一个生成好的二维码来生成新的艺术二维码）
     * 找到第一个true值点 （如果用像素点，就是找到第一个带色点），该点的坐标就是起始坐标，找到同行最后一个true值点，
     * 该点的x就是 结束点的x，找到同列最后一个true值点，该点的y就是 结束点的y；
     * 从起始点开始，延对角线找，找到第一个false值点，该点到起始点的单一方向上的距离 就是 一个信息点的宽。
     * <p>
     * 定位区的宽＝7个信息点的宽＝7*cellWidth；
     * 通过定位区的宽可以避免修改到它们 或者 对它们使用不同的修改方式
     * 通过信息点的宽 用来 找到每个信息点 并替换它们
     * <p>
     * 液化二维码原理
     * 判断某个信息点的周围点的情况，在对应角上加上圆弧；
     * 如
     * 某一信息点的左上没有信息点且同时左边和上面同时也没有信息点，说明该点左上角位凸角，要切出圆角；
     * 某一信息点的AB角没有信息点且同时A面和B面同时也没有信息点，说明该点AB角位凸角，要切出圆角；
     * <p>
     * 某一空白点的左边和上面同时具有信息点，说明该点左上角位凹角，要填充圆角；
     * 某一空白点的A面和B面同时具有信息点，说明该点AB角位凹角，要填充圆角；
     *
     * @param matrix
     * @param rect   带回二维码边界
     * @return 返回单个信息点的宽
     */
    private static int checkParam(BitMatrix matrix, Rect rect) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        int startX = 0;
        int startY = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    startX = x;
                    startY = y;
                    x = width;
                    y = height;
                }
            }
        }
        int endX = 0;
        for (int x = width - 1; x >= 0; x--) {
            if (matrix.get(x, startY)) {
                endX = x;
                x = -1;
            }
        }
        int endY = 0;
        for (int y = height - 1; y >= 0; y--) {
            if (matrix.get(startX, y)) {
                endY = y;
                y = -1;
            }
        }
        int cellWidth = 1;
        while (true) {
            int pX = startX + cellWidth;
            int pY = startY + cellWidth;
            if (pX <= endX && pY <= endY && matrix.get(pX, pY)) {
                cellWidth++;
                continue;
            }
            break;
        }
        rect.left = startX;
        rect.top = startY;
        rect.right = endX;
        rect.bottom = endY;
        return cellWidth;
    }

    /**
     * 生成普通二维码
     *
     * @param content 二维码内容
     * @param size    二维码大小
     * @return 普通二维码
     */
    public static Bitmap createNormalQRCode(String content, int size) {
        try {
            return createNormalQRCode(content, size, 0xff000000, 0x00ffffff);
        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            return null;
        }
    }

    /**
     * 生成普通二维码
     *
     * @param content  二维码内容
     * @param size     二维码大小
     * @param qr_color 二维码颜色
     * @param bg_color 二维码背景颜色
     * @return 普通二维码
     */
    public static Bitmap createNormalQRCode(String content, int size, int qr_color, int bg_color) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        //矫错等级
        //分为四个等级：L/M/Q/H, 等级越高，容错率越高，识别速度降低。例如一个角被损坏，容错率高的也许能够识别出来。通常为H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //编码集，通常有中文，设置为 utf-8
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET_UTF_8);
        //默认为4, 实际效果并不是填写的值，一般默认值就行
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = qr_color;
                } else { // 无信息设置像素点为白色
                    pixels[y * width + x] = bg_color;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
        return bitmap;
    }


    /**
     * 给原始二维码附加一个logo
     *
     * @param qrCode 原始二维码
     * @param logo   要 附加的logo
     * @param scale  附加的logo占原始二维码的百分比,越小越好,二维码有一定的纠错功能，中间图片越小，越容易纠错
     * @return 附加icon后的二维码
     */
    public static Bitmap withLogo(Bitmap qrCode, Bitmap logo, float scale) {
        //如果原二维码为空，返回空
        if (qrCode == null) {
            return null;
        }
        //如果logo为空，返回原二维码
        if (logo == null) {
            return qrCode;
        }
        //这里得到原二维码bitmap的数据
        int qrCodeWidth = qrCode.getWidth();
        int qrCodeHeight = qrCode.getHeight();
        //logo的Width和Height
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        //同样如果为空，返回空
        if (qrCodeWidth == 0 || qrCodeHeight == 0) {
            return null;
        }
        //同样logo大小为0，返回原二维码
        if (logoWidth == 0 || logoHeight == 0) {
            return qrCode;
        }
        //logo大小为二维码整体大小的scale
        float scaleFactor = qrCodeHeight * scale / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(qrCode, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, qrCodeWidth / 2, qrCodeHeight / 2);
            // 开始合成图片
            canvas.drawBitmap(logo, (qrCodeWidth - logoWidth) / 2, (qrCodeHeight - logoHeight) / 2, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return bitmap;
    }


    /**
     * 生成圆点二维码
     *
     * @param content 二维码内容
     * @param size    二维码大小
     * @return 圆点二维码
     */
    public static Bitmap createDotQRCode(String content, int size) {
        try {
            // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET_UTF_8);
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);

            Rect codeRect = new Rect();
            int cellWidth = checkParam(matrix, codeRect);

            int width = matrix.getWidth();
            int height = matrix.getHeight();

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            int hcellWidth = cellWidth / 2;
            int startXp = codeRect.left + hcellWidth;
            int startYp = codeRect.top + hcellWidth;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            for (int x = startXp; x <= codeRect.right; x += cellWidth) {
                for (int y = startYp; y <= codeRect.bottom; y += cellWidth) {
                    if (matrix.get(x, y)) {
                        canvas.drawCircle(x, y, hcellWidth, paint);
                    }
                }
            }
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }


    /**
     * 图像二维码
     *
     * @param content
     * @param size
     * @return
     */
    public static Bitmap createQRCodeBitmap(String content, int size, Bitmap[] bitmaps, Bitmap bitmapKey) {
        if (bitmaps == null || bitmaps.length == 0) {
            return null;
        }
        try {
            int count = bitmaps.length;
            // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET_UTF_8);
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            Rect codeRect = new Rect();
            int cellWidth = checkParam(matrix, codeRect);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            int hcellWidth = cellWidth / 2;
            int startXp = codeRect.left + hcellWidth;
            int startYp = codeRect.top + hcellWidth;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            int pw = 7 * cellWidth;
            for (int x = startXp; x <= codeRect.right; x += cellWidth) {
                for (int y = startYp; y <= codeRect.bottom; y += cellWidth) {
                    if (matrix.get(x, y)) {
                        Bitmap bm = null;
                        if ((x > codeRect.left + pw || y > codeRect.top + pw)
                                && (x < codeRect.right - pw || y > codeRect.top + pw)
                                && (x > codeRect.left + pw || y < codeRect.bottom - pw)) {
                            if (count == 1) {
                                bm = bitmaps[0];
                            } else {
                                int i = (int) (Math.random() * count);
                                if (i >= count) {
                                    i = count - 1;
                                }
                                bm = bitmaps[i];
                            }
                        } else {
                            bm = bitmapKey;
                        }
                        Rect rect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
                        RectF rectf = new RectF(x - hcellWidth, y - hcellWidth, x + hcellWidth, y + hcellWidth);
                        canvas.drawBitmap(bm, rect, rectf, paint);
                    }
                }
            }
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }
}
