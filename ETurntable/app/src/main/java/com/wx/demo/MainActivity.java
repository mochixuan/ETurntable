package com.wx.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wx.eturntable.ETurntableMenuView;

public class MainActivity extends AppCompatActivity {

    private ETurntableMenuView mETurntableMenuView;

    private int[] mImgItems = new int[]{R.mipmap.icon_chicken_1,R.mipmap.icon_fox_2,R.mipmap.icon_crab_3,
            R.mipmap.icon_koala_4,R.mipmap.icon_zebra_5,R.mipmap.icon_tiger_6,
            R.mipmap.icon_pig_7,R.mipmap.icon_hippo_8};
    private String[] mTextItems = new String[]{"公鸡","狐狸","螃蟹","考拉","小马","老虎","小猪","河马"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

    }

    private void initView() {
        mETurntableMenuView = (ETurntableMenuView) findViewById(R.id.etb_menu_view);
    }

    private void initData() {
        mETurntableMenuView.setMenuItemIconsAndTexts(mImgItems,mTextItems);
        mETurntableMenuView.setOnMenuItemClickListener(new ETurntableMenuView.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                Toast.makeText(MainActivity.this, mTextItems[pos], Toast.LENGTH_SHORT).show();
            }
        });
    }

}
