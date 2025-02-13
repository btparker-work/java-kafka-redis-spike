package com.example.java_kafka_redis_spike;

import redis.clients.jedis.Jedis;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ExceptionHandler {
    private Jedis jedis;

    public ExceptionHandler(Jedis jedis) {
        this.jedis = jedis;
    }

    public void addException(WorkflowException WorkflowException) {
        // Store WorkflowException details in a hash
        String hashKey = "WorkflowException:" + WorkflowException.getExceptionId().toString();
        jedis.hset(hashKey, "ItemNumber", WorkflowException.getItemNumber().toString());
        jedis.hset(hashKey, "DaysInQueue", String.valueOf(WorkflowException.getDaysInQueue()));
        jedis.hset(hashKey, "OrderPriority", WorkflowException.getOrderPriority());
        jedis.hset(hashKey, "NeedByDate", WorkflowException.getNeedByDate().toString());
        jedis.hset(hashKey, "tags", String.join(",", WorkflowException.getTags()));

        // Calculate score
        double score = calculateScore(WorkflowException);

        // Add to ZSET
        jedis.zadd("FINANCE_QUEUE", score, hashKey);
    }

    private double calculateScore(WorkflowException WorkflowException) {
        double priorityScore = getPriorityScore(WorkflowException.getOrderPriority());
        double daysInQueueScore = WorkflowException.getDaysInQueue() * 10;
        double needByDateScore = calculateNeedByDateScore(WorkflowException.getNeedByDate());

        return priorityScore + daysInQueueScore + needByDateScore;
    }

    private double getPriorityScore(String priority) {
        switch (priority.toLowerCase()) {
            case "high": return 100;
            case "medium": return 50;
            case "low": return 10;
            default: return 0;
        }
    }

    private double calculateNeedByDateScore(LocalDateTime needByDateTime) {
        LocalDate now = LocalDate.now();
        LocalDate needBy = needByDateTime.toLocalDate();
        long daysUntilNeedBy = ChronoUnit.DAYS.between(now, needBy);
        return Math.max(0, 100 - daysUntilNeedBy);
    }
}

