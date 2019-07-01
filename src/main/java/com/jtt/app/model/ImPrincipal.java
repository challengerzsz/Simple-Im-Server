package com.jtt.app.model;

import java.security.Principal;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 18:16
 */
public class ImPrincipal implements Principal {

    private String uid;

    public ImPrincipal(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getName() {
        return uid;
    }
}
