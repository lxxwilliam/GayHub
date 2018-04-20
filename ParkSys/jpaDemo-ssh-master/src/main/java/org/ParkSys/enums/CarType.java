package org.ParkSys.enums;

public enum CarType {
    small("小车"),
    middle("中型车"),
    big("大型车");

    private String title;

    private CarType(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return this.title;
    }
}
