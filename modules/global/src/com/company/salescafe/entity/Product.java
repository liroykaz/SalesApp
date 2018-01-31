package com.company.salescafe.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.annotations.Composition;

@NamePattern("%s|productName")
@Table(name = "SALESCAFE_PRODUCT")
@Entity(name = "salescafe$Product")
public class Product extends StandardEntity {
    private static final long serialVersionUID = 1059390248643310069L;

    @Column(name = "PRODUCT_NAME")
    protected String productName;

    @Column(name = "PRODUCT_PRICE")
    protected Integer productPrice;

    @Column(name = "IS_NOT_AVAILABLE")
    protected Boolean isNotAvailable;

    @Column(name = "RESIDUE")
    protected Integer residue;

    @Column(name = "PRODUCT_TYPE")
    protected Integer productType;


    public void setIsNotAvailable(Boolean isNotAvailable) {
        this.isNotAvailable = isNotAvailable;
    }

    public Boolean getIsNotAvailable() {
        return isNotAvailable;
    }


    public void setProductType(ProductTypes productType) {
        this.productType = productType == null ? null : productType.getId();
    }

    public ProductTypes getProductType() {
        return productType == null ? null : ProductTypes.fromId(productType);
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setResidue(Integer residue) {
        this.residue = residue;
    }

    public Integer getResidue() {
        return residue;
    }


}