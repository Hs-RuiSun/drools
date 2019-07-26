/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruby.drools.model;

/**
 *
 * @author dawna.floyd
 */
public class Request {
    String code;
    String subCode;
    Integer id;
    
    public Request(){}
    
    public Request(String code, String subCode, Integer id) {
    	super();
		this.code = code;
		this.subCode = subCode;
		this.id = id;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }
    
}
