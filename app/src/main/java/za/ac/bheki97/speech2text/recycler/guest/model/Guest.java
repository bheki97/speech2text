package za.ac.bheki97.speech2text.recycler.guest.model;

import za.ac.bheki97.speech2text.model.user.User;

public class Guest {

    private int guestId;
    private User account;
    private String joindate;

    public Guest() {
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", account=" + account +
                ", joindate='" + joindate + '\'' +
                '}';
    }
}
