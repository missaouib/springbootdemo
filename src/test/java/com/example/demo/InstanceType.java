package com.example.demo;

public enum InstanceType {
    OWN     ((byte) 0),
    SHARED  ((byte) 1);

    private byte type;

    InstanceType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public static boolean isOwn(Byte instanceType) {
        return instanceType == null || instanceType == OWN.type;
    }

    public static boolean isShare(Byte instanceType) {
        return instanceType != null && instanceType == SHARED.type;
    }
}