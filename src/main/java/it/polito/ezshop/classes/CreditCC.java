package it.polito.ezshop.classes;

import java.time.LocalDate;
//todo add to design are it needed?
public class CreditCC extends BalanceOperationObj {
    public CreditCC(int id, LocalDate date, String type) {
        super(id, date, type);
    }
}
