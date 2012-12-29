package view;

/**
 * The interface the model must be able to handle.
 * @author capshaw
 *
 */
public interface IModelAdapter {

	/**
	 * Open a file with the given file path.
	 * @param pathToFile The path to the file.
	 */
	void openFile(String pathToFile);
	
	/**
	 * Cluster the data that is already in the system.
	 */
	void cluster();
	
	/**
	 * Set k to the specified value.
	 */
	void setK(int k);
}