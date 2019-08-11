package net.chrisrichardson.ftgo.orderservice.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.micrometer.core.instrument.MeterRegistry;
import net.chrisrichardson.ftgo.orderservice.api.events.OrderDetails;
import net.chrisrichardson.ftgo.orderservice.api.events.OrderDomainEvent;
import net.chrisrichardson.ftgo.orderservice.api.events.OrderLineItem;
import net.chrisrichardson.ftgo.orderservice.sagas.createorder.CreateOrderSagaState;
import net.chrisrichardson.ftgo.orderservice.web.MenuItemIdAndQuantity;
import net.chrisrichardson.ftgo.restaurantservice.events.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private OrderRepository orderRepository;

    private RestaurantRepository restaurantRepository;

    private SagaManager<CreateOrderSagaState> createOrderSagaManager;

    private Optional<MeterRegistry> meterRegistry;

    private OrderDomainEventPublisher orderAggregateEventPublisher;

    public Order createOrder(long consumerId, long restaurantId, List<MenuItemIdAndQuantity> lineItems) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        List<OrderLineItem> orderLineItems = makeOrderLineItems(lineItems, restaurant);

        ResultWithDomainEvents<Order, OrderDomainEvent> orderAndEvents =
                Order.createOrder(consumerId, restaurant, orderLineItems);

        Order order = orderAndEvents.result;
        orderRepository.save(order);

        orderAggregateEventPublisher.publish(order, orderAndEvents.events);

        OrderDetails orderDetails = new OrderDetails(consumerId, restaurantId, orderLineItems, order.getOrderTotal());

        CreateOrderSagaState data = new CreateOrderSagaState(order.getId(), orderDetails);

        createOrderSagaManager.create(data, Order.class, order.getId());

        meterRegistry.ifPresent(mr -> mr.counter("placed_orders").increment());
        return order;
    }

    private List<OrderLineItem> makeOrderLineItems(List<MenuItemIdAndQuantity> lineItems, Restaurant restaurant) {
        return lineItems.stream().map(li -> {
            MenuItem om = restaurant.findMenuItem(li.getMenuItemId())
                    .orElseThrow(() -> new InvalidMenuItemIdException(li.getMenuItemId()));
            return new OrderLineItem(li.getMenuItemId(), om.getName(), om.getPrice(), li.getQuantity());
        }).collect(Collectors.toList());
    }

    public Order cancel(long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return order;
    }
}
