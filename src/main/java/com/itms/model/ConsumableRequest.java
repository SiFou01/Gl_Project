package com.itms.model;

public class ConsumableRequest {
    private int id;
    private int consumableId;
    private int agentId;
    private int quantity;
    private String status;
    private String requestDate;
    private String message;

    // Extra fields for display
    private String consumableName;
    private String agentName;

    public ConsumableRequest() {
    }

    public ConsumableRequest(int id, int consumableId, int agentId, int quantity, String status, String requestDate,
            String message) {
        this.id = id;
        this.consumableId = consumableId;
        this.agentId = agentId;
        this.quantity = quantity;
        this.status = status;
        this.requestDate = requestDate;
        this.message = message;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConsumableId() {
        return consumableId;
    }

    public void setConsumableId(int consumableId) {
        this.consumableId = consumableId;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConsumableName() {
        return consumableName;
    }

    public void setConsumableName(String consumableName) {
        this.consumableName = consumableName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
