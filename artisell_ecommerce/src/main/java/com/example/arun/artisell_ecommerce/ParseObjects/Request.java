package com.example.arun.artisell_ecommerce.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Request")
public class Request extends ParseObject
{

    public ParseUser getCustomer()
    {
        return getParseUser("customer");
    }
    public void setCustomer(ParseUser user) {
        put("customer", user);
    }

    public String getRequestDescription() {
        return getString("request_description");
    }

    public void setRequestDescription(String description) {
        put("request_description", description);
    }
    public ParseFile getRequestPhoto() {
        return getParseFile("request_photo");
    }

    public void setRequestPhoto(ParseFile image) {
        put("request_photo", image);
    }

    public int getRequestBudget() {
        return getInt("request_budget");
    }

    public void setRequestBudget(int budget) {
        put("request_budget", budget);
    }

    public Date getRequestDeliverBy() {
        return getDate("request_deliverby");
    }

    public void setRequestDescription(Date date) {
        put("request_deliverby", date);
    }

    public int getRequestStatus() {
        return getInt("status");
    }

    public void setRequestStatus(int status) {
        put("status", status);
    }

    public Category getRequestCategory() {
        return (Category) get("req_category");
    }

    public void setRequestCategory(Category category) {
        put("req_category", category);
    }


    public static ParseQuery<Request> getQuery()
    {
        return ParseQuery.getQuery(Request.class);
    }

}
