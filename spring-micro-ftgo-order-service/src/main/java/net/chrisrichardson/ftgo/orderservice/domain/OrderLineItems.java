package net.chrisrichardson.ftgo.orderservice.domain;

import net.chrisrichardson.ftgo.common.Money;
import net.chrisrichardson.ftgo.orderservice.api.events.OrderLineItem;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @ElementCollection
    @CollectionTable(name = "order_line_items")
    private List<OrderLineItem> lineItems;

    private OrderLineItems() {
    }
    public OrderLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    OrderLineItem findOrderLineItem(String lineItemId) {
        return lineItems.stream().filter(li -> li.getMenuItemId().equals(lineItemId)).findFirst().get();
    }

    Money orderTotal(){
        return lineItems.stream().map(li->li.getTotal()).reduce(Money.ZERO,Money::add);
    }
}
