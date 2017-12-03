package client.Common;

import java.io.Serializable;

public class DTOFIile implements Serializable{
    private String filename;
    private String userModified;
    private String actionTaken;
	public DTOFIile(String filename, String userModified, String actionTaken) {
		super();
		this.filename = filename;
		this.userModified = userModified;
		this.actionTaken = actionTaken;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUserModified() {
		return userModified;
	}
	public void setUserModified(String userModified) {
		this.userModified = userModified;
	}
	public String getActionTaken() {
		return actionTaken;
	}
	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

 
}
