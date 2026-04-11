package com.example.ondas_be.domain.entity;

public enum Role {

    USER(1, "Regular app user"),
    CONTENT_MANAGER(2, "Can manage songs, albums, artists, lyrics"),
    ADMIN(3, "Full system access");

    private final int id;
    private final String description;

    Role(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthority() {
        return "ROLE_" + this.name();
    }

    public static Role fromId(int id) {
        for (Role role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role id: " + id);
    }
}
