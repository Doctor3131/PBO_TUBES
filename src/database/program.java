
package database;

import java.util.ArrayList;
import java.util.List;

public class program {
    static service service = new service();
    
    public static void main(String[] args) {
        List<account> listcustomer = new ArrayList<>();
        
        System.out.println("");
        
        // insert
        System.out.println("===Insert");
        account addCustomer = new account("ninaaja@gmail.com", "apaya", 800);
        service.add(addCustomer);
        System.out.println("berhasil insert" + addCustomer); 
    }
}
