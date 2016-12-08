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
        "billId",
        "userId"
})
public class AcceptPaymentRequest {

    @JsonProperty("billId")
    private Integer billId;
    @JsonProperty("userId")
    private Integer userId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The userId
     */
    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The userId
     */
    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
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