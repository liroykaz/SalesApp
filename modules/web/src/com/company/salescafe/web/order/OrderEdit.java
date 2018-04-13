package com.company.salescafe.web.order;

import com.company.salescafe.entity.OrderCard;
import com.company.salescafe.entity.OrderStatus;
import com.company.salescafe.entity.ProductStatus;
import com.company.salescafe.services.OrderService;
import com.haulmont.cuba.gui.components.*;
import com.company.salescafe.entity.Order;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
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
    protected Table orderCardTable;

    @Inject
    protected CollectionDatasource<OrderCard, UUID> orderCardDs;
    @Inject
    protected OrderService orderService;
    @Inject
    private ComponentsFactory componentsFactory;

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

    @Override
    protected void initNewItem(Order item) {
        super.initNewItem(item);
        item.setNumberOfOrder(orderService.generateNewOrderNumber());
    }

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

    public Component generateActionsCell(OrderCard entity) {
        HBoxLayout hbox = componentsFactory.createComponent(HBoxLayout.class);
        hbox.setSpacing(true);

        final Button passButton = componentsFactory.createComponent(Button.class);
        passButton.setAction(new AbstractAction("orderPassed") {
            @Override
            public void actionPerform(Component component) {
                entity.setProductStatus(ProductStatus.isComplete);
                orderCardTable.repaint();
            }
        });
        passButton.setCaption("Выполнен");
        passButton.setStyleName("friendly");
        passButton.setVisible(ProductStatus.inWork.equals(entity.getProductStatus()));

        final Button acceptButton = componentsFactory.createComponent(Button.class);
        acceptButton.setAction(new AbstractAction("acceptOrder") {
            @Override
            public void actionPerform(Component component) {
                entity.setProductStatus(ProductStatus.inWork);
                orderCardTable.repaint();
            }
        });
        acceptButton.setCaption("Принять в работу");
        acceptButton.setStyleName("primary");
        acceptButton.setVisible(ProductStatus.isAccepted.equals(entity.getProductStatus()));

        final Button reopenButton = componentsFactory.createComponent(Button.class);
        reopenButton.setAction(new AbstractAction("reopenOrder") {
            @Override
            public void actionPerform(Component component) {
                entity.setProductStatus(ProductStatus.isAccepted);
                orderCardTable.repaint();
            }
        });
        reopenButton.setCaption("Выполнить заного");
        reopenButton.setStyleName("danger");
        reopenButton.setVisible(ProductStatus.isComplete.equals(entity.getProductStatus()));

        hbox.add(passButton);
        hbox.add(acceptButton);
        hbox.add(reopenButton);
		return hbox;
    }
}