package it.polito.ezshop;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.polito.ezshop.classes.CustomerManager;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {

    public static void main(String[] args) throws Exception{
//    	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
//        EZShopGUI gui = new EZShopGUI(ezShop);
//
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(new it.polito.ezshop.classes.CustomerObj(5, "hu"));
        System.out.println(json);
//        cm.defineCustomer("Mattia");
//        cm.defineCustomer("Nicola");


    }

}
