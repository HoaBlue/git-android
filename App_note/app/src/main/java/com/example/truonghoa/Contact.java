package com.example.truonghoa;

public class Contact {
    public String _ID;
    public String owner_email;
    public String contact_fullname;
    public String contact_phonenumber;
    public String contact_address;
    public String contact_email;
    public String contact_avatar;

    public Contact(String _ID, String owner_email, String contact_fullname, String contact_phonenumber, String contact_address, String contact_email, String contact_avatar) {
        this._ID = _ID;
        this.owner_email = owner_email;
        this.contact_fullname = contact_fullname;
        this.contact_phonenumber = contact_phonenumber;
        this.contact_address = contact_address;
        this.contact_email = contact_email;
        this.contact_avatar = contact_avatar;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getContact_fullname() {
        return contact_fullname;
    }

    public void setContact_fullname(String contact_fullname) {
        this.contact_fullname = contact_fullname;
    }

    public String getContact_phonenumber() {
        return contact_phonenumber;
    }

    public void setContact_phonenumber(String contact_phonenumber) {
        this.contact_phonenumber = contact_phonenumber;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_avatar() {
        return contact_avatar;
    }

    public void setContact_avatar(String contact_avatar) {
        this.contact_avatar = contact_avatar;
    }
}
