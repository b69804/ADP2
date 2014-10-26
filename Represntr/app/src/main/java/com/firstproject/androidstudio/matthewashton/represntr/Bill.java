package com.firstproject.androidstudio.matthewashton.represntr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewashton on 10/25/14.
 */
public class Bill {

    private String title;
    private String introduced;
    private String number;
    private String lastUpdate;
    private String committee;
    private String billURI;
    private String billSponsor;

    public Bill(String billNumber, String billTitle){
        this.number = billNumber;
        this.title = billTitle;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduced() {
        return introduced;
    }

    public void setIntroduced(String introduced) {
        this.introduced = introduced;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCommittee() {
        return committee;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }

    public static List<Bill> HOUSE_BILLS = new ArrayList<Bill>();
    public static List<Bill> SENATE_BILLS = new ArrayList<Bill>();

    public String getBillURI() {
        return billURI;
    }

    public void setBillURI(String billURI) {
        this.billURI = billURI;
    }

    public String getBillSponsor() {
        return billSponsor;
    }

    public void setBillSponsor(String billSponsor) {
        this.billSponsor = billSponsor;
    }
}
