package com.anjanatec.pos.db;

import com.anjanatec.pos.model.Customer;
import com.anjanatec.pos.model.Item;
import com.anjanatec.pos.model.Order;

import java.util.ArrayList;

public class Database {
    public static ArrayList<Customer> customersTable = new ArrayList<Customer>();

    public static ArrayList <Item> itemTable = new ArrayList<Item>();

    public static ArrayList<Order> orderTable = new ArrayList<>();

    static {
        customersTable.add(new Customer("C001","Anjana","Tissa",2500));
        customersTable.add(new Customer("C002","Dilhani","Magama",3500));
        customersTable.add(new Customer("C003","Isuru","Pandura",250500));
        customersTable.add(new Customer("C004","Rashmi","Kuliyapitiya",2500));
        customersTable.add(new Customer("C005","Hasanka","Akurugoda",2500));

        itemTable.add(new Item("I-001","Description-1",25,20));
        itemTable.add(new Item("I-002","Description-2",35,10));
        itemTable.add(new Item("I-003","Description-3",45,30));
        itemTable.add(new Item("I-004","Description-4",55,15));
        itemTable.add(new Item("I-005","Description-5",65,25));
    }

}
