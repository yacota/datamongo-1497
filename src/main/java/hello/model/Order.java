package hello.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 
 * @author jllach
 */
@Document(collection = "ORDER")
public class Order {
    
    @Id
    Long id;
    @Field
    String name;
    
    public Order(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}