package unitTest;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import photoRenamer.Image;

public class ImageTest {
	/** The test directory structures. */
	private final static String TEST_PATH = ".";

	/** The first test directory. */
	private String TEST_DIR = TEST_PATH + "/" + "test_dir";

	/** The File for the first test directory. */
	private File testFile;

	/**
	 * Create the test directory structures.
	 * 
	 * @throws IOException
	 *             if there is something wrong with files
	 */
	@Before
	public void setUp() throws IOException {
		testFile = new File(TEST_DIR);
		testFile.mkdir();

		// Create different files with different extensions
		new File(TEST_DIR + "/test1.jpg").createNewFile();
		new File(TEST_DIR + "/test2.jpeg").createNewFile();
		new File(TEST_DIR + "/test3.bmp").createNewFile();
	}

	/**
	 * Delete the test folder.
	 * 
	 * @throws IOException
	 *             if there is something wrong with files
	 */
	@After
	public void tearDown() throws IOException {
		deleteFolder(new File(TEST_DIR));
	}

	/**
	 * Delete the dir folder.
	 * 
	 * @param dir
	 *            test directory folder
	 */
	public void deleteFolder(File dir) {
		for (File f : dir.listFiles()) {
			f.delete();
		}
		dir.delete();
	}

	/**
	 * Test adding tag.
	 */
	@Test
	public void testAddTag() {
		// Test lower case tag
		Image i1 = new Image(TEST_DIR + "/test1.jpg");
		i1.addTag("awesome", true);
		assertEquals("test1@awesome.jpg", i1.toString());

		// Test upper case tag
		Image i2 = new Image(TEST_DIR + "/test2.jpeg");
		i2.addTag("GOOD", true);
		assertEquals("test2@GOOD.jpeg", i2.toString());

		// Test adding empty tag
		Image i3 = new Image(TEST_DIR + "/test3.bmp");
		i3.addTag("", false);
		assertEquals("test3.bmp", i3.toString());
	}

	/**
	 * Test adding a list of tags.
	 */
	@Test
	public void testAddTags() {
		// Test adding a list of tags.
		Image i1 = new Image(TEST_DIR + "/test1.jpg");
		List<String> tags1 = new ArrayList<String>();
		tags1.add("best");
		tags1.add("elite");
		i1.addTags(tags1);
		assertEquals("test1@best@elite.jpg", i1.toString());

		// Test adding an empty list of tags.
		Image i2 = new Image(TEST_DIR + "/test2.jpeg");
		List<String> tags2 = new ArrayList<String>();
		i2.addTags(tags2);
		assertEquals("test2.jpeg", i2.toString());
	}

	/**
	 * Test removing tag.
	 */
	@Test
	public void testRemoveTag() {
		// Test lower case tag
		Image i1 = new Image(TEST_DIR + "/test1.jpg");
		i1.addTag("awesome", false);
		i1.removeTag("awesome", true);
		assertEquals("test1.jpg", i1.toString());

		// Test upper case tag
		Image i2 = new Image(TEST_DIR + "/test2.jpeg");
		i2.addTag("GOOD", false);
		i2.removeTag("GOOD", true);
		assertEquals("test2.jpeg", i2.toString());

		// Test removing empty tag
		Image i3 = new Image(TEST_DIR + "/test3.bmp");
		i3.removeTag("", false);
		assertEquals("test3.bmp", i3.toString());
	}

	/**
	 * Test removing a list of tags.
	 */
	@Test
	public void testRemoveTags() {
		// Test removing a list of tags.
		Image i1 = new Image(TEST_DIR + "/test1.jpg");
		i1.addTag("a", false);
		i1.addTag("b", false);
		i1.addTag("c", false);
		List<String> tags1 = new ArrayList<String>();
		tags1.add("a");
		tags1.add("b");
		i1.removeTags(tags1);
		assertEquals("test1@c.jpg", i1.toString());

		// Test removing an empty list of tags.
		Image i2 = new Image(TEST_DIR + "/test2.jpeg");
		List<String> tags2 = new ArrayList<String>();
		i2.removeTags(tags2);
		assertEquals("test2.jpeg", i2.toString());
	}

	/**
	 * Test reverting name.
	 */
	@Test
	public void testRevertName() {
		// Revert to previous name.
		Image i1 = new Image(TEST_DIR + "/test1.jpg");
		i1.addTag("a", false);
		i1.revertName("test1.jpg");
		assertEquals("test1.jpg", i1.toString());

		// No previous name to be reverted.
		Image i2 = new Image(TEST_DIR + "/test2.jpeg");
		i2.revertName("test2@a.jpeg");
		assertEquals("test2.jpeg", i2.toString());
	}
}
