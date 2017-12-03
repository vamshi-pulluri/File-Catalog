package server.Manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerManager {

	private final String root = "./serverFiles";
	private Path workingDir = Paths.get(root);

	public ServerManager() {

	}

	public byte[] readFile(String filename) throws IOException {
		Path file = workingDir.resolve(Paths.get(filename));
		System.out.println("enter SERVERMANAGER");
		return Files.readAllBytes(file);
	}

	public void deleteFile(String filename) throws IOException {
		Path path = workingDir.resolve(Paths.get(filename));
		System.out.println("enter deletefile");
		Files.deleteIfExists(path);
	}

	public void writeFile(String filename, byte[] bytes) throws IOException {
		Path file = workingDir.resolve(Paths.get(filename));
		System.out.println("enter writefile");
		Files.write(file, bytes);
	}
}
