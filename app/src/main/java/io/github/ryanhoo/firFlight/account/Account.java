package io.github.ryanhoo.firFlight.account;

import io.github.ryanhoo.firFlight.data.model.User;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/2/16
 * Time: 5:43 PM
 * Desc: Account
 */
public class Account {

    public Account() {
        // Empty Constructor
    }

    public Account(String name) {
        this.name = name;
    }

    private String name;

    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
