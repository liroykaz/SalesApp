package com.company.salescafe.service;

import com.company.salescafe.entity.Order;
import com.company.salescafe.services.OrderService;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.TimeSource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.TemporalType;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

@Service(OrderService.NAME)
public class OrderServiceBean implements OrderService {

    @Inject
    private Persistence persistence;
    @Inject
    private TimeSource timeSource;

    @Override
    public int generateNewOrderNumber() {
        final Transaction tx = persistence.getTransaction();
        try {
            final EntityManager em = persistence.getEntityManager();
            TypedQuery<Order> typedQuery = em.createQuery("select t from salescafe$Order t", Order.class);

            List<Order> orderList = typedQuery.getResultList();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Order order = orderList.stream().filter(e->{
                String today = dateFormat.format(timeSource.currentTimestamp());
                return today.equals(dateFormat.format(e.getTimeOfOrder()));
            }).max(Comparator.comparing(Order::getNumberOfOrder)).orElse(null);

            return order == null ? 1 : order.getNumberOfOrder() + 1;
        } finally {
            tx.end();
        }
    }
}
