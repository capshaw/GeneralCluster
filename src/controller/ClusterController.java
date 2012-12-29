package controller;

import java.util.Set;

import log.*;
import util.NamedPoint;
import view.ClusterView;
import view.IModelAdapter;
import model.ClusterModel;
import model.IViewAdapter;

/**
 * The controller for the clustering program.
 * @author capshaw
 *
 */
public class ClusterController {
	
	/**
	 * The cluster model.
	 */
	private ClusterModel model;
	
	/**
	 * The cluster view.
	 */
	private ClusterView view;
	
	/**
	 * The shared logging service throughout the MVC structure.
	 */
	private LoggingService ls;
	
	/**
	 * Create a new cluster controller.
	 * @param args Unused arguments.
	 */
	public static void main(String[] args) {
		new ClusterController();
	}
	
	/**
	 * Instantiate a controller for the general cluster system.
	 */
	public ClusterController() {
		
		/* Create a new logging service for the model and the view. */
		ls = new LoggingService();
		ls.log("General Cluster at your service.");
		
		/* Create a model to handle the logic of the clustering. */
		model = new ClusterModel(new IViewAdapter() {

			@Override
			public void showPoints(Set<NamedPoint> points, 
					Set<NamedPoint> means, Integer maxX, Integer maxY, 
					Integer minX, Integer minY) {
				view.showPoints(points, means, maxX, maxY, minX, minY);
			}
			
		}, ls);
	
		/* Create a view and an adapter to the model to handle rendering. */
		view = new ClusterView(new IModelAdapter() {

			@Override
			public void openFile(String pathToFile) {
				model.openFile(pathToFile);
			}

			@Override
			public void cluster() {
				model.cluster();
			}

			@Override
			public void setK(int k) {
				model.setK(k);
			}
			
		}, ls);
		
		/* Start the model and the view. */
		model.start();
		view.start();
	}
}