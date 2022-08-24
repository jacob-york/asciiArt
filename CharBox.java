package asciiArtSrs;

// written by Jacob York

public class CharBox {
	
	private int xPos;  // top-left pixel of rectangle
	private int yPos;  // 
	private int width;
	private int height;
	private int area;
	
	public CharBox(int width) {
		xPos = 0;
		yPos = 0;
		this.width = width;
		this.height = width * 2;
		this.area = width * height;
	}
	
	// getters
	public int getX() {
		return xPos;
	}
	public int getY() {
		return yPos;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getArea() {
		return area;
	}
	public int getGrayVal (int[][] shadingRaster) { // 0-255 avrg that represents how dark the region outlined by Rectangle is
		int sumOfPixels = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sumOfPixels += shadingRaster[yPos + y][xPos + x];
			}
		}
		return sumOfPixels / area;
	}
	
	// setters
	public void setPos(int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	public String toString()
	{
		String returnVal = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				returnVal += "[]";
			}
			returnVal += "\n";
		}
		returnVal += ("top-left pixel coords: (" + xPos + ", " + yPos + ")\n");
		returnVal += ("width: " + width + " pixels\n");
		returnVal += ("height: " + height + " pixels\n");
		returnVal += ("greyVal: " + "\n");
		return returnVal;
	}

	public char loadChar(String palette, int[][] shadingRaster) { 
		// checks
		if (!(palette.length() == 16))
			throw new ArrayIndexOutOfBoundsException("Palette must be 16 elements long exactly.");
		
		int index = (int) Math.floor(getGrayVal(shadingRaster) / 16);
		return palette.charAt(index);
	}
	
}
