package com.example.demo.commonspool2;

/**
 * 需要池化的对象
 */
public class NeedPooledObject {
    private String name;
    private boolean isActive;

    public NeedPooledObject() {
    }

    public NeedPooledObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void destroy(){

    }
}
