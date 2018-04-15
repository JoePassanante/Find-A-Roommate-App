package ser210.findaroommate.Models;

/**
 * Created by Joe Passanante on 4/12/2018.
 */
public class User {
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String housingPref = "";
    private int partyPreference = 0; //0-5 0 is low, 5 is high

    //required for Auth.
    private String uid = "";
    private String email = "";

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    //Setters and Getters
    public void setUid(String s) {
        this.uid = s;
    }

    public String getUid() {
        return this.uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHousingPref() {
        return housingPref;
    }

    public void setHousingPref(String housingPref) {
        this.housingPref = housingPref;
    }

    public String toString() {
        return this.getFirstName() + this.getLastName();
    }
}