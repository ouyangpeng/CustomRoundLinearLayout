package com.oyp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView qrCodeImageView = findViewById(R.id.qrCode);

        String qrContent = getString(R.string.blog);

        //生成普通二维码
        Bitmap bitmap = CreateQRUtils.createNormalQRCode(qrContent,SizeConvertUtil.dpTopx(this, 240));

        //生成 圆点二维码
//        Bitmap bitmap = CreateQRUtils.createDotQRCode(qrContent,SizeConvertUtil.dpTopx(this, 240));

        //生成 大小随机的圆点二维码
//        Bitmap bitmap = CreateQRUtils.createRandomDotQRCode(qrContent,SizeConvertUtil.dpTopx(this, 240));

        //生成  多边形二维码  边数 3 - 10
//        Bitmap bitmap = CreateQRUtils.createPolygonQRCode(qrContent,SizeConvertUtil.dpTopx(this, 240),3);

//        Bitmap bitmap = CreateQRUtils.createStarQRCode(qrContent,SizeConvertUtil.dpTopx(this, 240),3);

        // 生成 图像二维码
//        Bitmap[] bitmaps = new Bitmap[7];
//        bitmaps[0] = readBitMap(this, R.drawable.deg);
//        bitmaps[1] = readBitMap(this, R.drawable.ebo);
//        bitmaps[2] = readBitMap(this, R.drawable.ecn);
//        bitmaps[3] = readBitMap(this, R.drawable.eco);
//        bitmaps[4] = readBitMap(this, R.drawable.eep);
//        bitmaps[5] = readBitMap(this, R.drawable.eer);
//        bitmaps[6] = readBitMap(this, R.drawable.eft);
//        Bitmap bm = readBitMap(this, R.drawable.kys);
//        Bitmap bitmap = CreateQRUtils.createQRCodeBitmap(qrContent, SizeConvertUtil.dpTopx(this, 240), bitmaps, bm);

        // 生成液态二维码
//        Bitmap bitmap = CreateQRUtils.createQRCodeSmooth(qrContent,SizeConvertUtil.dpTopx(this, 240),1.0f);

        //logo图片
//        Bitmap headBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.oyp);
        //附加icon
//        bitmap = CreateQRUtils.withLogo(bitmap,headBitmap,0.2f);

        //设置二维码到imageView中
        qrCodeImageView.setImageBitmap(bitmap);
    }

    private Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
