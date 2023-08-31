
package com.orwa.gatherin.Models.SubscriptionPackagesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    String checked = "false";

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("max-groups")
    @Expose
    private String maxGroups;
    @SerializedName("max-departments")
    @Expose
    private String maxDepartments;
    @SerializedName("id")
    @Expose
    private String id;

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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}
