package ru.vinogradiya.utils.mapping;

public abstract class ItemViews {

    public static class UserAccess {}
    public static class AdminAccess extends UserAccess {};
    public static class Private extends AdminAccess {}
}