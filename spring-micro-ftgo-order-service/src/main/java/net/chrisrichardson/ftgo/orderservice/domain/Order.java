package net.chrisrichardson.ftgo.orderservice.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import net.chrisrichardson.ftgo.common.Money;
import net.chrisrichardson.ftgo.orderservice.api.events.OrderCreatedEvent;
import net.chrisrichardson.ftgo.orderservice.api.events.OrderDetails;
import net.chrisrichardson.ftgo.orderservice.api.events.OrderDomainEvent;
import net.chrisrichardson.ftgo.orderservice.api.events.OrderLineItem;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

//@Table 为实体Bean指定对应数据库表，目录和schema的名字
@Table(name = "orders")
//AccessType.FIELD 直接访问Entity的状态（属性），可不定义getter和setter方法，但是需要将属性声明为public
@Access(AccessType.FIELD)
public class Order {

    public static ResultWithDomainEvents<Order,OrderDomainEvent>
    createOrder(long consumerId, Restaurant restaurant, List<OrderLineItem> orderLineItems){
        Order order = new Order(consumerId,restaurant.getId(),orderLineItems);

        List<OrderDomainEvent> events = Collections.singletonList(new OrderCreatedEvent(
                new OrderDetails(consumerId, restaurant.getId(), orderLineItems, order.getOrderTotal()),
                restaurant.getName()));
        return new ResultWithDomainEvents<>(order, events);
    }

    //@Id 标注用于声明一个实体类的属性映射为数据库的主键列
    @Id
    //@GeneratedValue 用于标注主键的生成策略，通过strategy 属性指定
    @GeneratedValue
    private Long id;

    //@Version是jpa里提供的一个注解，其作用是用于实现乐观锁
    @Version
    private Long version;

    private Long consumerId;
    private Long restaurantId;

    @Embedded
    private OrderLineItems orderLineItems;

    @Embedded
    private Money orderMinimum = new Money(Integer.MAX_VALUE);

    private Order() {
    }

    public Order(long consumerId,long restaurantId,List<OrderLineItem> orderLineItems){
        this.consumerId = consumerId;
        this.restaurantId = restaurantId;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public Money getOrderTotal() {
        return orderLineItems.orderTotal();
    }

}
