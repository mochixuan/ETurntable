package com.wx.demo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wx.eturntable.ETurntableMenuView;

public class SixCircleActivity extends AppCompatActivity {

    private ETurntableMenuView mETurntableMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        setContentView(R.layout.activity_six_circle);

        mETurntableMenuView = (ETurntableMenuView) findViewById(R.id.eturnable_view);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.getMinLenght(this),Utils.getMinLenght(this));
        mETurntableMenuView.setLayoutParams(layoutParams);

        mETurntableMenuView.setMenuItemIconsAndTexts(DataConstant.getImages(6),DataConstant.getTexts(6));
        mETurntableMenuView.setOnMenuItemClickListener(new ETurntableMenuView.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                Toast.makeText(SixCircleActivity.this,pos+" "+DataConstant.TextItems[pos],Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initWindows() {

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();

        if (width<height) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

}
