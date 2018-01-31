package com.company.salescafe.web.order;

import com.company.salescafe.entity.OrderCard;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Component;
import com.company.salescafe.entity.Order;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;

public class OrderBrowse extends AbstractLookup {

    @Inject
    private ComponentsFactory componentsFactory;

    public Component generateOrderCardProductListCell(Order entity) {
        StringBuilder str = new StringBuilder();
       // str.append("\n");
        for (OrderCard orderCard : entity.getOrderCard()) {
            str.append(orderCard.getProduct().getProductName()).append(" x").append(orderCard.getAmount()).append("\n");
        }
        Label label = componentsFactory.createComponent(Label.class);
        label.setValue(str.toString());
		return label;
    }
}