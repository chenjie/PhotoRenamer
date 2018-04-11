package unitTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import managers.ImageManager;
import photoRenamer.Image;

public class ImageManagerTest {
	// Get the instance of ImageManager
	ImageManager im = ImageManager.getInstance();

	/** The test directory structures. */
	private final static String TEST_PATH = ".";

	/** The first test directory. */
	private String TEST_DIR = TEST_PATH + "/" + "uoft";

	/** The File for the first and second test directory. */
	private File testFile1, testFile2;

	/**
	 * Create the test directory structures.
	 * 
	 * @throws IOException
	 *             if there is something wrong with files
	 */
	@Before
	public void setUp() throws IOException {
		// Creating folders
		testFile1 = new File(TEST_DIR);
		testFile1.mkdir();
		testFile2 = new File(TEST_DIR + "/uoft2");
		testFile2.mkdir();

		// Creating files
		new File(TEST_DIR + "/innisCollege.jpg").createNewFile();
		new File(TEST_DIR + "/newCollege.jpg").createNewFile();
		new File(TEST_DIR + "/uoft2/victoriaCollege.jpg").createNewFile();
	}

	/**
	 * Test showing all image files under a directory.
	 */
	@Test
	public void testShowAllImageFiles() {
		ArrayList<String> a1 = new ArrayList<String>();
		File dir = new File("uoft");
		String output = im.showAllImageFiles(dir, a1);
		assertEquals("innisCollege.jpg\nnewCollege.jpg\nvictoriaCollege.jpg", output);
	}

	/**
	 * Test adding and saving images.
	 */
	@Test
	public void testAddWithSaveAndWithoutSave() {
		Image i = new Image("uoft/innisCollege.jpg");
		im.addImage(i);
		try {
			im.saveToFile("im.bin");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("innisCollege.jpg\n", im.toString());

		Image i2 = new Image("uoft/uoft2/victoriaCollege.jpg");
		im.addImage(i2);
		assertEquals("victoriaCollege.jpg\ninnisCollege.jpg\n", im.toString());
	}

}
