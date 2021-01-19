package com.qyf.rpc.eunm;

public enum RemotingType {

    Http("http"),
    Netty("netty"),;

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    RemotingType(String val) {
        this.val = val;
    }
}
