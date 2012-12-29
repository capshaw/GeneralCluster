package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Graphics;

import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import log.LoggingService;

import java.util.HashSet;
import java.util.Set;
import util.NamedPoint;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The GUI / client facing aspect of general cluster. 
 * @author capshaw
 *
 */
public class ClusterView extends JFrame {

	private static final long serialVersionUID = -7401512899926644544L;
	
	/**
	 * How the view talks to the model.
	 */
	private IModelAdapter _model;
	
	/**
	 * Allows the view to log details to a common log.
	 */
	private LoggingService _ls;
	
	/**
	 * The points that are being clustered.
	 */
	private Set<NamedPoint> _points;
	
	/**
	 * The current set of points that represent the k-means.
	 */
	private Set<NamedPoint> _means;
	
	/**
	 * The minimum, maximum dimensions of the view area.
	 */
	private Integer _minX, _maxX, _minY, _maxY;
	
	/**
	 * The bottom tool panel that holds buttons for the user to manipulate.
	 */
	private final JPanel _toolPanel = new JPanel();
	
	/**
	 * The main canvas area where points are drawn.
	 */
	private final JPanel _canvas = new JPanel() {

		private static final long serialVersionUID = 1026643422596867382L;

		/**
		 * Override the paint component aspect of this JPanel so that each time
		 * the panel is redrawn, the circles are drawn to the screen.
		 */
		public void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    
		    /* If there are no points, don't attempt to draw the points. */
		    if (_points.size() == 0) return;
		    
		    /* For every point and mean, draw a point. */
		    for (NamedPoint np : _points) drawPoint(np, g, 7);
		    for (NamedPoint np : _means)  drawPoint(np, g, 40);
		    
		}
		
		/**
		 * Draw a NamedPoint to the canvas.
		 * @param np The point to draw
		 * @param g  The graphics object to draw to
		 * @param d  The diameter of the point to draw
		 */
		private void drawPoint(NamedPoint np, Graphics g, int d) {
	    	g.setColor(np.getColor());
	    	
	    	/* Scale the x and y components to the minimums and maximums. */
	    	int x = (int) ((np.getXPosition() - _minX) / 
	    			(1.0 * (_maxX - _minX)) * _canvas.getWidth());
	    	int y = (int) ((np.getYPosition() - _minY) / 
	    			(1.0 * (_maxY - _minY)) * _canvas.getHeight());
	    	g.fillOval(x, y, d, d);			
		}
	};
	
	/**
	 * The file dialog that lets a user select a file.
	 */
	private final FileDialog _fileDialog = new FileDialog(this, 
			"Please choose a .2d file to load");
	
	/**
	 * Button user clicks when they want to import a new file.
	 */
	private final JButton _btnImportFile = new JButton("Import File");
	
	/**
	 * The text field that holds the name of the file path a user selected.
	 */
	private final JTextField _filePath = new JTextField();
	
	/**
	 * The button a user clicks to cluster the loaded data.
	 */
	private final JButton _btnFindClusters = new JButton("Find Clusters");
	
	/** 
	 * A simple label for the slider.
	 */
	private final JLabel _lblKmeans = new JLabel("k-means:");
	
	/**
	 * A slider to choose the k of k-means.
	 */
	private final JSlider _slider = new JSlider();
	
	/**
	 * The client-facing view for general cluster.
	 * @param model The adapter for the model.
	 * @param ls The logging service to print system information.
	 */
	public ClusterView(IModelAdapter model, LoggingService ls) {
		this._model = model;
		this._ls = ls;
		this._points = new HashSet<NamedPoint>();
		this._means  = new HashSet<NamedPoint>();
		initGUI();
	}
	
	/**
	 * Initialize the user's interface to the program.
	 */
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,1400,800);
		setTitle("General Cluster");
		
		getContentPane().add(_toolPanel, BorderLayout.SOUTH);
		_toolPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		_toolPanel.add(_filePath);
		_slider.setSnapToTicks(true);
		
		_slider.setMinimum(1);
		_slider.setMaximum(10);
		_slider.setValue(3);
		_slider.setMajorTickSpacing(1);
		_slider.setPaintTicks(true);
		
		_filePath.setEditable(false);
		_filePath.setEnabled(false);
		_filePath.setColumns(20);		
		_toolPanel.add(_btnImportFile);
		_btnImportFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importFile();
				_btnFindClusters.setEnabled(true);
			}
		});
		_toolPanel.add(_btnFindClusters);
		_btnFindClusters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_model.cluster();
			}
		});
		
		_btnFindClusters.setEnabled(false);
		
		_toolPanel.add(_lblKmeans);
		
		_toolPanel.add(_slider);
		
		_slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				_model.setK(_slider.getValue());
			}
		});
		_canvas.setBackground(Color.DARK_GRAY);
		_canvas.setLayout(new BorderLayout(0, 0));
		getContentPane().add(_canvas, BorderLayout.CENTER);
	}
	
	/**
	 * Start the view, making it visible.
	 */
	public void start() {
		_ls.log("Initializing the view and making it visible");
		setVisible(true);
	}
	
	/**
	 * Handler for when a user tells the program to open a new file.
	 */
	private void importFile() {
		_fileDialog.setVisible(true);
		
		/* If the user actually selected a file, process that request. */
		if (_fileDialog.getDirectory() != null && _fileDialog.getFile() != null) {
			String filePath = _fileDialog.getDirectory() + _fileDialog.getFile();
			_ls.log("The user wanted to open " + filePath);
			setFilepath(filePath);
			_model.openFile(filePath);
		}else{
			_ls.log("The user didn't quite choose a file");
		}
	}
	
	/**
	 * Show the given points on the canvas.
	 * @param points The set of points
	 * @param means  The set of means
	 * @param maxX   The maximum x to scale to.
	 * @param maxY   The maximum y to scale to. 
	 * @param minX   The minimum x to scale to.
	 * @param minY   The minimum y to scale to.
	 */
	public void showPoints(Set<NamedPoint> points, Set<NamedPoint> means, Integer maxX, 
			Integer maxY, Integer minX, Integer minY) {
		this._points = points;
		this._means = means;
		this._maxY = maxY;
		this._maxX = maxX;
		this._minY = minY;
		this._minX = minX;
		_canvas.repaint();
	}
	
	/**
	 * Set the file path text to the given text.
	 * @param text The string to set the text to. 
	 */
	private void setFilepath(String text) {
		_filePath.setText(text);
	}	
}