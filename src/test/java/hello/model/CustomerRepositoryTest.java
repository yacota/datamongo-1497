package hello.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import hello.Application;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class CustomerRepositoryTest {
    
    
    private static final String COL1 = "CUSTOMER";
    private static final String COL2 = "ORDER";
    
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    private static MongodExecutable mongodExecutable;
    private static MongodProcess mongoD;
    private static MongoClient mongo;
    
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MongoClient client;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        int port = 12345;
        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                                                              .net(new Net(port, Network.localhostIsIPv6())).build();
        mongodExecutable = starter.prepare(mongodConfig);
        mongoD = mongodExecutable.start();
        mongo = new MongoClient("localhost", port);
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        mongoD.stop();
        mongodExecutable.stop();
    }
    
    @Before
    public void before() {
        DB db = client.getDB("test");
        DBCollection col1 = db.getCollection(COL1);
        BasicDBObject order = new BasicDBObject("_id", 1L).append("name", "orderName");
        HashMap<String, Object> fields = new HashMap<>(2);
        fields.put("firstName",  "jordi");
        fields.put("lastName",   "llach");
        fields.put("firstOrders", Arrays.asList(order));
        fields.put("lastOrders",  Arrays.asList(order));
        col1.save(new BasicDBObject("DATA", fields).append("_id", 1L));
        DBCollection col2 = db.createCollection(COL2, new BasicDBObject());
        col2.save(new BasicDBObject("testDoc", new Date()));
    }
    
    @After
    public void after() {
        client.getDB("test").getCollection(COL1).drop();
        client.getDB("test").getCollection(COL2).drop();
    }
    
    //
    // Test
    // 
    
    @Test
    public void testFindOne() {
        Customer findOne = customerRepository.findOne(1L);
        Assert.assertNotNull(findOne.firstName);
        Assert.assertNotNull(findOne.firstOrders);
        // below ones will fail because they are setted by refelrection and MappingMongoConverter does not use a DBObjectAccessor
        Assert.assertNotNull(findOne.lastName);
        Assert.assertNotNull(findOne.lastOrders);
    }

}