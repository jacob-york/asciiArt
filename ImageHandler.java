package asciiArtSrs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

// written by Jacob York

public class ImageHandler {
	
	private static final String[] ACCEPTED_FORMATS = {"jpg", "jpeg", "png"};
	private Path curPath;
	private BufferedImage curImage;
	
	// constructor
	public ImageHandler() {
		curPath = null;
		curImage = null;
	}
	
	// getters
	public Path getCurImagePath() {
		return curPath;
	}
	public String getCurImageName() {
		if (hasImage())
			return curPath.getFileName().toString();
		else return null;
	}
	public static String[] getAcceptedFormats() {
		return ACCEPTED_FORMATS.clone();
	}
	public String getCurFormat() {
		return getFormat(curPath);
	}
	public BufferedImage getCurImage() {
		return curImage;
	}
	public int[][] getShadingRaster() {
		int sRWidth = curImage.getWidth();
		int sRHeight = curImage.getHeight();
		int[][] shadingRaster = new int[sRHeight][sRWidth];
		
		int pixelColor = 0;
		for (int y = 0; y < sRHeight; y++) {
			for(int x = 0; x < sRWidth; x++) {
				pixelColor = curImage.getRGB(x, y);
				shadingRaster[y][x] = desaturate(pixelColor);
			}
		}
		return shadingRaster;
	}
	
	// setters
	public int setImage(Path newImgPath) {
		try {
			curImage = ImageIO.read(newImgPath.toFile());
		}
		catch (IOException e) {
			curImage = null;
			return 1;  // file not found.
		}
		
		if (setPath(newImgPath) == 1)
			return 2;  // file was found, but the format is not accepted.
		
		return 0;
	}
	public int setImage(File newImgPath) {	
		try {
			curImage = ImageIO.read(newImgPath);
		}
		catch (IOException e) {
			curImage = null;
			return 1;  // file not found.
		}
		
		if (setPath(newImgPath.toPath()) == 1)
			return 2;  // file was found, but the format is not accepted.
		
		return 0;
	}
	public int setImage(String newImgPath) {	
		Path newPath = Paths.get(newImgPath);
		try {
			curImage = ImageIO.read(newPath.toFile());
		}
		catch (IOException e) {
			curImage = null;
			return 1;  // file not found.
		}
		if (setPath(newPath) == 1)
			return 2;  // file was found, but the format is not accepted.
		
		return 0;
	}
	
	private int setPath(Path path) {
		String formatOfImage = getFormat(path);
		
		if (isAcceptedFormat(formatOfImage)) {
			curPath = path;
			return 0;
		}
		else {
			curPath = null;
			return 1;  // format of path is not accepted.
		}
	}
	
	public String toString() {
		String returnVal = "";
		returnVal += "image loaded: " + hasImage() + "\n";
		if (hasImage()) {
			returnVal += "current path: " + curPath.toString() + "\n";
			returnVal += "current buffered image info:\n";
			returnVal += curImage.toString();
		}
		return returnVal;
	}
	
	public static boolean isAcceptedFormat(String format) {
		for (String element : ACCEPTED_FORMATS) {
			if (element.equals(format.toLowerCase()))
				return true;
		}
		return false;
	}
	
	public static String getFormat(Path path) {
		if (path == null)
			return null;
		
		String pathString = path.getFileName().toString();
		int pathExtensionStart = pathString.lastIndexOf('.') + 1;
		
		return pathString.substring(pathExtensionStart);
	}
	public static String getFormat(File file) {
		if (file == null)
			return null;
		
		String fileString = file.toString();
		int fileExtensionStart = fileString.lastIndexOf('.') + 1;
		
		return fileString.substring(fileExtensionStart);
	}
	
	// algorithm for desaturating a color. Returns an int value (0-255) to represent pixel's shade of grey.
	private static int desaturate(int color) {
	
		int a = (color & 0xff000000) >> 24;
		int r = (color & 0xff0000) >> 16;
		int g = (color & 0xff00) >> 8;
		int b = color & 0xff;
		
		if (a == 0) {  // TODO: quick-fix for alpha values. Make it more sophisticated.
			return 255;
		}
		else {
			if (r > g) {
				if (r > b) {
					// r is bigger than both, so it's the primary color
					while (r > g && r > b) {
						// desaturate the pixel by lowering primaries and raising secondaries until the secondaries pass the primaries
						r --;
						g ++;
						b ++;
					}
				}
				else if (r < b) {
					while (b > r && b > g) {
						r ++;
						g ++;
						b --;
					}
				}
				else if (r == b) {
					while (r > g) {
						r --;
						g ++;
						b --;
					}
				}
			}
			else if (g > r) {
				if (g > b) {
					while (g > r && g > b) {
						r ++;
						g --;
						b ++;
					}
				}
				else if (g < b) {
					while (b > r && b > g) {
						r ++;
						g ++;
						b --;
					}
	
				}
				else if (g == b) {
					while (g > r) {
						r ++;
						g --;
						b --;
					}
				}
			}
			else if (r == g) {
				if (r > b) {
					while (r > b) {
						r --;
						g --;
						b ++;
					}
				}
				else if (r < b) {
					while (b > r && b > g) {
						r ++;
						g ++;
						b --;
					}
				}
				else if (g == b) {
					return r;  // arriving here means the pixel is already grey, so just return any color value
				}
			}
			
			return (r + g + b) / 3;  // after your process, average the three
		}
	}
	
	public boolean hasImage() {
		if (curImage == null || curPath == null)
			return false;
		else return true;
	}
	
	public void reset() {
		curPath = null;
		curImage = null;
	}
}
