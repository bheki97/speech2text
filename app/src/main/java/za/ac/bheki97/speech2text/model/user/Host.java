package za.ac.bheki97.speech2text.model.user;

public class Host {

    private User account;
    private String brand;

    public Host() {
    }

    public Host(User account, String brand) {
        this.account = account;
        this.brand = brand;
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
    public String toString() {
        return "Host{" +
                "account=" + account.toString() +
                ", brand='" + brand + '\'' +
                '}';
    }
}
