package tu.thesis.onlinebanking.pk.Model;

import java.util.*;

public class UserData

{

    public String uid="";

    public String name="";
    public String birthday="";

    public String phone="";
    public String email="";
    public long amount=0;
    public String address="";
    public String nationalID="";
    public String account="";
    public String password ="";











    public UserData(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public UserData(String name, String birthday, String phone, String email,
//
//                    String amount, String address, String nationalID, String account, String uid){
//
//        this.name=name;
//        this.birthday=birthday;
//        this.phone=phone;
//        this.email=email;
//        this.amount=amount;
//        this.address=address;
//      this.nationalID=nationalID;
//        this.uid=uid;
//        this.account=account;
//    }

}