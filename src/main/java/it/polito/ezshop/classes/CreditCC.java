package it.polito.ezshop.classes;

import java.time.LocalDate;

public class CreditCC extends BalanceOperationObj {
    public CreditCC(int id, LocalDate date, String type) {
        super(id, date, type);
    }
}
