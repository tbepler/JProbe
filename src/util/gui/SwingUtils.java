package util.gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;

public class SwingUtils {
	
	public static void centerWindow(Window w){
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		centerWindow(w, center);
	}
	
	public static void centerWindow(Window w, Window reference){
		Point refCorner = reference.getLocation();
		Point center = new Point(refCorner.x + reference.getWidth()/2, refCorner.y + reference.getHeight()/2);
		centerWindow(w, center);
	}
	
	private static void centerWindow(Window w, Point center){
		int x = center.x - w.getWidth()/2;
		int y = center.y - w.getHeight()/2;
		w.setLocation(x, y);
	}
	
	public static boolean isPointOnScreen(Point p){
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for(GraphicsDevice device : devices){
			if(device.getDefaultConfiguration().getBounds().contains(p)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean inBounds(int x, int boundA, int boundB){
		if(boundA < boundB){
			return x >= boundA && x <= boundB;
		}
		return x <= boundA && x >= boundB;
	}
	
	public static boolean isXOnScreen(int x){
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for(GraphicsDevice device : devices){
			int boundA = device.getDefaultConfiguration().getBounds().x;
			int boundB = boundA + device.getDefaultConfiguration().getBounds().width;
			if(inBounds(x, boundA, boundB)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isYOnScreen(int y){
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for(GraphicsDevice device : devices){
			int boundA = device.getDefaultConfiguration().getBounds().y;
			int boundB = boundA + device.getDefaultConfiguration().getBounds().height;
			if(inBounds(y, boundA, boundB)){
				return true;
			}
		}
		return false;
	}
	
	
}
