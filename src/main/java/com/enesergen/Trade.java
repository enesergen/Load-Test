package com.enesergen;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade {
    @JsonProperty
    private String transactionId;
    @JsonProperty
    private String actionType;
    @JsonProperty
    private String mkkMemberCode;
    @JsonProperty
    private String transactionDate;
    @JsonProperty
    private String transactionTime;
    @JsonProperty
    private String buyerRegNo;
    @JsonProperty
    private String sellerRegNo;
    @JsonProperty
    private String baseCurrency;
    @JsonProperty
    private String quoteCurrency;
    @JsonProperty
    private String baseAmount;
    @JsonProperty
    private String quoteAmount;
    @JsonProperty
    private String buyerFee;
    @JsonProperty
    private String sellerFee;
    @JsonProperty
    private String buyerCurrency;
    @JsonProperty
    private String sellerCurrency;
    @JsonProperty
    private String buyerCommissionTransferredRegNo;
    @JsonProperty
    private String sellerCommissionTransferredRegNo;
    @JsonProperty
    private String correctionDate;
    @JsonProperty
    private String correctionRefId;
    @JsonProperty
    private String correctionDesc;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setMkkMemberCode(String mkkMemberCode) {
        this.mkkMemberCode = mkkMemberCode;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public void setBuyerRegNo(String buyerRegNo) {
        this.buyerRegNo = buyerRegNo;
    }

    public void setSellerRegNo(String sellerRegNo) {
        this.sellerRegNo = sellerRegNo;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public void setQuoteAmount(String quoteAmount) {
        this.quoteAmount = quoteAmount;
    }

    public void setBuyerFee(String buyerFee) {
        this.buyerFee = buyerFee;
    }

    public void setSellerFee(String sellerFee) {
        this.sellerFee = sellerFee;
    }

    public void setBuyerCurrency(String buyerCurrency) {
        this.buyerCurrency = buyerCurrency;
    }

    public void setSellerCurrency(String sellerCurrency) {
        this.sellerCurrency = sellerCurrency;
    }

    public void setBuyerCommissionTransferredRegNo(String buyerCommissionTransferredRegNo) {
        this.buyerCommissionTransferredRegNo = buyerCommissionTransferredRegNo;
    }

    public void setSellerCommissionTransferredRegNo(String sellerCommissionTransferredRegNo) {
        this.sellerCommissionTransferredRegNo = sellerCommissionTransferredRegNo;
    }

    public void setCorrectionDate(String correctionDate) {
        this.correctionDate = correctionDate;
    }

    public void setCorrectionRefId(String correctionRefId) {
        this.correctionRefId = correctionRefId;
    }

    public void setCorrectionDesc(String correctionDesc) {
        this.correctionDesc = correctionDesc;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getMkkMemberCode() {
        return mkkMemberCode;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getBuyerRegNo() {
        return buyerRegNo;
    }

    public String getSellerRegNo() {
        return sellerRegNo;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public String getQuoteAmount() {
        return quoteAmount;
    }

    public String getBuyerFee() {
        return buyerFee;
    }

    public String getSellerFee() {
        return sellerFee;
    }

    public String getBuyerCurrency() {
        return buyerCurrency;
    }

    public String getSellerCurrency() {
        return sellerCurrency;
    }

    public String getBuyerCommissionTransferredRegNo() {
        return buyerCommissionTransferredRegNo;
    }

    public String getSellerCommissionTransferredRegNo() {
        return sellerCommissionTransferredRegNo;
    }

    public String getCorrectionDate() {
        return correctionDate;
    }

    public String getCorrectionRefId() {
        return correctionRefId;
    }

    public String getCorrectionDesc() {
        return correctionDesc;
    }
}
