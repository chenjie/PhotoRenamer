package photoRenamer;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import managers.TagManager;
import managers.MyLogingSystem;;

/**
 * An image that stores all the tags of this image file (can add or remove tags)
 * and also stores a list of names this image has had. Furthermore, it can
 * revert to its previous name.
 * 
 */
@SuppressWarnings("serial")
public class Image implements Serializable {
	// A list that stores all the names this image has had(from old to new).
	private ArrayList<String> names;

	// The path of this image.
	private String path;

	// All the (non-duplicate) tags of this image file.
	private HashSet<String> tags;

	// The name of this file.
	private String fileName;

	/**
	 * Return the file name of this Image.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Return the absolute path of this Image.
	 * 
	 * @return the absolute path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Return the list of names that this Image has had.
	 * 
	 * @return the ArrayList of names
	 */
	public ArrayList<String> getNames() {
		return names;
	}

	/**
	 * Return the set of all tags of this Image.
	 * 
	 * @return the HashSet of tags
	 */
	public HashSet<String> getTags() {
		return tags;
	}

	/**
	 * An Image.
	 * 
	 * @param s
	 *            the absolute path of this file
	 */
	public Image(String s) {
		this.names = new ArrayList<String>();
		this.path = s;
		this.tags = new HashSet<String>();
		this.fileName = new File(s).getName();
		this.names.add(fileName);
	}

	/**
	 * Add a tag to the image file (and change its name meanwhile) and record
	 * this in MyLogingSystem if log is true.
	 * Precondition: tag is in the allTags available list.
	 * 
	 * @param tag
	 *            a String of tag
	 * @param log
	 *            whether log or not
	 * @see MyLogingSystem
	 */
	public void addTag(String tag, Boolean log) {
		if (this.getTags().contains(tag)) {
		} else if (tag.equals("")) {
		} else {
			File oldFile = new File(path);
			String oldFileName = oldFile.getName();
			int i = oldFileName.lastIndexOf('.');
			String oldFileNameWithoutEx = oldFileName.substring(0, i);
			String extension = oldFileName.substring(i + 1).toLowerCase();

			File newFile = new File(
					oldFile.getParent() + "/" + oldFileNameWithoutEx + TagManager.PREFIX + tag + "." + extension);
			if (oldFile.renameTo(newFile)) {
				this.fileName = newFile.getName();
				this.names.add(fileName);
				this.tags.add(tag);
				File f = new File(path);
				this.path = f.getParent() + "/" + newFile.getName();
				if (log) {
					MyLogingSystem.getLogger()
							.info(MessageFormat.format("TAG(S) ADDED: ({0} -> {1})", oldFileName, newFile.getName()));
				}
			} else {
				System.out.println("Add Tag Error");
			}
		}
	}

	/**
	 * Add a list of tags to the image file (and change its name meanwhile).
	 * Precondition: list of tags is chosen from the allTags available list.
	 * 
	 * @param tags
	 *            a list of Strings of tags
	 * @return whether the file name is changed or not (which means same as
	 *         before)
	 */
	public boolean addTags(List<String> tags) {
		String oldFileName = this.fileName;
		for (String s : tags) {
			addTag(s, false);
		}
		String newFileName = this.fileName;
		if (oldFileName.equals(newFileName)) {
			return false;
		} else {
			MyLogingSystem.getLogger()
					.info(MessageFormat.format("TAG(S) ADDED: ({0} -> {1})", oldFileName, newFileName));
			return true;
		}
	}

	/**
	 * Remove the tag from the image file (and change its name meanwhile) and
	 * record this in MyLogingSystem if log is true.
	 * Precondition: tag is contained in the name of this image.
	 * 
	 * @param tag
	 *            a String of tag
	 * @param log
	 *            whether log or not
	 * @see MyLogingSystem
	 */
	public void removeTag(String tag, Boolean log) {
		if (tag.equals("")) {
		} else {
			File oldFile = new File(path);
			String oldFileName = oldFile.getName();
			int i = oldFileName.lastIndexOf('.');
			String oldFileNameWithoutEx = oldFileName.substring(0, i);
			String extension = oldFileName.substring(i + 1).toLowerCase();
	
			String newName = oldFileNameWithoutEx.replace(TagManager.PREFIX + tag, "");
			File newFile = new File(oldFile.getParent() + "/" + newName + "." + extension);
			if (oldFile.renameTo(newFile)) {
				this.fileName = newFile.getName();
				this.names.add(fileName);
				this.tags.remove(tag);
				File f = new File(path);
				this.path = f.getParent() + "/" + newFile.getName();
				if (log) {
					MyLogingSystem.getLogger()
							.info(MessageFormat.format("TAG(S) REMOVED: ({0} -> {1})", oldFileName, newFile.getName()));
				}
			} else {
				System.out.println("Remove Tag Error");
			}
		}
	}

	/**
	 * Remove a list of tags from the image file (and change its name
	 * meanwhile).
	 * Precondition: list of tags is chosen from the name of this image.
	 * 
	 * @param tags
	 *            a list of Strings of tags
	 */
	public void removeTags(List<String> tags) {
		String oldFileName = this.fileName;
		for (String s : tags) {
			removeTag(s, false);
		}
		String newFileName = this.fileName;
		MyLogingSystem.getLogger()
				.info(MessageFormat.format("SET OF TAGS REMOVED: ({0} -> {1})", oldFileName, newFileName));
	}

	/**
	 * Revert the name of this image file to a previous name (given as an input)
	 * and change the file name meanwhile.
	 * Precondition: newName is in the list of image's previous names.
	 * 
	 * @param newName
	 *            one of this Image's previous name
	 */
	public void revertName(String newName) {
		if (names.size() == 1) {
			System.out.println("No name to revert back to!");
		} else {
			File oldFile = new File(path);
			File newFile = new File(oldFile.getParent() + "/" + newName);
			if (oldFile.renameTo(newFile)) {
				this.names.add(newFile.getName());
				// figure out the tags of this new name.
				this.tags.clear();
				int dotIndex = newName.lastIndexOf(".");
				if (newName.lastIndexOf("@") != -1) {
					int currentIndex = newName.lastIndexOf("@");
					String firstTag = newName.substring(currentIndex + 1, dotIndex);
					this.tags.add(firstTag);

					for (int i = currentIndex; (newName.lastIndexOf("@", i - 1)) != -1;) {
						int previousIndex = i;
						i = newName.lastIndexOf("@", i - 1);
						this.tags.add(newName.substring(i + 1, previousIndex));
					}
				}
				File f = new File(path);
				this.path = f.getParent() + "/" + newName;
				this.fileName = newName;
				MyLogingSystem.getLogger().info(
						MessageFormat.format("NAME REVERTED: ({0} -> {1})", oldFile.getName(), newFile.getName()));
			} else {
				System.out.println("Error");
			}
		}
	}

	/**
	 * Return the image's file name.
	 * 
	 * @return the String of file name
	 */
	@Override
	public String toString() {
		return this.getFileName();
	}

}
