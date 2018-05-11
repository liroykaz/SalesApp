package com.company.salescafe.web.ordercard;

import com.company.salescafe.entity.Product;
import com.company.salescafe.entity.ProductStatus;
import com.company.salescafe.entity.ProductTypes;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.company.salescafe.entity.OrderCard;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.*;

public class OrderCardEdit extends AbstractEditor<OrderCard> {

    @Inject
    protected CollectionDatasource<Product, UUID> productsDs;

    @Named("fieldGroup.productType")
    protected Field productType;

    @Named("orderCardFieldGroup.amount")
    protected Field amount;

    @Named("orderCardFieldGroup.productStatus")
    protected Field productStatus;

    @Named("fieldGroup.product")
    protected Field product;

    @Named("orderCardFieldGroup.price")
    protected Field price;

    @Inject
    protected Button plusButton;
    @Inject
    protected Button minusButton;

    @Override
    protected void postInit() {
        super.postInit();
        initCalculatePrice();
        initOrderProperties();
    }

    protected void filterProductListOnType(ProductTypes productTypes) {
        DataService dataService = AppBeans.get(DataService.NAME);
        LoadContext loadContext = new LoadContext(Product.class);

        loadContext.setQueryString("select p from salescafe$Product p where p.productType = :type").setParameter("type", productTypes);
        List<Product> products = dataService.loadList(loadContext);
        productsDs.clear();

        for (Product product : products) {
            productsDs.addItem(product);
        }
    }

    protected void initOrderProperties() {
        amount.setValue(1);
        productStatus.setValue(ProductStatus.isAccepted);
    }

    public void onCreateProductButtonClick() {
        openWindow("salescafe$Product.menuBrowse", WindowManager.OpenType.NEW_WINDOW)
                .addCloseListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        product.getDatasource().refresh();
                    }
                });
    }

    protected void initCalculatePrice() {
        productType.addValueChangeListener(e -> {
            product.setValue(null);
            filterProductListOnType((ProductTypes) e.getValue());
        });

        product.addValueChangeListener(e -> {
            if (e.getValue() != null)
                price.setValue(getItem().getProduct().getProductPrice() * getItem().getAmount());
            else
                price.setValue(null);
        });

        amount.addValueChangeListener(e -> {
            if (e.getValue() != null && getItem().getProduct() != null)
                price.setValue(getItem().getProduct().getProductPrice() * getItem().getAmount());
        });
    }

    public void onPlusButtonClick() {
        if (getItem().getAmount() != null)
            getItem().setAmount(getItem().getAmount() + 1);
    }

    public void onMinusButtonClick() {
        if (getItem().getAmount() != null && getItem().getAmount() > 0)
            getItem().setAmount(getItem().getAmount() - 1);
    }
}