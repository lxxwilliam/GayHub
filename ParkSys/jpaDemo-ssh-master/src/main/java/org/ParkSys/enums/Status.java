package org.ParkSys.enums;

public enum Status {
    notExist("不存在"),
    exist("存在");

    private String title;

    private Status(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
