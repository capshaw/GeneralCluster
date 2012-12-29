package model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.NamedPoint;

import log.LoggingService;

/**
 * The model component of general cluster.
 * @author capshaw
 *
 */
public class ClusterModel {

	/**
	 * The adapter to the view.
	 */
	private IViewAdapter _view;
	
	/**
	 * The logging service to log system information.
	 */
	private LoggingService _ls;
	
	/**
	 * A set of the named points that the model has imported.
	 */
	private Set<NamedPoint> _points;
	
	/**
	 * A set of the named points that represent the means of the points.
	 */
	private Set<NamedPoint> _means;
	
	/**
	 * The number of means.
	 */
	private int _k;
	
	/**
	 * A boolean that marks whether this is the first time clustering or not
	 * with this particular data set. Reset every time the data is loaded.
	 */
	private Boolean _firstTime;
	
	/**
	 * The minimum and maximum dimensions of the canvas.
	 */
	private Integer _maxX, _maxY, _minX, _minY;
	
	/**
	 * The random element for random means.
	 */
	private Random _rand;

	/**
	 * The business logic of the clustering system.
	 * @param view The adapter to the view.
	 * @param ls The logging service to log system information.
	 */
	public ClusterModel(IViewAdapter view, LoggingService ls) {
		this._view = view;
		this._ls = ls;
		this._points = new HashSet<NamedPoint>();
		this._means = new HashSet<NamedPoint>();
		this._rand = new Random();
		this._k = 3;
	}

	/**
	 * Start the model, and begin processing necessary
	 * requests from the view.
	 */
	public void start() {
		_ls.log("Cluster model initializing.");
	}

	/**
	 * Set k to the specified value
	 * @param k The number of means.
	 */
	public void setK(int k) {
		_k = k;
		_firstTime = true;
		_ls.log("Changed k to value "+ k);
	}
	
	/**
	 * Open a file with the given path.
	 * @param pathToFile The path to the file to open.
	 */
	public void openFile(String pathToFile) {
		
		/* Reset the sets of points and means. */
		_points = new HashSet<NamedPoint>();
		_means = new HashSet<NamedPoint>();
		
		/* Attempt to read in the file given and parse it. */
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathToFile));
			String line;
			while ((line = br.readLine()) != null) 
				parseAndAddPoint(line).toString();
			br.close();
		} catch (Exception e) {
			_ls.log("Error opening the file: " + pathToFile);
		}

		_ls.log("Found "+_points.size()+" named points in the file.");
		updateView();
		
		/* The information hasn't been clustered yet. */
		_firstTime = true;
	}

	/**
	 * Tell the view to update with the current information stored here in this 
	 * model. Gives a buffer zone in the minimum and maximum values.
	 */
	private void updateView() {
		_view.showPoints(_points, _means, _maxX + 10, _maxY + 10, _minX - 10, 
				_minY - 10);
	}

	/**
	 * Parse and add a point given a line of information from a 2d file.
	 * @param line The line to parse
	 * @return True if the line was able to be parsed, false otherwise.
	 */
	public Boolean parseAndAddPoint(String line) { 
		Pattern regex = Pattern.compile("(\\w+)\\s*\\((\\d+),\\s*(\\d+)\\)");
		Matcher m = regex.matcher(line);

		/* Parse a single point from this line. */
		if (m.find())
			addNamedPoint(m.group(1), Integer.parseInt(m.group(2)), 
					Integer.parseInt(m.group(3)));

		/* Reset the minimum and maximum values to extremes. */
		_maxX = Integer.MIN_VALUE;
		_maxY = Integer.MIN_VALUE;
		_minX = Integer.MAX_VALUE;
		_minY = Integer.MAX_VALUE;

		/* Find the minimum and maximum values in the current data set. */
		for (NamedPoint np : _points) {
			if (_maxX == null || np.getXPosition() > _maxX)
				_maxX = np.getXPosition();
			if (_maxY == null || np.getYPosition() > _maxY)
				_maxY = np.getYPosition();
			if (_minX == null || np.getXPosition() < _minX)
				_minX = np.getXPosition();
			if (_minY == null || np.getYPosition() < _minY)
				_minY = np.getYPosition();		    	
		}

		return m.matches();
	}

	/**
	 * Add a named point to the list of points.
	 * @param name The name of the point
	 * @param x    The x location of the point
	 * @param y    The y location of the point
	 */
	private void addNamedPoint(String name, int x, int y) {
		_points.add(new NamedPoint(name, x, y));
	}

	/**
	 * Cluster the points. If it is the first time, generate the appropriately
	 * random k-means. Every time, attach the points to means and move the means
	 * to the average of its children. 
	 */
	public void cluster() {
		_ls.log("clustering");

		/* If it is the first time, create the k-means clustering points. */
		if (_firstTime) 
			generateInitialMeans();

		/* Reset the associations between means and points. */
		Map<NamedPoint, Set<NamedPoint>> assignments = 
				new HashMap<NamedPoint, Set<NamedPoint>>();
		for (NamedPoint mean : _means)
			assignments.put(mean, new HashSet<NamedPoint>());
		
		/* Associate every point with the nearest mean. */
		for (NamedPoint np : _points) {
			double currentDistance = Double.MAX_VALUE;
			NamedPoint meanToAssignTo = null; //TODO: could be bad if 0 means
			for (NamedPoint mean : _means) {
				if (mean.distance(np) < currentDistance) {
					np.setColor(mean.getColor());
					currentDistance = mean.distance(np);
					meanToAssignTo = mean;
				}
			}
			assignments.get(meanToAssignTo).add(np);
		}

		/* Move the mean to the center of the points assigned to it. */
		for (NamedPoint mean : assignments.keySet()) {
			int sumX = 0;
			int sumY = 0;

			for (NamedPoint np : assignments.get(mean)) {
				sumX += np.getXPosition();
				sumY += np.getYPosition();
			}

			if (assignments.get(mean).size() != 0) {
				mean.setXPosition(sumX/assignments.get(mean).size());
				mean.setYPosition(sumY/assignments.get(mean).size());
			}
		}

		// + tell the view the locations of the means and the Voronoi lines 
		// TODO: figure out how to draw Voronoi lines
		updateView();
	}

	/**
	 * Generate k initial means at random locations within the bounds of the 
	 * data. 
	 */
	private void generateInitialMeans() {
		_ls.log("generating some initial means");

		_means = new HashSet<NamedPoint>();
		
		int k = _k;
		while (k-- > 0) {
			int x = _rand.nextInt((_maxX - _minX)) + _minX;
			int y = _rand.nextInt((_maxY - _minY)) + _minY;
			NamedPoint np = new NamedPoint("mean", x, y);
			np.setColor(randomColor());
			_means.add(np);
		}

		_firstTime = false;
	}
	
	/**
	 * Returns a random color within lighter color spectrum.
	 * @return A random color
	 */
	private Color randomColor() {
		return new Color(_rand.nextFloat()*0.5f + 0.5f,
				_rand.nextFloat()*0.5f + 0.5f, _rand.nextFloat()*0.5f + 0.5f,
				.8f);
	}
}