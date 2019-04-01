/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.model;


public class OrderSelectedEvent {
    String order;

    public OrderSelectedEvent(String order){
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

}
