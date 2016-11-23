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
        "billName",
        "totalAmt",
        "dueDate",
        "groupId",
        "includedMembers"
})
public class NewBill {

    @JsonProperty("billName")
    private String billName;
    @JsonProperty("totalAmt")
    private String totalAmt;
    @JsonProperty("dueDate")
    private String dueDate;
    @JsonProperty("groupId")
    private String groupId;
    @JsonProperty("includedMembers")
    private List<String> includedMembers = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The groupId
     */
    @JsonProperty("groupId")
    public String getGroupId() {
        return groupId;
    }

    /**
     *
     * @param groupId
     * The groupId
     */
    @JsonProperty("groupId")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     *
     * @return
     * The includedMembers
     */
    @JsonProperty("includedMembers")
    public List<String> getIncludedMembers() {
        return includedMembers;
    }

    /**
     *
     * @param includedMembers
     * The includedMembers
     */
    @JsonProperty("includedMembers")
    public void setIncludedMembers(List<String> includedMembers) {
        this.includedMembers = includedMembers;
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