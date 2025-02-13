package com.example.java_kafka_redis_spike;

import java.time.LocalDateTime;
import java.util.List;

public class WorkflowException {

    private Integer exceptionId;
    private Integer itemNumber;
    private LocalDateTime needByDate;
    private List<String> tags;
    private OrderPriority orderPriority;
    private Short daysInQueue;

    // Getter Method - Returns Integer
    public Integer getExceptionId() {
        return exceptionId;
    }

    // Setter Method - Accepts Integer
    public void setExceptionId(Integer exceptionId) {
        this.exceptionId = exceptionId;
    }

    // Getter Method - Returns Integer
    public Integer getItemNumber() {
        return itemNumber;
    }

    // Setter Method - Accepts Integer
    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    // Getter Method - Returns Integer
    public Short getDaysInQueue() {
        return daysInQueue;
    }

    // Setter Method - Accepts Integer
    public void setDaysInQueue(Short daysInQueue) {
        if (daysInQueue != null && daysInQueue < 0) {
            throw new IllegalArgumentException("Days in queue cannot be negative.");
        }
        this.daysInQueue = daysInQueue;
    }

    // Getter Method (Returns the priority as a String)
    public String getOrderPriority() {
        return (orderPriority != null) ? orderPriority.name() : null;
    }

    // Setter Method (Accepts a String and Converts to Enum)
    public void setOrderPriority(String priority) {
        if (priority != null) {
            try {
                this.orderPriority = OrderPriority.valueOf(priority.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order priority: " + priority);
            }
        } else {
            this.orderPriority = null;
        }
    }

    // Getter Method - Returns LocalDateTime
    public LocalDateTime getNeedByDate() {
        return needByDate;
    }

    // Setter Method - Allows Setting LocalDateTime
    public void setNeedByDate(LocalDateTime needByDate) {
        this.needByDate = needByDate;
    }

    // Getter Method - Returns List<String>
    public List<String> getTags() {
        return tags;
    }

    // Setter Method - Allows Setting List<String>
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // Enum for Valid Order Priorities
    public enum OrderPriority {
        LOW, MEDIUM, HIGH, URGENT;
    }

}
