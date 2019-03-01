package com.example.list_dome;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class list extends BmobObject {
    private  String name;
    private BmobFile icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getIcon() {
        return icon;
    }
    public String getIconUrl(){
        return icon.getFileUrl();
    }
    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }
}