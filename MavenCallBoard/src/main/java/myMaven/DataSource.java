package myMaven;

public abstract class DataSource {
	
	public abstract void readFromFile(String fileName);
	public abstract void writeToFile(String fileName);

}
