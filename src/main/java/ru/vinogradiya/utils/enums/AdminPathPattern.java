package ru.vinogradiya.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminPathPattern implements PathPattern {

    PRODUCT("/api/v1/admin/products", "/v1/admin/products"),
    SELECTION("/api/v1/admin/selections", "/v1/admin/selections");

    private final String apiPath;
    private final String mvcPath;
}