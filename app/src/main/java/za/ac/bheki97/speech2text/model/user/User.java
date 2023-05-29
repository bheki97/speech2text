package za.ac.bheki97.speech2text.model.user;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private String email;
    private String idNumber;
    private String firstname;
    private String lastname;
    private Character gender;
    private String mobileNumber;
    private String language;
    private String password;


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getContactNo() {
        return mobileNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setContactNo(String contactNo) {
        this.mobileNumber = contactNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) && idNumber.equals(user.idNumber) && firstname.equals(user.firstname) && lastname.equals(user.lastname) && gender.equals(user.gender) && mobileNumber.equals(user.mobileNumber) && language.equals(user.language) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, idNumber, firstname, lastname, gender, mobileNumber, language, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gender=" + gender +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", language='" + language + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
