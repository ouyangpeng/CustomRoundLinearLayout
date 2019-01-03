package com.oyp.view;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView qrCodeImageView = findViewById(R.id.qrCode);

        String qrContent = getString(R.string.blog);
        //生成普通二维码
        Bitmap bitmap = CreateQRUtils.createNormalQRCode(qrContent,SizeConvertUtil.dpTopx(this, 240));

        qrCodeImageView.setImageBitmap(bitmap);
    }
}
