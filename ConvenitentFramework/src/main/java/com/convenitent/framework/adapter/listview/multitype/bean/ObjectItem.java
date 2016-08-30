package com.convenitent.framework.adapter.listview.multitype.bean;

/**
 * Created by yangboqing on 16/8/24.
 */
public class ObjectItem {

    private int type;
    private Object obj;
    private int layoutId = -1;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }
}
