package com.company.salescafe.web.order;

import com.company.salescafe.entity.Order;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.data.GroupDatasource;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CalculateOrderBrowse extends AbstractLookup {
    @Inject
    protected GroupTable ordersTable;
    @Inject
    protected GroupDatasource<Order, UUID> ordersDs;

    @Inject
    protected Button applyButton;
    @Inject
    protected DateField dateField;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }

    public void onApplyButtonClick() {
        ordersDs.refresh();
        List<Order> orderList = new ArrayList<>();
        if (dateField.getValue() != null) {
            ordersDs.getItems().forEach(e -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                String orderDate = dateFormat.format(e.getTimeOfOrder());
                if (!orderDate.equals(dateFormat.format(dateField.getValue())))
                    orderList.add(e);
            });

            orderList.forEach(e -> {
                ordersDs.excludeItem(e);
            });
            ordersTable.setVisible(true);
        }
    }
}