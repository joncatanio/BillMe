package com.joncatanio.billme.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "numPayers",
        "groupName",
        "totalAmt",
        "paid",
        "ownerUsername",
        "billName",
        "billId",
        "dueDate",
        "pending",
        "ownerName"
})
public class Bill {

    @JsonProperty("numPayers")
    private Integer numPayers;
    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("totalAmt")
    private String totalAmt;
    @JsonProperty("paid")
    private Integer paid;
    @JsonProperty("ownerUsername")
    private String ownerUsername;
    @JsonProperty("billName")
    private String billName;
    @JsonProperty("billId")
    private Integer billId;
    @JsonProperty("dueDate")
    private String dueDate;
    @JsonProperty("pending")
    private Integer pending;
    @JsonProperty("ownerName")
    private String ownerName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The numPayers
     */
    @JsonProperty("numPayers")
    public Integer getNumPayers() {
        return numPayers;
    }

    /**
     *
     * @param numPayers
     * The numPayers
     */
    @JsonProperty("numPayers")
    public void setNumPayers(Integer numPayers) {
        this.numPayers = numPayers;
    }

    /**
     *
     * @return
     * The groupName
     */
    @JsonProperty("groupName")
    public String getGroupName() {
        return groupName;
    }

    /**
     *
     * @param groupName
     * The groupName
     */
    @JsonProperty("groupName")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     *
     * @return
     * The totalAmt
     */
    @JsonProperty("totalAmt")
    public String getTotalAmt() {
        return totalAmt;
    }

    /**
     *
     * @param totalAmt
     * The totalAmt
     */
    @JsonProperty("totalAmt")
    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    /**
     *
     * @return
     * The paid
     */
    @JsonProperty("paid")
    public Integer getPaid() {
        return paid;
    }

    /**
     *
     * @param paid
     * The paid
     */
    @JsonProperty("paid")
    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    /**
     *
     * @return
     * The ownerUsername
     */
    @JsonProperty("ownerUsername")
    public String getOwnerUsername() {
        return ownerUsername;
    }

    /**
     *
     * @param ownerUsername
     * The ownerUsername
     */
    @JsonProperty("ownerUsername")
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    /**
     *
     * @return
     * The billName
     */
    @JsonProperty("billName")
    public String getBillName() {
        return billName;
    }

    /**
     *
     * @param billName
     * The billName
     */
    @JsonProperty("billName")
    public void setBillName(String billName) {
        this.billName = billName;
    }

    /**
     *
     * @return
     * The billId
     */
    @JsonProperty("billId")
    public Integer getBillId() {
        return billId;
    }

    /**
     *
     * @param billId
     * The billId
     */
    @JsonProperty("billId")
    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    /**
     *
     * @return
     * The dueDate
     */
    @JsonProperty("dueDate")
    public String getDueDate() {
        return dueDate;
    }

    /**
     *
     * @param dueDate
     * The dueDate
     */
    @JsonProperty("dueDate")
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     *
     * @return
     * The pending
     */
    @JsonProperty("pending")
    public Integer getPending() {
        return pending;
    }

    /**
     *
     * @param pending
     * The pending
     */
    @JsonProperty("pending")
    public void setPending(Integer pending) {
        this.pending = pending;
    }

    /**
     *
     * @return
     * The ownerName
     */
    @JsonProperty("ownerName")
    public String getOwnerName() {
        return ownerName;
    }

    /**
     *
     * @param ownerName
     * The ownerName
     */
    @JsonProperty("ownerName")
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}