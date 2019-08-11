package net.chrisrichardson.ftgo.orderservice.sagas.createorder;

import net.chrisrichardson.ftgo.orderservice.api.events.OrderDetails;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateOrderSagaState {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Long orderId;

    private OrderDetails orderDetails;
    private long ticketId;

    public Long getOrderId() {
        return orderId;
    }

    private CreateOrderSagaState(){}

    public CreateOrderSagaState(Long orderId,OrderDetails orderDetails){
        this.orderId = orderId;
        this.orderDetails = orderDetails;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public long getTicketId() {
        return ticketId;
    }

}
