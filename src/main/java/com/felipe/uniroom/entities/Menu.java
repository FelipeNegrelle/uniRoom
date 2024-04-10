package com.felipe.uniroom.entities;

import javax.swing.*;

public class Menu {
    private String name;
    private Icon icon;
    private Class<?> destination;

    public Menu(String name, Icon icon, Class<?> destination) {
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

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }
}
