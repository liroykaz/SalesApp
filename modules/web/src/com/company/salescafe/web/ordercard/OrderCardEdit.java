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
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.salescafe.entity.OrderCard;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.LookupField;
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

    @Named("fieldGroup.amount")
    protected Field amount;

    @Named("fieldGroup.productStatus")
    protected Field productStatus;

    @Named("fieldGroup.product")
    protected Field product;

    @Named("fieldGroup.price")
    protected Field price;

    @Override
    protected void postInit() {
        super.postInit();

        productType.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChanged(ValueChangeEvent e) {
                product.setValue(null);
                filterProductListOnType((ProductTypes) e.getValue());
            }
        });

        product.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChanged(ValueChangeEvent e) {
                if (e.getValue() != null)
                    price.setValue(getItem().getProduct().getProductPrice());
                else
                    price.setValue(null);
            }
        });

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

    protected void initOrderProperties(){
        amount.setValue(1);
        productStatus.setValue(ProductStatus.isAccepted);
    }

    public void onCreateProductButtonClick() {
        openWindow("salescafe$Product.browse", WindowManager.OpenType.NEW_WINDOW)
        .addCloseListener(new CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                product.getDatasource().refresh();
            }
        });
    }
}