package model;

import util.NamedPoint;
import java.util.Set;

/**
 * The interface that the view must implement in some way.
 * @author capshaw
 *
 */
public interface IViewAdapter {

	/**
	 * Show the points and means with the given bounds.
	 * @param points The points in the data set
	 * @param means  The means representing the current state of the clustering
	 * @param maxX   The maximum x position the canvas must represent
	 * @param maxY   The maximum y position the canvas must represent
	 * @param minX   The minimum x position the canvas must represent
	 * @param minY   The minimum y position the canvas must represent
	 */
	void showPoints(Set<NamedPoint> points, Set<NamedPoint> means, Integer maxX, 
			Integer maxY, Integer minX, Integer minY);
	
}