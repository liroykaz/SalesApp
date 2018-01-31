package com.company.salescafe.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ProductStatus implements EnumClass<String> {

    inWork("10"),
    isComplete("20"),
    isAccepted("30");

    private String id;

    ProductStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ProductStatus fromId(String id) {
        for (ProductStatus at : ProductStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}