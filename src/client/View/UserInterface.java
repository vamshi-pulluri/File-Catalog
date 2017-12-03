package client.View;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import server.Model.*;

import client.Common.*;
import client.Manager.ClientManager;

public class UserInterface implements Runnable {
	ServerFileCatalog fileServer;

	private boolean running = false;
	private final Scanner input = new Scanner(System.in);
	private long key = -1;
	private static final String PROMPT = "> ";
	private final ThreadSafeStdOut consoleOut = new ThreadSafeStdOut();
	ClientManager clientManager = new ClientManager();
	private ReceiveMsg serverOutput;

	public UserInterface() throws RemoteException {
		this.serverOutput = new ReceiveMsg();
	}

	public void start(ServerFileCatalog fileServer) {
		this.fileServer = fileServer;
		if (running) {
			return;
		}
		running = true;
		new Thread(this).run();
	}

	@Override
	public void run() {
		consoleOut.println("Welcome Everyone!");
		consoleOut.println("Type HELP to List of Commands that you can use");
		while (running) {
			try {
				consoleOut.print(PROMPT);
				String filename;
				String in = input.nextLine();
				if (in.equals("")) {
					consoleOut.println("");
					continue;
				}
				CommandLine cmd = new CommandLine(in);
				switch (cmd.getCmd()) {
				case QUIT:
					running = false;
					serverOutput = null;
					break;
				case LOGIN:
					if (cmd.getArgs().length >= 2) {
						Customer c = new Customer(cmd.getArgs()[0],
								cmd.getArgs()[1]);
						long result = fileServer.login(c, serverOutput);
						if (result == -1) {
							consoleOut.println("Sorry, wrong login details");
						} else {
							consoleOut.println("logged in");
							key = result;
						}
					} else {
						consoleOut
								.println("Error, Enter both username and password.");
					}
					break;
				case LOGOUT:
					fileServer.logout(key);
					consoleOut.println("logged out!");
					break;
				case REGISTER:
					if (cmd.getArgs().length >= 2) {
						System.out.println("hello all UI");
						fileServer.register(new Customer(cmd.getArgs()[0], cmd
								.getArgs()[1]));
						consoleOut
								.println("Congrats, account registered to database");
					} else {
						consoleOut
								.println("Error,provide a username and password to register.");
					}
					break;
				case UNREGISTER:
					if (cmd.getArgs().length >= 1) {
						fileServer.unRegister(cmd.getArgs()[0]);
						consoleOut
								.println("Success, account removed from database");
					} else {
						consoleOut
								.println("Provide a valid username to unregister.");
					}
					break;
				case DELETE:
					if (cmd.getArgs().length >= 1) {
						fileServer.deleteFileFromDB(key, cmd.getArgs()[0]);
						consoleOut.println("File deleted successfully");
					} else {
						consoleOut
								.println("Sorry, enter the proper filename. Type Help to know the input format");
					}
					break;
				case LIST:
					if (cmd.getArgs().length >= 1) {
						formatFilesOutput(fileServer.listFiles(key),
								cmd.getArgs()[0]);
					}
					break;
				case UPLOAD:
					upload(cmd);
					break;
				case DOWNLOAD:
					if (cmd.getArgs().length >= 1) {
						filename = cmd.getArgs()[0];
						clientManager.writeFile(filename,
								fileServer.download(key, filename));
						consoleOut.println("Download Success");
					} else {
						consoleOut.println("Enter a valid filename.");
					}
					break;
				case NOTIFY:
					if (cmd.getArgs().length >= 2) {
						fileServer.notifyFile(key, cmd.getArgs()[0],
								cmd.getArgs()[1]);
						consoleOut.println("Notify registered for file: "
								+ cmd.getArgs()[0]);
					} else {
						consoleOut
								.println("Not enough arguments. Notify <filename> <True/False>.");
					}
					break;
				case MODIFY:
					modifyFile(cmd);
					break;
				case HELP:
					help();
					break;
				case ILLEGAL:
					consoleOut
							.println("Illegal Command. Type HELP command to list the commands you can use");
					break;
				}
			} catch (AccountAlreadyExistsException e) {
				consoleOut
						.println("Username already in use. Please pick another username.");
			} catch (FileNotFoundException e) {
				consoleOut.println("Could not find file");
			}

			catch (UnauthorizedAccessException | IOException | UserException
					| FileException e) {
				consoleOut.println("Operation failed");
				consoleOut.println(e.getMessage());
			}
		}
	}

