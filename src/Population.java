import java.util.ArrayList;
import java.util.Random;

public class Population {
	public ArrayList<Individual> individuals = new ArrayList<Individual>();
	private int numCities;
	
	public Population(int numCities) {
		this.numCities = numCities;
	}
	
	public void initializePopulationRandomly(int numIndividuals) {
		for (int i = 0; i < numIndividuals; i++) {
			Individual ind = new Individual();
			ind.generateRandomTour(numCities);
			individuals.add(ind);
		}
	}
	
	public String toString() {
		StringBuilder build = new StringBuilder();
		for (Individual ind : individuals) {
			build.append(ind.toString() + "\n");
		}
		return build.toString();
	}
	
	
	ArrayList<Individual> sorted;
	
	public Population evolve() {
		sorted = new ArrayList<Individual>();
		Population nextGenPop = new Population(numCities);
		int populationSpaceAvailable = individuals.size();
		
		for (int i = 0; i < GeneticManager.POPULATION_SIZE; i++) {
			double bestCost = Integer.MAX_VALUE;
			int bestIndex = -1;
			for (int j = 0; j < individuals.size(); j++) {
				if (individuals.get(j).getCost() < bestCost) {
					bestCost = individuals.get(j).getCost();
					bestIndex = j;
				}
			}
			sorted.add(individuals.get(bestIndex));
			individuals.remove(bestIndex);
		}
		int numElite = (int) (GeneticManager.POPULATION_SIZE * GeneticManager.ELITE_PERCENT);
		for (int i = 0; i < numElite; i++) {
			if (Math.random() < GeneticManager.MUTATION_RATE) {
				nextGenPop.individuals.add(mutate(sorted.get(i)));
				nextGenPop.individuals.get(nextGenPop.individuals.size()-1).calculateCost();
//				GUI.outputArea.setText(GUI.outputArea.getText() + "\nElite: " + nextGenPop.individuals.get(nextGenPop.individuals.size()-1));
			} else {
				nextGenPop.individuals.add(sorted.get(i));
				nextGenPop.individuals.get(nextGenPop.individuals.size()-1).calculateCost();
//				GUI.outputArea.setText(GUI.outputArea.getText() + "\nElite: " + nextGenPop.individuals.get(nextGenPop.individuals.size()-1));
			}
			populationSpaceAvailable--;
		}
		
		while (populationSpaceAvailable > 0) {
			if (populationSpaceAvailable > 1) {
				Individual p1 = selectParentViaTournament();
				Individual p2 = selectParentViaTournament();
				Individual child = crossover(p1, p2);
				Individual child2 = crossover(p1, p2);

				if (Math.random() < GeneticManager.MUTATION_RATE) {
					mutate(child);
				}
				
				if (Math.random() < GeneticManager.MUTATION_RATE) {
					mutate(child2);
				}
				
				child.calculateCost();
				child2.calculateCost();

				nextGenPop.individuals.add(child);
				populationSpaceAvailable--;
				nextGenPop.individuals.add(child2);
				populationSpaceAvailable--;
			}else {
				Individual p1 = selectParentViaTournament();
				Individual p2 = selectParentViaTournament();
				Individual child = crossover(p1, p2);

				if (Math.random() < GeneticManager.MUTATION_RATE) {
					mutate(child);
				}
				
				child.calculateCost();

				nextGenPop.individuals.add(child);
				populationSpaceAvailable--;
			}
		}
		return nextGenPop;
	}
	
	public Individual mutate(Individual ind) {
		int index1 = (int)(Math.random()*ind.tour.size());
		int index2 = (int)(Math.random()*ind.tour.size());
		if(index1 == index2) index1++;
		if(index1 >= ind.tour.size()) index1 = index2--;
		int storage = ind.getCity(index1);
		ind.tour.set(index1, ind.tour.get(index2));
		ind.tour.set(index2, storage);
		return ind;
	}
	
	public double averageFitness() {
		long sum = 0L;
		for (Individual ind : individuals) {
			sum += ind.getCost();
		}
		return (double) (sum/GeneticManager.POPULATION_SIZE);
	}
	
	public Individual crossover(Individual p1, Individual p2){
		Individual child = new Individual();
		
		try {
		int index1 = (int)(Math.random()*numCities-1);
		int index2 = (int)(Math.random()*numCities-1);
		int start = Math.min(index1, index2);
		int end = Math.max(index1, index2);

		for (int i = start; i < end; i++) {
			child.addCityToTour(p1.getCity(i));
		}

		for (int j = 0; j < numCities-1; j++) {
			if (!child.tour.contains(p2.getCity(j))) {
				child.addCityToTour(p2.getCity(j));
			}
		}

		if (Math.random() < GeneticManager.CLONE_RATE) {
			if (p1.getCost() < p2.getCost()) {
				return p1;
			}
			else {
				return p2;
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return child;
	}

	public Individual selectParentViaTournament() {
		Random rand = new Random();

		if (rand.nextDouble() < GeneticManager.ELITE_PARENT_RATE) {
			int numElite = (int) (GeneticManager.ELITE_PERCENT * GeneticManager.POPULATION_SIZE);
			if (numElite > 0) {
				return sorted.get(rand.nextInt(numElite));
			}
		}
		
		ArrayList<Individual> tournamentPopulation = new ArrayList<Individual>();
		for (int i = 0; i < GeneticManager.TOURNAMENT_SIZE; i++) {
			int randIndex = (int) (Math.random()*sorted.size());
			tournamentPopulation.add(sorted.get(randIndex));
		}
		return getBestIndividual(tournamentPopulation);
	}
	
	public Individual getBestIndividualInPop() {
		if (sorted != null) {
			return sorted.get(0);
		}
		return getBestIndividual(this.individuals);
	}
	
	public Individual getBestIndividual(ArrayList<Individual> pop) {
		double minCost = Integer.MAX_VALUE;
		int minIndex = -1;
		for (int i = 0; i < pop.size(); i++) {
			if (pop.get(i).getCost() < minCost) {
				minIndex = i;
				minCost = pop.get(i).getCost();
			}
		}
		return pop.get(minIndex);
	}
}