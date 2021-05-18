package it.polito.ezshop.classes;

import java.time.LocalDate;

public class Debit extends BalanceOperationObj {
    public Debit(int id,LocalDate date, String type) {
        super(id, date, type);
    }
}
