package io.github.ryanhoo.firFlight.event;

import io.github.ryanhoo.firFlight.data.model.User;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/2/16
 * Time: 11:53 PM
 * Desc: UserUpdatedEvent
 */
public class UserUpdatedEvent {

    public User user;

    public UserUpdatedEvent(User user) {
        this.user = user;
    }
}
