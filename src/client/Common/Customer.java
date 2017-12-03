package client.Common;

import java.io.Serializable;

public class Customer implements Serializable{

	private final String username;
    private final String password;

    public Customer(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
