package util.gui;

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
	
}
