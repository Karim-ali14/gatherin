
package com.orwa.gatherin.Models.HomeModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription {

    @SerializedName("max-groups")
    @Expose
    private String maxGroups;
    @SerializedName("max-departments")
    @Expose
    private String maxDepartments;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("currency")
    @Expose
    private String currency;

    public String getMaxGroups() {
        return maxGroups;
    }

    public void setMaxGroups(String maxGroups) {
        this.maxGroups = maxGroups;
    }

    public String getMaxDepartments() {
        return maxDepartments;
    }

    public void setMaxDepartments(String maxDepartments) {
        this.maxDepartments = maxDepartments;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
