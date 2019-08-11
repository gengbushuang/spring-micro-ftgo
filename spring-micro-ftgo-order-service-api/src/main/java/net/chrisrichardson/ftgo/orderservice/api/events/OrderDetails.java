package net.chrisrichardson.ftgo.orderservice.api.events;

import net.chrisrichardson.ftgo.common.Money;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class OrderDetails {

    private List<OrderLineItem> lineItems;
    private Money orderTotal;

    private long restaurantId;
    private long consumerId;

    private OrderDetails(){}

    public OrderDetails(long consumerId, long restaurantId, List<OrderLineItem> lineItems, Money orderTotal){
        this.consumerId = consumerId;
        this.restaurantId = restaurantId;
        this.lineItems = lineItems;
        this.orderTotal = orderTotal;
    }

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public Money getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Money orderTotal) {
        this.orderTotal = orderTotal;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public long getConsumerId() {
        return consumerId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
