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
        "username",
        "userFullname",
        "userId",
        "billId",
        "groupId",
        "groupName",
        "billName",
        "profilePic",
        "email"
})
public class PendingPayment {

    @JsonProperty("username")
    private String username;
    @JsonProperty("userFullname")
    private String userFullname;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("billId")
    private Integer billId;
    @JsonProperty("groupId")
    private Integer groupId;
    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("billName")
    private String billName;
    @JsonProperty("profilePic")
    private String profilePic;
    @JsonProperty("email")
    private String email;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The username
     */
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The userFullname
     */
    @JsonProperty("userFullname")
    public String getUserFullname() {
        return userFullname;
    }

    /**
     *
     * @param userFullname
     * The userFullname
     */
    @JsonProperty("userFullname")
    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
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
     * The profilePic
     */
    @JsonProperty("profilePic")
    public String getProfilePic() {
        return profilePic;
    }

    /**
     *
     * @param profilePic
     * The profilePic
     */
    @JsonProperty("profilePic")
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     *
     * @return
     * The email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
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