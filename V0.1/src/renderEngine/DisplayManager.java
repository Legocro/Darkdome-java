package renderEngine;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

import engineTester.Paths;

public class DisplayManager {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int FPS_CAP = 60;
	private static final String gameName = "Celestial Bloodlust";
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void createDisplay(){		
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(getMaxWidth(),getMaxHeight()));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(gameName);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
			Display.sync(FPS_CAP);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0,0, getMaxWidth(),getMaxHeight());
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay(){
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds(){
		return delta;
	}
	
	public static void closeDisplay(){
		Display.destroy();
	}
	
	private static long getCurrentTime(){
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}
	
	@SuppressWarnings("unused")
	public DisplayMode getAvailableModes1By1() throws LWJGLException {
		
	    DisplayMode[] modes = Display.getAvailableDisplayModes();
		for (int i = 0; i < modes.length; i++) {
			DisplayMode current = modes[i];
			return current;
		}
		return null;
	}
	
	public DisplayMode[] getAvailableModesAll() throws LWJGLException {
		
	    DisplayMode[] modes = Display.getAvailableDisplayModes();
	    return modes;
	}

	public static void setDisplayMode(int width, int height, boolean fullScreen) {
		//The same display mode was before
		if (Display.getDisplayMode().getWidth() == width && (Display.getDisplayMode().getHeight() == height) && 
			    (Display.isFullscreen() == fullScreen)) { return; }
	
	try {
		DisplayMode targetDisplayMode = null;
		
		if (fullScreen) {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			int freq = 0;
			
			for (int i = 0; i < modes.length; i++) {
				DisplayMode current = modes[i];
				 if ((current.getWidth() == width) && (current.getHeight() == height)) {
			            if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
			                if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
			                targetDisplayMode = current;
			                freq = targetDisplayMode.getFrequency();
			                        }
			                    }
			 
			            // if we've found a match for bpp and frequence against the 
			            // original display mode then it's probably best to go for this one
			            // since it's most likely compatible with the monitor
			            if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
			                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
			                            targetDisplayMode = current;
			                            break;
			                    }
			                }
			            }
			        } else {
			            targetDisplayMode = new DisplayMode(width,height);
			        }
			 
			        if (targetDisplayMode == null) {
			            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullScreen);
			            return;
			        }
			 
			        Display.setDisplayMode(targetDisplayMode);
			        Display.setFullscreen(fullScreen);
		} catch (LWJGLException  e) { e.printStackTrace(); }
	}
	
	public static int getMaxWidth() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return gd.getDisplayMode().getWidth();
	}
	
	public static int getMaxHeight() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return gd.getDisplayMode().getHeight();
	}
	
	public static void screenShotManager() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = Display.getDisplayMode().getWidth();
		int height= Display.getDisplayMode().getHeight();
		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
		String name = "Screenshot";
		boolean exists = false;
		File f = null;
		String format = ".png"; // Example: "PNG" or "JPG"
		int counter = 0;
		while (exists) {
			f = new File(Paths.getScreenshots() + name + format); 
				if (f.exists()) {
					name = "Screenshot";
					counter++;
					name += counter;
				}
				else
					exists = false;
		}
		File file = new File(Paths.getScreenshots() + name); // The file to save to.
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		   
		for(int x = 0; x < width; x++) 
		{
		    for(int y = 0; y < height; y++)
		    {
		        int i = (x + (width * y)) * bpp;
		        int r = buffer.get(i) & 0xFF;
		        int g = buffer.get(i + 1) & 0xFF;
		        int b = buffer.get(i + 2) & 0xFF;
		        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		   
		try {
		    ImageIO.write(image, format, file);
		} catch (IOException e) { e.printStackTrace(); }
	}
}
		