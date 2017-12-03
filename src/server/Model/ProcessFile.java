package server.Model;

import java.io.Serializable;

public class ProcessFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private long size;
	private String username;
	// private Account owner;
	private String publicFile;
	private String publicWrite;
	private String publicRead;
	private String notify;

	Account owner = new Account();

	public ProcessFile() {
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getSize() {
		return size;
	}


	public void setSize(long size) {
		this.size = size;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPublicFile() {
		return publicFile;
	}


	public void setPublicFile(String publicFile) {
		this.publicFile = publicFile;
	}


	public String getPublicWrite() {
		return publicWrite;
	}


	public void setPublicWrite(String publicWrite) {
		this.publicWrite = publicWrite;
	}


	public String getPublicRead() {
		return publicRead;
	}


	public void setPublicRead(String publicRead) {
		this.publicRead = publicRead;
	}


	public String getNotify() {
		return notify;
	}


	public void setNotify(String notify) {
		this.notify = notify;
	}


	public Account getOwner() {
		return owner;
	}


	public void setOwner(Account owner) {
		this.owner = owner;
	}


	public ProcessFile(String name, long size, String username,
			String publicFile, String publicWrite, String publicRead) {
		super();
		this.name = name;
		this.size = size;
		this.username = username;
		this.publicFile = publicFile;
		this.publicWrite = publicWrite;
		this.publicRead = publicRead;
		owner.setUsername(username);
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("ProcessFile: [");
		string.append("name: ");
		string.append(name);
		string.append(", size: ");
		string.append(size);
		string.append(", owner: ");
		string.append(username);
		string.append(", publicFile: ");
		string.append(publicFile);
		string.append(", publicWrite: ");
		string.append(publicWrite);
		string.append(", publicRead: ");
		string.append(publicRead);
		string.append("]");
		return string.toString();
	}
}
