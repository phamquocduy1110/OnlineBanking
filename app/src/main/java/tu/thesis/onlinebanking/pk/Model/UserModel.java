package tu.thesis.onlinebanking.pk.Model;

public class UserModel {

    public String uid="";
    public String name="";
    public String email="";
    public String password="";
    public String phone="";
    public long amount=0;
    public String birthday="";
    public String nationalID="";
    public String address="";

    public UserModel(){}

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public long getAmount() {
        return amount;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getNationalID() {
        return nationalID;
    }

    public String getAddress() {
        return address;
    }
}
