package it.polito.ezshop.classes;

import java.time.LocalDate;

public class Debit extends BalanceOperationObj {
    public Debit(LocalDate date, String type) {
        super(date, type);
    }
}
