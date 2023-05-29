package za.ac.bheki97.speech2text.recycler.guest.model;

import java.io.Serializable;
import java.util.Objects;

import za.ac.bheki97.speech2text.model.user.User;

public class Guest implements Serializable {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return guestId == guest.guestId && account.equals(guest.account) && joindate.equals(guest.joindate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId, account, joindate);
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
