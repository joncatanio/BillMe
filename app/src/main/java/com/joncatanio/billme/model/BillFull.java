package com.joncatanio.billme.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "complete",
        "groupName",
        "totalAmt",
        "ownerUsername",
        "billName",
        "ownerName",
        "payers",
        "dueDate"
})
public class BillFull {

    @JsonProperty("complete")
    private Integer complete;
    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("totalAmt")
    private String totalAmt;
    @JsonProperty("ownerUsername")
    private String ownerUsername;
    @JsonProperty("billName")
    private String billName;
    @JsonProperty("ownerName")
    private String ownerName;
    @JsonProperty("payers")
    private List<Payer> payers = new ArrayList<Payer>();
    @JsonProperty("dueDate")
    private String dueDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The complete
     */
    @JsonProperty("complete")
    public Integer getComplete() {
        return complete;
    }

    /**
     *
     * @param complete
     * The complete
     */
    @JsonProperty("complete")
    public void setComplete(Integer complete) {
        this.complete = complete;
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

    /**
     *
     * @return
     * The payers
     */
    @JsonProperty("payers")
    public List<Payer> getPayers() {
        return payers;
    }

    /**
     *
     * @param payers
     * The payers
     */
    @JsonProperty("payers")
    public void setPayers(List<Payer> payers) {
        this.payers = payers;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}