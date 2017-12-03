package server.Model;

import java.util.HashMap;
import java.util.Random;

import server.Integration.FileServerDAO;

import client.Common.Customer;

public class AccountManager {
	private final HashMap<Long, Account> LoggedUsers = new HashMap<>();
	private final Random value = new Random();

	public void register(Customer customer) throws AccountAlreadyExistsException {
		FileServerDAO fileServerDAO = new FileServerDAO();
		Account account = fileServerDAO.findUserByName(customer.getUsername());
		if (account == null) {
			fileServerDAO.register(new Account(customer.getUsername(), customer
					.getPassword()));
		} else {
			throw new AccountAlreadyExistsException("Account "
					+ customer.getUsername() + " is already registered!");
		}
	}

	public void unRegister(String username) throws UserException {
		FileServerDAO fileServerDAO = new FileServerDAO();
		fileServerDAO.unRegister(username);
	}

	public long login(Customer customer) {
		FileServerDAO fileServerDAO = new FileServerDAO();
		Account account = fileServerDAO.findUserByName(customer.getUsername());
		if (account != null) {
			if (account.getPassword().equals(customer.getPassword())) {
				long id = value.nextLong();
				LoggedUsers.put(id, account);
				return id;
			}
		}
		return -1;
	}

	public void logout(long key) throws UserException {
		LoggedUsers.remove(key);
	}

	public Account getUser(long key) {
		return (Account) LoggedUsers.get(key);
	}
}
