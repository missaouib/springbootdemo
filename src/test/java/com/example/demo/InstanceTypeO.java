package com.example.demo;

public enum InstanceTypeO {
    OWN(0),
    SHARED(1);

    private int type;

    InstanceTypeO(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isOwn(Integer instanceType) {
        return instanceType == null || instanceType == OWN.type;
    }

    public static boolean isShare(Integer instanceType) {
        return instanceType != null && instanceType == SHARED.type;
    }
}