package com.itms.model;

public class RepairRequest {
    private int id;
    private int productId;
    private int agentId;
    private String issueDescription;
    private String status;
    private String requestDate;
    private String priority;

    // Helper fields for display
    private String productName;
    private String agentName;

    public RepairRequest() {
    }

    public RepairRequest(int id, int productId, int agentId, String issueDescription, String status, String requestDate,
            String priority) {
        this.id = id;
        this.productId = productId;
        this.agentId = agentId;
        this.issueDescription = issueDescription;
        this.status = status;
        this.requestDate = requestDate;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
