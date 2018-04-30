package ser210.findaroommate.Models;

public class Match {
    public String userIDOne = "";
    public String userIDTwo = "";
    public boolean userOneConfirm = false;
    public boolean userTwoConfirm = false;
    public String myKey = "";

    public Match(){
        //required empty constructor
    }
    public Match(String userOneID, boolean matchone, String userTwoID, boolean matchtwo){
        this.userIDOne = userOneID;
        this.userIDTwo = userTwoID;
        this.userOneConfirm = matchone;
        this.userTwoConfirm = matchtwo;
    }
    public Match(String userOneID, String userTwoID){
        this(userOneID,false,userTwoID,false);
    }
    public String getUserIDOne() {
        return userIDOne;
    }

    public void setUserIDOne(String userIDOne) {
        this.userIDOne = userIDOne;
    }

    public String getUserIDTwo() {
        return userIDTwo;
    }

    public void setUserIDTwo(String userIDTwo) {
        this.userIDTwo = userIDTwo;
    }

    public boolean isUserOneConfirm() {
        return userOneConfirm;
    }

    public void setUserOneConfirm(boolean userOneConfirm) {
        this.userOneConfirm = userOneConfirm;
    }

    public boolean isUserTwoConfirm() {
        return userTwoConfirm;
    }

    public void setUserTwoConfirm(boolean userTwoConfirm) {
        this.userTwoConfirm = userTwoConfirm;
    }

    public String getMyKey() {
        return myKey;
    }

    public void setMyKey(String myKey) {
        this.myKey = myKey;
    }
}
