package ru.vinogradiya.utils.mapping;

public interface ItemMapper<E, T> {

    T toDomain(E entity);
}