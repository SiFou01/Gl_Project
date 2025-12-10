package com.itms.model;

public class MaintenanceContract {
    private int id;
    private String supplierName;
    private String startDate;
    private String endDate;
    private String status;
    private String terms;
    private String product;

    public MaintenanceContract() {
    }

    public MaintenanceContract(int id, String supplierName, String startDate, String endDate, String status,
            String terms, String product) {
        this.id = id;
        this.supplierName = supplierName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.terms = terms;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
