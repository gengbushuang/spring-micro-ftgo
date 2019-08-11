package net.chrisrichardson.ftgo.orderservice.web;

import net.chrisrichardson.ftgo.orderservice.api.web.CreateOrderRequest;
import net.chrisrichardson.ftgo.orderservice.api.web.CreateOrderResponse;
import net.chrisrichardson.ftgo.orderservice.domain.Order;
import net.chrisrichardson.ftgo.orderservice.domain.OrderRepository;
import net.chrisrichardson.ftgo.orderservice.domain.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private OrderService orderService;

    private OrderRepository orderRepository;

    @RequestMapping(method = RequestMethod.POST)
    public CreateOrderResponse create(@RequestBody CreateOrderRequest request){
        Order order = orderService.createOrder(request.getConsumerId(),request.getRestaurantId()
        ,request.getLineItems().stream().map(x->new MenuItemIdAndQuantity(x.getMenuItemId(),x.getQuantity()))
                        .collect(Collectors.toList()));

        return new CreateOrderResponse(order.getId());
    }

    @RequestMapping(path = "/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(o -> new ResponseEntity<>(makeGetOrderResponse(o), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private GetOrderResponse makeGetOrderResponse(Order order) {
        return new GetOrderResponse(order.getId(), order.getState().name(), order.getOrderTotal());
    }

//    public ResponseEntity<GetOrderResponse> cancel(@PathVariable long orderId) {
//
//        Order order = orderService.cancel(orderId);
//    }

}
