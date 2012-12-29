package util;

import java.awt.Color;

/**
 * A point in 2D space with a name and a color.
 * @author capshaw
 *
 */
public class NamedPoint {
	
	/**
	 * The x position of the point.
	 */
	private int xPosition;
	
	/**
	 * The y position of the point.
	 */
	private int yPosition;
	
	/**
	 * The color of the point.
	 */
	private Color color;
	
	/**
	 * The name of the point.
	 */
	private String name;
	
	/**
	 * Create a new NamedPoint with a name, xPosition and yPosition.
	 * @param name      The name of the point.
	 * @param xPosition The x position of the point.
	 * @param yPosition The y position of the point.
	 */
	public NamedPoint(String name, int xPosition, int yPosition) {
		this.name = name;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.color = new Color(1f, 1f, 1f, 0.7f);
	}

	/**
	 * Getter for the name.
	 * @return The name of the point.
	 */
	public String getName() {
		return name;
	}
	
	/** 
	 * Getter for the x position.
	 * @return The x position of the point.
	 */
	public int getXPosition() {
		return xPosition;
	}
	
	/**
	 * Set the x position of the point.
	 * @param xPosition The x position of the point.
	 */
	public void setXPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	/**
	 * Get the y position of the point.
	 * @return The y position of the point.
	 */
	public int getYPosition() {
		return yPosition;
	}
	
	/**
	 * Set the y position of the point.
	 * @param yPosition The new y position of the point.
	 */
	public void setYPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	/**
	 * Get the color of the point.
	 * @return The point's color.
	 */
	public Color getColor() {
		return color;
	} 
	
	/**
	 * Set the color of the point
	 * @param color The color to set the point to.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Get euclidean distance between this point and another point.
	 * @param other The other point.
	 * @return The double distance between the points.
	 */
	public double distance(NamedPoint other) {
		return Math.sqrt(Math.pow(other.getXPosition() - this.getXPosition(), 2)
				+ Math.pow(other.getYPosition() - this.getYPosition(), 2));
	}
}