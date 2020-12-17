package br.com.alabastrum.escritoriovirtual.dto;

public class PagarMeDTO {

    private String status;
    private String current_status;

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getCurrent_status() {
	return current_status;
    }

    public void setCurrent_status(String current_status) {
	this.current_status = current_status;
    }

}