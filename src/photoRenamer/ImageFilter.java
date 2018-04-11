package photoRenamer;

import java.io.*;

/**
 * A File filter that is able to distinguish image files from other types of
 * files.
 * 
 */
public class ImageFilter implements FileFilter {
	// Declare all types of images.
	public final static String jpeg = "jpeg";
	public final static String jpg = "jpg";
	public final static String gif = "gif";
	public final static String tiff = "tiff";
	public final static String tif = "tif";
	public final static String png = "png";
	public final static String bmp = "bmp";

	/**
	 * Return true if the file is a directory or has the correct image type
	 * extension, false otherwise.
	 * 
	 * @param f
	 *            any type of file
	 * @return true if this file is a directory or has the correct image type
	 *         extension
	 */
	@Override
	public boolean accept(File f) {
		String s = f.getName();
		int i = s.lastIndexOf('.');
		String extension = s.substring(i + 1).toLowerCase();
		if (extension.equals(tiff) || extension.equals(tif) || extension.equals(gif) || extension.equals(jpeg)
				|| extension.equals(jpg) || extension.equals(png)) {
			return true;
		} else if (i == -1) {
			return true;
		} else {
			return false;
		}
	}

}
