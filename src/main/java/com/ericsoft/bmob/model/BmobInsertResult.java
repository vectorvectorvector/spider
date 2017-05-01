package com.ericsoft.bmob.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/29/0029.
 */
public class BmobInsertResult {
    private Date createdAt;//创建时间
    private String objectId;//ID

    public BmobInsertResult() {
        createdAt = null;
        objectId = "";
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
