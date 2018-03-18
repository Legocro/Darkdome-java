package fontMeshCreator;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;

public class FontCreator {

	public static Font loadFont(float size, int style, JPanel jp) {
		InputStream is = jp.getClass().getResourceAsStream("fonts/chem1.ttf");
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(size);
			font = font.deriveFont(style);
		} catch (FontFormatException e) { 
			e.printStackTrace();  
			System.exit(1);
		} catch (IOException e) { 
			e.printStackTrace();
			System.exit(1);
		}
		return font;
	} //how to use -> font = Loader.loadFont(36f, Font.BOLD, myJPanelObj); -> it loads custom fonts
}
