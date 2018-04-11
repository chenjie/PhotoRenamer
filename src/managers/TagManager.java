package managers;

import java.io.*;
import java.util.*;

/**
 * A tag manager that stores all the tags currently available. And is able to
 * read or save these tags from/to a file.
 * 
 */
public class TagManager {
	// A hashSet used to store all the tags.
	private static HashSet<String> allTags;
	// Singleton pattern.
	private static final TagManager instance = new TagManager("tm.bin");
	// Define prefix for tag.
	public final static String PREFIX = "@";

	/**
	 * Return a set of all tags.
	 * 
	 * @return a set of all tags
	 */
	public static HashSet<String> getAllTags() {
		return allTags;
	}

	/**
	 * Return an instance for TagManager.
	 * 
	 * @return an instance for TagManager
	 */
	public static TagManager getInstance() {
		return instance;
	}

	/**
	 * A TagManager.
	 * 
	 * @param filePath
	 *            the path of the Serializable configuration file
	 */
	private TagManager(String filePath) {

		allTags = new HashSet<String>();

		// Reads serializable objects from file.
		// Populates the record list using stored data, if it exists.
		File file = new File(filePath);
		if (file.exists()) {
			try {
				readFromFile(filePath);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Given a path of file and read the stored tags from that file.
	 * 
	 * @param path
	 *            an absolute path of the file to read
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private void readFromFile(String path) throws ClassNotFoundException {

		try {
			InputStream file = new FileInputStream(path);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			// deserialize the Map
			allTags = (HashSet<String>) input.readObject();
			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Given a path of file and save all the tags to that file.
	 * 
	 * @param filePath
	 *            an absolute path of the file to save
	 * @throws IOException
	 *             if there is something wrong with the file
	 */
	public void saveToFile(String filePath) throws IOException {

		OutputStream file = new FileOutputStream(filePath);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);

		// serialize the Map
		output.writeObject(allTags);
		output.close();
	}

	/**
	 * Return the String representation of all tags.
	 * 
	 * @return a String of all tags
	 */
	@Override
	public String toString() {
		String result = "";
		for (String r : allTags) {
			result += r.toString() + "\n";
		}
		return result;
	}
}
