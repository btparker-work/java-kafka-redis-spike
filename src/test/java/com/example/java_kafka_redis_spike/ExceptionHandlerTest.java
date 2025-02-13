package com.example.java_kafka_redis_spike;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;

public class ExceptionHandlerTest {
    private Jedis jedis;
    private WorkflowExceptionHandler handler;

    @Before
    public void setUp() {
        jedis = new Jedis("localhost");
        jedis.flushAll(); // Clear Redis database
        handler = new WorkflowExceptionHandler(jedis);
    }

    @Test
    public void testAddAndRetrieveExceptions() {
        // Create test exceptions
        WorkflowException exception1 = new WorkflowException("123123123", "123123123", 0.25, "Low", "2025/02/20", 
                                             Arrays.asList("epostrx", "Finance_Queue", "Default_Filter"));
        WorkflowException exception2 = new WorkflowException("123123124", "123123124", 1.5, "High", "2025/02/15", 
                                             Arrays.asList("epostrx", "Finance_Queue"));

        // Add exceptions to Redis
        handler.addException(exception1);
        handler.addException(exception2);

        // Retrieve and verify exceptions
        Set<Tuple> results = jedis.zrangeWithScores("FINANCE_QUEUE", 0, -1);
        assertEquals(2, results.size());

        for (Tuple tuple : results) {
            String hashKey = tuple.getElement();
            double score = tuple.getScore();

            // Verify hash contents
            assertEquals(exception1.getItemNumber(), jedis.hget(hashKey, "ItemNumber"));
            assertTrue(jedis.hget(hashKey, "tags").contains("Finance_Queue"));

            // Verify scoring
            assertTrue(score > 0);
        }
    }
}

