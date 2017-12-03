package server.Model;

import java.io.Serializable;

public class Account implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long userID;

    public String username;

    public String password;

    public Account(long userID, String username, String password) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
	}

	public Account() {
        this(null,null);
    }

    public void setUserID(long userID) {
		this.userID = userID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getUserID() {
        return userID;
    }
}
