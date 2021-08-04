package ru.job4j.cinema.model;

import java.util.Objects;

public class Account {
    private String username;
    private String email;
    private String phone;

    public Account(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return username.equals(account.username)
                && email.equals(account.email)
                && phone.equals(account.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, phone);
    }
}
