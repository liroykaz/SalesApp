package com.company.salescafe.web.order;

import com.company.salescafe.entity.OrderCard;
import com.company.salescafe.entity.ProductStatus;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Component;
import com.company.salescafe.entity.Order;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;

public class OrderBrowse extends AbstractLookup {

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    protected Messages messages;

    public Component generateOrderCardProductListCell(Order entity) {
        StringBuilder str = new StringBuilder();
        for (OrderCard orderCard : entity.getOrderCard()) {
            if (orderCard.getProduct() != null)
                str.append("[").append(messages.getMessage(ProductStatus.class,"ProductStatus." + orderCard.getProductStatus().name())).append("] ")
                        .append(orderCard.getProduct().getProductName())
                        .append(" x")
                        .append(orderCard.getAmount())
                        .append("\n");
        }
        Label proudctInfoLabel = componentsFactory.createComponent(Label.class);
        proudctInfoLabel.setValue(str.toString());
        return proudctInfoLabel;
    }
}