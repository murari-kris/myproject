package com.ecom.util;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ActiveUserStore {

    private final Set<String> users = new HashSet<>();

    public Set<String> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        users.remove(username);
    }
}
