package asciiArtSrs;

// written by Jacob York

public class AsciiImage {
	
	private CharBox charBox;
	
	public static final String DEFAULT_PALETTE = "@N#bhyo+s/=-:.` ";
	
	private int domain;
	private int range;
	
	private String basePalette;
	private String activePalette;
	
	private boolean isInverted;
	
	private int[][] shadingRaster;
	
	// constructor(s)
	public AsciiImage(int[][] shadingRaster) {
		this.shadingRaster = shadingRaster;
		charBox = new CharBox(1);
		isInverted = false;
		basePalette = DEFAULT_PALETTE;
		activePalette = DEFAULT_PALETTE;
		
		updateDomainAndRange();
	}
	public AsciiImage(int[][] shadingRaster, int charWidth) {
		this.shadingRaster = shadingRaster;
		charBox = new CharBox(charWidth);
		isInverted = false;
		basePalette = DEFAULT_PALETTE;
		activePalette = DEFAULT_PALETTE;
		
		updateDomainAndRange();
	}
	public AsciiImage(int[][] shadingRaster, int charWidth, boolean invertedShading) {
		this.shadingRaster = shadingRaster;
		charBox = new CharBox(charWidth);
		isInverted = invertedShading;
		basePalette = DEFAULT_PALETTE;
		if (isInverted)
			activePalette = reverseString(basePalette);
		else activePalette = basePalette;	
		
		updateDomainAndRange();
	}
	
	private void updateDomainAndRange() {
		int SRWidth = shadingRaster[0].length;
		int SRHeight = shadingRaster.length;
		
		domain = SRWidth - (SRWidth % charBox.getWidth());
		range = SRHeight - (SRHeight % charBox.getHeight());
	}
	
	// getters
	public int getWidth() {
		return domain / charBox.getWidth();
	}
	public int getHeight() {
		return range / charBox.getHeight();
	}
	public int getArea() {
		return getWidth() * getHeight();
	}
	public int getCharWidth() {
		return charBox.getWidth();
	}
	public String getPalette() {
		return basePalette;
	}
	public int[][] getShadingRaster() {
		return shadingRaster.clone();
	}
	
	// setters
	public int setCharWidth(int newCharWidth) {
		if (newCharWidth <= 0)
			return 1;  // error code 1: charWidth is less than or equal to 0
		
		if (newCharWidth > shadingRaster[0].length || (newCharWidth * 2) > shadingRaster.length)
			return 2;  // error code 2: charWidth is too large for this shadingRaster
		
		charBox = new CharBox(newCharWidth);
		
		updateDomainAndRange();
		return 0;
	}
	public int setPalette(String newPalette) {
		if (newPalette.length() != 16)
			return 1;
		
		basePalette = newPalette;
		if (isInverted)
			activePalette = reverseString(basePalette);
		else activePalette = basePalette;
		
		return 0;
	}
	public int setShadingRaster(int[][] newShadingRaster) {
		
		if (newShadingRaster[0].length < charBox.getWidth() || newShadingRaster.length < charBox.getHeight())
			return 1;
		
		shadingRaster = newShadingRaster;
		
		updateDomainAndRange();
		return 0;
	}
	
	public String toString() {
		String[] stringArray = toStringArray();
		
		String returnVal = "";
		for (int i = 0; i < stringArray.length; i++) {
			returnVal += stringArray[i];
			returnVal += "\n";
		}
		
		return returnVal;
	}
	public String[] toStringArray() {
		String[] stringArray = new String[getHeight()];
		
		// shadingRaster position
		int y;
		int x;
		// stringArray position
		int SAy = 0;

		for (y = 0; y < range; y += charBox.getHeight()) {
			stringArray[SAy] = "";
			for (x = 0; x < domain; x += charBox.getWidth()) {
				charBox.setPos(x, y);
				stringArray[SAy] += charBox.loadChar(activePalette, shadingRaster);
			}
			SAy++;
		}
		
		return stringArray;
	}
	public char[][] toCharRaster() {
		char[][] charRaster = new char[getHeight()][getWidth()];
		
		// shadingRaster position
		int SRy;
		int SRx;
		// charRaster position
		int CRy = 0;
		int CRx = 0;
		
		for (SRy = 0; SRy < range; SRy += charBox.getHeight()) {
			for (SRx = 0; SRx < domain; SRx += charBox.getWidth()) {
				charBox.setPos(SRx, SRy);
				charRaster[CRy][CRx] = charBox.loadChar(activePalette, shadingRaster);
				CRx++;
			}
			CRx = 0;
			CRy++;
		}
		
		return charRaster;
	}
	
	public void invertedShading(boolean shadingStatus) {
		isInverted = shadingStatus;
		if (isInverted)
			activePalette = reverseString(basePalette);
		else activePalette = basePalette;
	}
	
	public boolean usesDefaultPalette() {
		if (basePalette.equals(DEFAULT_PALETTE))
			return true;
		else return false;
	}
	public boolean isInverted() {
		return isInverted;
	}
	
	private static String reverseString(String orig) {
		String reversed = "";
		for (int i = (orig.length() - 1); i > 0; i--) {
			reversed += orig.charAt(i);
		}
		return reversed;
	}
	
}
