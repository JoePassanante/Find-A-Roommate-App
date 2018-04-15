package ser210.findaroommate.Models;

/**
 * Created by Joe Passanante on 4/12/2018.
 */
public class User{
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String housingPref;
    private String password;
    private String uid;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String email, String uid) {
        this.email = email; this.uid = uid;
    }
    /**
     *
     * @param newPassword - The new password for this user.
     */
    public void setPassword(String newPassword){
        this.setPassword(newPassword,null);
    }

    /**
     *
     * @param newPassword
     * @param oldPassword
     * @return
     */
    public boolean setPassword(String newPassword, String oldPassword){
        //check if we currently have a password, and if someone is trying to run a setup on this.
        if(oldPassword==null && this.password!=null)
            return false;
        //we don't have a password, and the oldpassword is null... so lets set the current password to the new password
        if(oldPassword==null && this.password==null){
            this.password = newPassword;
            return true;
        }
        //user has confirmed old password, so therefore we must be setting the new password.
        if(oldPassword.equals(this.password)){
            this.password=newPassword;
            return true;
        }
        return false;
    }
    public boolean checkSignIn(String password){
        return password.equals(this.password);
    }

    //Setters and Getters
    public void setUid(String s){
        this.uid=s;
    }
    public String getUid(){
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

    public boolean checkPassword(String compare){
        return compare.equals(this.password);
    }
    public String toString(){
        return this.getFirstName() + this.getLastName();
    }
}