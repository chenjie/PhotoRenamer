package unitTest;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;
import managers.TagManager;

public class TagManagerTest {
	// Get the instance of TagManager
	TagManager tm = TagManager.getInstance();

	/**
	 * Test adding and saving tags.
	 */
	@Test
	public void testAddAndSave() {
		TagManager.getAllTags().add("first");
		try {
			tm.saveToFile("tm.bin");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("first\n", tm.toString());
	}

	/**
	 * Test adding another tag without saving.
	 */
	@Test
	public void testAddAnotherWithoutSaving() {
		TagManager.getAllTags().add("second");
		assertEquals("first\nsecond\n", tm.toString());
	}

}
