package com.felipe.uniroom.entities;

import javax.swing.*;

public class Menu {
    private String name;
    private Icon icon;
    private Object destination;

    public Menu(String name, Icon icon, Object destination) {
        this.name = name;
        this.icon = icon;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Object getDestination() {
        return destination;
    }

    public void setDestination(Object destination) {
        this.destination = destination;
    }
}
