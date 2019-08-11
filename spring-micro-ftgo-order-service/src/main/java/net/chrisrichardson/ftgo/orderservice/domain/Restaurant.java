package net.chrisrichardson.ftgo.orderservice.domain;

import net.chrisrichardson.ftgo.restaurantservice.events.MenuItem;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "order_service_restaurants")
@Access(AccessType.FIELD)
public class Restaurant {

    @Id
    private Long id;

    @Embedded
    @ElementCollection
    @CollectionTable(name = "order_service_restaurant_menu_items")
    private List<MenuItem> menuItems;

    private String name;

    private Restaurant(){}

    public Restaurant(long id,String name,List<MenuItem> menuItems) {
        this.id = id;
        this.name = name;
        this.menuItems = menuItems;
    }

    public Long getId() {
        return id;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public String getName() {
        return name;
    }

    public Optional<MenuItem> findMenuItem(String menuItemId){
        return menuItems.stream().filter(mi->mi.getId().equals(menuItemId)).findFirst();
    }
}
