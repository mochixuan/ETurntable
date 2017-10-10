package com.wx.demo;

public class DataConstant {

    public static int[] ImgaeItems = new int[]{R.mipmap.icon_chicken_1,R.mipmap.icon_fox_2,R.mipmap.icon_crab_3,
            R.mipmap.icon_koala_4,R.mipmap.icon_zebra_5,R.mipmap.icon_tiger_6,
            R.mipmap.icon_pig_7,R.mipmap.icon_hippo_8};
    public static String[] TextItems = new String[]{"公鸡","狐狸","螃蟹","考拉","小马","老虎","小猪","河马"};

    public static int[] getImages(int count) {
        int[] images = new int[count];
        for (int i=0;i<count;i++) {
            images[i] = ImgaeItems[i];
        }
        return images;
    }

    public static String[] getTexts(int count) {
        String[] texts = new String[count];
        for (int i=0;i<count;i++) {
            texts[i] = TextItems[i];
        }
        return texts;
    }

}