	public void modifyFile(CommandLine cmd) throws RemoteException,
			FileNotFoundException, FileException, UnauthorizedAccessException,
			UserException {
		String filename1 = "";
		String publicFile1 = "";
		String publicWrite1 = "";
		String publicRead1 = "";

		switch (cmd.getArgs().length) {
		case 0:
			consoleOut.print("Enter the filename ");
			filename1 = input.nextLine();
			consoleOut.print("Should it be a public file? Enter true/false: ");
			publicFile1 = input.nextLine();
			consoleOut
					.print("Should it have public write access? Enter true/false: ");
			publicWrite1 = input.nextLine();
			consoleOut
					.print("Should it have public read access? Enter true/false: ");
			publicRead1 = input.nextLine();
			break;
		}
		if (filename1 != "") {
			fileServer.modifyFileContents(key, filename1, publicFile1,
					publicWrite1, publicRead1);
			consoleOut.println("Successfully modified");
		}
	}

	private void upload(CommandLine cmd) throws FileException,
			UnauthorizedAccessException, IOException {
		String filename = "";
		String publicFile = "";
		String publicWrite = "";
		String publicRead = "";

		switch (cmd.getArgs().length) {
		case 0:
			consoleOut.print("Enter the filename ");
			filename = input.nextLine();
			consoleOut.print("Should it be a public file? Enter true/false: ");
			publicFile = input.nextLine();
			consoleOut
					.print("Should it have public write access? Enter true/false: ");
			publicWrite = input.nextLine();
			consoleOut
					.print("Should it have public read access? Enter true/false: ");
			publicRead = input.nextLine();
			break;
		}
		if (filename.equals("")) {
			consoleOut.println("Sorry, File missing!");
		} else {
			try {
				byte[] data = clientManager.readFile(filename);
				// System.out.println("data" + data.length);
				fileServer.upload(key, filename, data, publicFile, publicWrite,
						publicRead);
				consoleOut.println("File uploaded successfully!");
			} catch (FileNotFoundException e) {
				consoleOut.println("No files found");
			}
		}
	}

	private void formatFilesOutput(ArrayList<ProcessFile> files, String name) {
		if (files.size() == 0) {
			consoleOut.println("File store is empty");
		}
		for (ProcessFile file : files) {
			if (file.getPublicFile().equalsIgnoreCase("true")
					|| file.getOwner().getUsername().equalsIgnoreCase(name)) {
				consoleOut.println("The File Contents are:");
				consoleOut.println("File-owner :"
						+ file.getOwner().getUsername() + ", Public: "
						+ file.getPublicFile() + ", Writable: "
						+ file.getPublicWrite() + ", Readable: "
						+ file.getPublicRead() + ", Size of the file: "
						+ file.getSize() + ", Filename: " + file.getName());
			}
		}
	}

	private void help() {
		consoleOut.println("Quit" + "-" + "To end the process");
		consoleOut.println("Register <username> <password>" + "-"
				+ "To register a customer");
		consoleOut.println("Unregister <username>" + "-"
				+ "Deletes user from database");
		consoleOut.println("Login <username> <password>" + "-"
				+ "Can login only if registered");
		consoleOut.println("Logout" + "-" + "can logout");
		consoleOut.println("List" + "-" + "Lists all the files in directory");
		consoleOut.println("Upload" + "->" + "To upload files to a database");
		consoleOut.println("Download <filename>" + "-"
				+ "Download file from database");
		consoleOut.println("Delete <filename>" + "-"
				+ "Delete file from database");
		consoleOut
				.println("Modify <filename> <PublicFile> <publicWrite> <publicRead>"
						+ "-" + "modifies the file stored earlier");
		consoleOut.println("Notify <filename> <True> or <False>" + "-"
				+ "Notifies the customer");
	}

	private class ReceiveMsg extends UnicastRemoteObject implements
			ClientFileCatalog {
		public ReceiveMsg() throws RemoteException {
		}

		@Override
		public void handleMsg(DTOFIile message) throws RemoteException {
			consoleOut.println("File: " + message.getFilename()
					+ " was modified by: " + message.getUserModified()
					+ ". Action taken was: " + message.getActionTaken());
			consoleOut.print(PROMPT);
		}
	}
}
