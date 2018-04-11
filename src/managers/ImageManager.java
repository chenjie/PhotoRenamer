package managers;

import java.io.*;
import java.util.*;
import photoRenamer.Image;
import photoRenamer.ImageFilter;

/**
 * An image manager that stores all the images under a certain directory. And is
 * able to read or save these images from/to a file.
 * 
 */
public class ImageManager {
	// Store all the Images in a map.
	private static HashMap<String, Image> images;
	// Singleton pattern.
	private static final ImageManager instance = new ImageManager("im.bin");

	/**
	 * Return a map of all images.
	 * 
	 * @return a map of all images
	 */
	public static HashMap<String, Image> getImages() {
		return images;
	}

	/**
	 * Return an ImageManager instance.
	 * 
	 * @return an ImageManager instance
	 */
	public static ImageManager getInstance() {
		return instance;
	}

	/**
	 * An ImageManager.
	 * 
	 * @param filePath
	 *            the path of the Serializable configuration file
	 */
	private ImageManager(String filePath) {

		images = new HashMap<String, Image>();

		// Reads Serializable objects from file.
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
	 * Given a path of file and read the stored images from that file.
	 * 
	 * @param path
	 *            the path of the Serializable configuration file
	 */
	@SuppressWarnings("unchecked")
	private void readFromFile(String path) throws ClassNotFoundException {

		try {
			InputStream file = new FileInputStream(path);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			// deserialize the Map
			images = (HashMap<String, Image>) input.readObject();
			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Given a path of file and save all the images to that file.
	 * 
	 * @param filePath
	 *            the path of the Serializable configuration file
	 * @throws IOException
	 *             if there is something wrong with the file
	 */
	public void saveToFile(String filePath) throws IOException {

		OutputStream file = new FileOutputStream(filePath);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);

		// serialize the Map
		output.writeObject(images);
		output.close();
	}

	/**
	 * Add an image to the images map.
	 * 
	 * @param i
	 *            an Image
	 */
	public void addImage(Image i) {
		ImageManager.images.put(i.getPath(), i);
	}

	/**
	 * Show all images under a directory and save them into an ArrayList arr.
	 * 
	 * @param dir
	 *            a file directory needed to be explored
	 * @param arr
	 *            an empty ArrayList
	 * @return a String containing all the images' absolute paths.
	 */
	public String showAllImageFiles(File dir, ArrayList<String> arr) {
		if (dir.exists()) {
			if (dir.isFile()) {
				if (dir.getName().lastIndexOf('.') != -1) {
					arr.add(dir.getAbsolutePath());
				}
				return dir.getName();
			} else {
				String result = "";
				for (File f : dir.listFiles(new ImageFilter())) {
					result += showAllImageFiles(f, arr) + "\n";
				}
				return result.trim();
			}
		} else {
			return null;
		}
	}

	/**
	 * Return all image files' name.
	 * 
	 * @return a String of all images' name.
	 */
	@Override
	public String toString() {
		String result = "";
		for (Image i : images.values()) {
			result += i.toString() + "\n";
		}
		return result;
	}
}
