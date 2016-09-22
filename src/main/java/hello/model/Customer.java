package hello.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 
 * @author jllach
 */
@Document(collection = "CUSTOMER")
public class Customer {

    @Id
    Long id;    
    @Field("DATA.firstName")
    String firstName;
    @Field("DATA.firstOrders")
    List<Order> firstOrders;
    
    // bellow ones are setted by "reflection" in MappingMongoCoverter
    @Field("DATA.lastName")
    String lastName;
    @Field("DATA.lastOrders")
    List<Order> lastOrders;

    public Customer(Long id, String firstName, List<Order> firstOrders) {
        this.id = id;
        this.firstName = firstName;
        this.firstOrders = firstOrders;
    }

}