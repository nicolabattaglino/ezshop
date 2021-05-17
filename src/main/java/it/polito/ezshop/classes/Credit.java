package it.polito.ezshop.classes;

import java.time.LocalDate;

public class Credit extends BalanceOperationObj {
    public Credit(LocalDate date, String type) {
        super(date, type);
    }
}
