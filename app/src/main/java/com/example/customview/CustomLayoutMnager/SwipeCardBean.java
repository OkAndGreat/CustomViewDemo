package com.example.customview.CustomLayoutMnager;

import java.util.ArrayList;
import java.util.List;

public class SwipeCardBean {
    private int postition;
    private String url;
    private String name;

    public SwipeCardBean(int postition, String url, String name) {
        this.postition = postition;
        this.url = url;
        this.name = name;
    }

    public int getPostition() {
        return postition;
    }

    public SwipeCardBean setPostition(int postition) {
        this.postition = postition;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public SwipeCardBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getName() {
        return name;
    }

    public SwipeCardBean setName(String name) {
        this.name = name;
        return this;
    }

    public static List<SwipeCardBean> initDatas() {
        List<SwipeCardBean> datas = new ArrayList<>();
        int i = 1;
        datas.add(new SwipeCardBean(i++, "http://img.netbian.com/file/2021/0503/smalled091e032223ba8d8e6959e60271c8701620055005.jpg", "美女1"));
        datas.add(new SwipeCardBean(i++, "http://img.netbian.com/file/2021/0407/smallbbeb94fc620c1d254f1fa9ab2a7fb4871617806248.jpg", "美女2"));
        datas.add(new SwipeCardBean(i++, "http://img.netbian.com/file/2021/0323/smalldb75e03d9955af84dbd9e80d20fb7e301616431847.jpg", "美女3"));
        datas.add(new SwipeCardBean(i++, "http://img.netbian.com/file/2021/0317/small9398baebee6da75ce10cc5dedc8f074f1615913170.jpg", "美女4"));
        datas.add(new SwipeCardBean(i++, "http://img.netbian.com/file/2021/0317/small4120bc42500eb9be6082f23b6071c1c51615913054.jpg", "美女5"));
        datas.add(new SwipeCardBean(i++, "http://img.netbian.com/file/2021/0310/smallb65037577ff1affdbdd9fc804f78fa1f1615386288.jpg", "美女6"));
        datas.add(new SwipeCardBean(i++, "http://img.netbian.com/file/2021/0122/small2861bb5516bd41b0dfe79f6a9538892d1611328336.jpg", "美女7"));
        datas.add(new SwipeCardBean(i++, "http://img.netbian.com/file/2020/0814/small04f08e79c7dbdfd2c394df768da784bb1597419809.jpg","美女8"));
        return datas;
    }
}
