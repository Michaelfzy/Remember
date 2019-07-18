package com.example.cardview_test;

public class Family {

    private String name;
    private String phone;
    private String more;
    private boolean checked;

    public Family(String name, String phone, String more) {
        super();
        this.name = name;
        this.phone = phone;
        this.more = more;

    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

}
