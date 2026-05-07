package ru.vinogradiya.models.dto;

public abstract class ItemViews {

    public static class UserAccess {}
    public static class AdminAccess extends UserAccess {};
    public static class Private extends AdminAccess {}
}