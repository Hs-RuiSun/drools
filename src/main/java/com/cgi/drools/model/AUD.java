package com.cgi.drools.model;

public class AUD {
    private String reqPortfolioType;
    private String reqSpecialCommentCode;
    private Boolean isValid;
    private Integer errorCode;

    public AUD(String reqPortfolioType, String reqSpecialCommentCode) {
        this.reqPortfolioType = reqPortfolioType;
        this.reqSpecialCommentCode = reqSpecialCommentCode;
        this.isValid = true;
    }

    public String getReqPortfolioType() {
        return reqPortfolioType;
    }

    public String getReqSpecialCommentCode() {
        return reqSpecialCommentCode;
    }

    public Boolean isValid() {
        return isValid;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setReqPortfolioType(String reqPortfolioType) {
        this.reqPortfolioType = reqPortfolioType;
    }

    public void setReqSpecialCommentCode(String reqSpecialCommentCode) {
        this.reqSpecialCommentCode = reqSpecialCommentCode;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
