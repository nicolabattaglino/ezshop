package it.polito.ezshop.classes;

import java.time.LocalDate;

public class Credit extends BalanceOperationObj {
    public Credit(int id,LocalDate date, String type) {
        super(id, date, type);
    }
}
