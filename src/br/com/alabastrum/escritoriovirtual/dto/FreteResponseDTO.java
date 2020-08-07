package br.com.alabastrum.escritoriovirtual.dto;

public class FreteResponseDTO {

    private String error;
    private Integer id;
    private String price;
    private Integer deliveryTime;
    private CompanyDTO company;

    public String getPrice() {
	return price;
    }

    public void setPrice(String price) {
	this.price = price;
    }

    public Integer getDeliveryTime() {
	return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
	this.deliveryTime = deliveryTime;
    }

    public CompanyDTO getCompany() {
	return company;
    }

    public void setCompany(CompanyDTO company) {
	this.company = company;
    }

    public String getError() {
	return error;
    }

    public void setError(String error) {
	this.error = error;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }
}