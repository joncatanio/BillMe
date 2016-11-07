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
        "groupName",
        "groupId",
        "groupImg",
        "amtOwedAsGroup"
})
public class GroupShort {

    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("groupId")
    private Integer groupId;
    @JsonProperty("groupImg")
    private String groupImg;
    @JsonProperty("amtOwedAsGroup")
    private String amtOwedAsGroup;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The groupId
     */
    @JsonProperty("groupId")
    public Integer getGroupId() {
        return groupId;
    }

    /**
     *
     * @param groupId
     * The groupId
     */
    @JsonProperty("groupId")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     *
     * @return
     * The groupImg
     */
    @JsonProperty("groupImg")
    public String getGroupImg() {
        return groupImg;
    }

    /**
     *
     * @param groupImg
     * The groupImg
     */
    @JsonProperty("groupImg")
    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    /**
     *
     * @return
     * The amtOwedAsGroup
     */
    @JsonProperty("amtOwedAsGroup")
    public String getAmtOwedAsGroup() {
        return amtOwedAsGroup;
    }

    /**
     *
     * @param amtOwedAsGroup
     * The amtOwedAsGroup
     */
    @JsonProperty("amtOwedAsGroup")
    public void setAmtOwedAsGroup(String amtOwedAsGroup) {
        this.amtOwedAsGroup = amtOwedAsGroup;
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