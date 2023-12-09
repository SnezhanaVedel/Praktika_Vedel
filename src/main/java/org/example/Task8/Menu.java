package org.example.Task8;

abstract class NeatMenu {
    abstract void draw();
}

class TNeatMenu extends NeatMenu {
    public String[] getItems() {
        return items;
    }
    private String[] items;
    public TNeatMenu(String[] items) {
        this.items = items;
    }

    @Override
    void draw() {
        System.out.println("Отрисовка TNeatMenu");
        for (String item : items) {
            System.out.println(item);
        }
    }
}

class VerticalMenu extends TNeatMenu {
    private String[] items;

    public VerticalMenu(String[] items) {
        super(items);
        this.items = items;
    }

    @Override
    void draw() {
        System.out.println("Отрисовка VerticalMenu");
        for (String item : items) {
            System.out.println("| " + item);
        }
    }
}