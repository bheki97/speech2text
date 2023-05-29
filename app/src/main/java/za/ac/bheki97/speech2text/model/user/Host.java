package za.ac.bheki97.speech2text.model.user;

import java.io.Serializable;
import java.util.Objects;

public class Host implements Serializable {

    private User account;
    private String brand;
    private int hostId;

    public Host() {
    }

    public Host(User account, String brand) {
        this.account = account;
        this.brand = brand;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return account.equals(host.account) && brand.equals(host.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, brand);
    }

    @Override
    public String toString() {
        return "Host{" +
                "account=" + account.toString() +
                ", brand='" + brand + '\'' +
                '}';
    }
}
