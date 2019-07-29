package com.ruby.drools.model;

public class ACDV {
    private String reqPortfolioType;
    private String reqSpecialCommentCode;
    private String respPortfolioType;
    private String respSpecialCommentCode;
    private Boolean isValid;
    private Integer errorCode;

    public ACDV(String reqPortfolioType, String reqSpecialCommentCode, String respPortfolioType, String respSpecialCommentCode) {
        this.reqPortfolioType = reqPortfolioType;
        this.reqSpecialCommentCode = reqSpecialCommentCode;
        this.respPortfolioType = respPortfolioType;
        this.respSpecialCommentCode = respSpecialCommentCode;
        this.isValid = true;
    }

    public String getReqPortfolioType() {
        return reqPortfolioType;
    }

    public String getReqSpecialCommentCode() {
        return reqSpecialCommentCode;
    }

    public String getRespPortfolioType() { return respPortfolioType; }

    public String getRespSpecialCommentCode() { return respSpecialCommentCode; }

    public Boolean isValid() {
        return isValid;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setReqPortfolioType(String reqPortfolioType) {
        this.reqPortfolioType = reqPortfolioType;
    }

    public void setReqSpecialCommentCode(String reqSpecialCommentCode) { this.reqSpecialCommentCode = reqSpecialCommentCode; }

    public void setRespPortfolioType(String respPortfolioType) { this.respPortfolioType = respPortfolioType; }

    public void setRespSpecialCommentCode(String respSpecialCommentCode) { this.respSpecialCommentCode = respSpecialCommentCode; }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ACDV{" +
                "reqPortfolioType='" + reqPortfolioType + '\'' +
                ", respPortfolioType='" + respPortfolioType + '\'' +
                ", reqSpecialCommentCode='" + reqSpecialCommentCode + '\'' +
                ", respSpecialCommentCode='" + respSpecialCommentCode + '\'' +
                ", isValid=" + isValid +
                ", errorCode=" + errorCode +
                '}';
    }
}
