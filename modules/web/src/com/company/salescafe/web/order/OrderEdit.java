package com.company.salescafe.web.order;

import com.company.salescafe.entity.OrderCard;
import com.company.salescafe.entity.OrderStatus;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.salescafe.entity.Order;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class OrderEdit extends AbstractEditor<Order> {

    @Named("fieldGroup.timeOfOrder")
    protected Field timeOfOrder;

    @Named("fieldGroup.orderStatus")
    protected Field orderStatus;

    @Named("fieldGroup.allCost")
    protected Field allCost;

    @Inject
    protected CollectionDatasource<OrderCard, UUID> orderCardDs;

    @Override
    protected void postInit() {
        super.postInit();
        initOrderProperties();

        orderCardDs.addCollectionChangeListener(new CollectionDatasource.CollectionChangeListener<OrderCard, UUID>() {
            @Override
            public void collectionChanged(CollectionDatasource.CollectionChangeEvent<OrderCard, UUID> e) {
                generateTotalCost(e.getItems());
            }
        });
    }

    protected LinkedList<String> list = new LinkedList();


    protected void generateTotalCost(List<OrderCard> cardList) {
        int totalCost = 0;
        cardList = getItem().getOrderCard();

        for (OrderCard orderCard : cardList) {
            totalCost += orderCard.getPrice() * orderCard.getAmount();
        }

        getItem().setAllCost(totalCost);
    }

    protected void initOrderProperties() {
        timeOfOrder.setValue(new Date());
        allCost.setValue(0);
        orderStatus.setValue(OrderStatus.isaccepted);
    }
}