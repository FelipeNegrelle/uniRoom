package com.felipe.uniroom.view;

public class RoomItem {
    private Integer id;
    private String display;

    public RoomItem(Integer id, String display) {
        this.id = id;
        this.display = display;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return display;  // Isso será usado para exibir no JComboBox
    }
}