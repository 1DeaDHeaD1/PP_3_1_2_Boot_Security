package ru.kata.spring.boot_security.demo.constant;

public enum RoleConst {

    ADMIN("ADMIN"),
    USER("USER");

    private final String title;

    RoleConst(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
