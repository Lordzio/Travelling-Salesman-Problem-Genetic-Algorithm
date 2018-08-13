import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Individual implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3293553321440441495L;
	public ArrayList<Integer> tour; 
	private double cost; 
	
	public Individual() {
		tour = new ArrayList<Integer>();
		this.cost = 0;
	}
	
	public void generateRandomTour(int numCities) {

		Random rand = new Random();
		ArrayList<Integer> nextPossibleCity = new ArrayList<Integer>();
		for (int j = 1; j < numCities; j++) {
			nextPossibleCity.add(j);
		}
		
		
		while (!nextPossibleCity.isEmpty()) {
			int index = rand.nextInt(nextPossibleCity.size());
			tour.add(nextPossibleCity.get(index));
			nextPossibleCity.remove(index);
		}
		
		
		calculateCost();
	}

	public void calculateCost() {
		int start = 0;
			cost = 0;
			cost+= GeneticManager.distances_matrix[start][tour.get(0)];
		for (int i = 0; i < tour.size()-1; i++) {
			cost += GeneticManager.distances_matrix[tour.get(i)][tour.get(i+1)];
		}
			cost += GeneticManager.distances_matrix[tour.get(tour.size()-1)][start];
	}
	
	public void addCityToTour(int city) {
		tour.add(city);
	}
	
	public int getCity(int index) {
		return tour.get(index);
	}
	
	public void setCost(int c) {
		cost = c;
	}
	
	public double getCost() {
		return cost;
	}

	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append("(" + cost + "): ");
		build.append("0 -> ");
		for (int i : tour) {
			build.append(i + " -> ");
		}
		build.append("0");
		return build.toString();
	}
}