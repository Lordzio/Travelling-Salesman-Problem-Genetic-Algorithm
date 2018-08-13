import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class GeneticManager {
	

	public static File file;

	public static int numCities;
	

	

	public static int POPULATION_SIZE;;
	public static int NUM_EVOLUTION_ITERATIONS;


	public static double TOURNAMENT_SIZE_PCT;
	public static int TOURNAMENT_SIZE;

	public static double MUTATION_RATE;

	public static double CLONE_RATE;

	public static double ELITE_PERCENT;

	public static double ELITE_PARENT_RATE;

		
	public static double[][] distances_matrix;
	
	public static char t = '1';
	
	public static ArrayList<City> cities;
	public static void main(String[] args) {
		checkParameterErrors();

		try {
			boolean read = readDistanceMatrix();
			if (read) {
				GUI gui = new GUI();
				gui.setVisible(true);
			}
			
		} catch (IOException e) {
			System.err.println(e);
		}

	}
		
	public GeneticManager() {
		
	}
	
	
	public static boolean readDistanceMatrix() throws IOException {
		
		JFileChooser fileChooser = new JFileChooser();

		int response = fileChooser.showOpenDialog(null);
		if(response != JFileChooser.APPROVE_OPTION)
		{
			return false;
		}

		file = fileChooser.getSelectedFile();
		
		
		BufferedReader reader = null;
		try 
		{
			reader = new BufferedReader(new FileReader(file));
		}
		catch(IOException e) 
		{
			
			JOptionPane.showMessageDialog(null, "Error loading file " + e);
			System.exit(1);
		}
		
		
		int dimension = 0;
		try {
			String line;
			while(!(line = reader.readLine()).equals("NODE_COORD_SECTION") && !line.equals("EDGE_WEIGHT_SECTION")) {
				String[] entry = line.split(":");
				
				switch(entry[0].trim()) {
					case "TYPE":
						entry[1] = entry[1].trim();
						if(!entry[1].equals("TSP")) 
							if(!entry[1].equals("ATSP"))
								throw new Exception("File not in TSP/ATSP format");
							else t = '2';
						else t = '1';
						break;
					case "DIMENSION":
						dimension = Integer.parseInt(entry[1].trim());
						break;
				}
			}
		}
		catch(Exception e) {
			alert("Error parsing header " + e);
			System.exit(1);
		}
		numCities = dimension;
		switch(t) {

			case '1':
				cities = new ArrayList<City>(dimension);
					try {
						String line;
						while((line = reader.readLine()) != null && !line.equals("EOF")) {
							String[] entry = line.split(" ");
							cities.add(new City(entry[0], Double.parseDouble(entry[1]), Double.parseDouble(entry[2])));
						}
						
						distances_matrix = new double[cities.size()][cities.size()];
						for(int i = 0; i < cities.size(); i++) 
						{
							for(int j = 0; j < cities.size(); j++)
							{
								distances_matrix[i][j] = cities.get(i).distance(cities.get(j));

							}
						}
						
						reader.close();
					}
					catch(Exception e) {
						alert("Error parsing data " + e);
						System.exit(1);
					}
					break;
					
			case '2':
				distances_matrix = new double[dimension][dimension];
				try {
					String line;

					int k;
					for(int i = 0; i < dimension; i++) {
						k = 0;
						String[] entry = null;
						
						while((line = reader.readLine()) != null && !line.equals("EOF") && line.length() != 1) {
							line = line.trim().replaceAll("\t", " ").replaceAll(" +", " ");
							String[] newEntry = line.split(" ");
							if (newEntry.length != 1 && newEntry != null) {
								String[] buf;
								int len;
								if (k == 0) {
									len = newEntry.length;
									buf = new String[len];
								}
								else {
									len = newEntry.length + k - 1;
									buf = new String[len + 1];
								}
								
								
								
								for (int j = 0; j < k; j++) 
									buf[j] = entry[j];
							
								if (k == 0)
									for (int j = k; j < len; j++)
										buf[j] = newEntry[j - k];
								else 
									for (int j = k; j < len + 1; j++)
										buf[j] = newEntry[j - k];

								
								entry = buf;
								k = entry.length;
							} else break;
						}
							for(int ii = 0; ii < dimension; ii++) {
								if(i == ii) distances_matrix[i][ii] = 0.0;
								else distances_matrix[i][ii] = Double.parseDouble(entry[ii].trim());
							}
						}
					reader.close();
				}
				catch(Exception e) {
					alert("Error parsing data " + e);
					System.exit(1);
				}
				break;
		}
		return true;
			
	}
	
	public void printMatrix() {
		for (int i = 0; i < distances_matrix.length; i++) {
			for (int j = 0; j < distances_matrix[i].length; j++) {
				System.out.print(distances_matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static void checkParameterErrors() {
		boolean error = false;
		if (POPULATION_SIZE < TOURNAMENT_SIZE) {
			System.err.println("ERROR: Tournament size must be less than population size.");
			error = true;
		}
		if (TOURNAMENT_SIZE < 0) {
			System.err.println("ERROR: Tournament size must be greater than zero");
			error = true;
		}
		if (error == true) {
			System.exit(1);
		}
	}
	
	private static void alert(String message) {
		if(GraphicsEnvironment.isHeadless())
			System.out.println(message);
		else
			JOptionPane.showMessageDialog(null, message);
	}
}