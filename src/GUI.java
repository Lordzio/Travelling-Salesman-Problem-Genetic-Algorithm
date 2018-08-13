import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.Toolkit;

import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tournamentTextField = new JTextField();
	private JTextField populationTextField = new JTextField();
	private JTextField iterationTextField = new JTextField();
	private JTextField mutationTextField = new JTextField();
	private JTextField cloneTextField = new JTextField();
	private JTextField eliteRateTextField = new JTextField();
	private JTextField eliteCountTextField = new JTextField();
	public static JTextArea outputArea = new JTextArea();
	private Population pop = new Population(GeneticManager.numCities);
	private Timer timer = new Timer();
	private Individual bestInd = new Individual();
	private double bestCost;
	/**
	 * Create the frame.
	 */
	public GUI() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 686, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel valuesPanel = new JPanel();
		contentPane.add(valuesPanel, "name_295280491380117");
		valuesPanel.setLayout(null);
		
		tournamentTextField.setText("100");
		
		JSlider tournamentSlider = new JSlider();
		tournamentSlider.setMinimum(1);
		tournamentSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				tournamentTextField.setText(Integer.toString(tournamentSlider.getValue()));
				GeneticManager.TOURNAMENT_SIZE_PCT = (double) tournamentSlider.getValue() / (double)100;
				GeneticManager.TOURNAMENT_SIZE = (int) (GeneticManager.POPULATION_SIZE*GeneticManager.TOURNAMENT_SIZE_PCT); 
			}
		});
		tournamentSlider.setBounds(38, 50, 200, 26);
		valuesPanel.add(tournamentSlider);
		tournamentSlider.setValue(Integer.parseInt(tournamentTextField.getText()));
		
		
		JPanel outputPanel = new JPanel();
		contentPane.add(outputPanel, "name_295282073560232");
		outputPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 650, 451);
		outputPanel.add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	
		scrollPane.setViewportView(outputArea);
		
		JButton btnRestart = new JButton("Restart");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					outputArea.setText("");
					dispose();
					GeneticManager.main(null);
			}
		});
		btnRestart.setBounds(286, 473, 89, 37);
		outputPanel.add(btnRestart);
		
		JLabel lblTournament = new JLabel("Tournament size (%)");
		lblTournament.setHorizontalAlignment(SwingConstants.CENTER);
		lblTournament.setBounds(38, 25, 200, 14);
		valuesPanel.add(lblTournament);
		

		tournamentTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (Integer.parseInt(tournamentTextField.getText()) > 100) tournamentTextField.setText("100");
				if (Integer.parseInt(tournamentTextField.getText()) < 1) tournamentTextField.setText("1");
				tournamentSlider.setValue(Integer.parseInt(tournamentTextField.getText()));
				GeneticManager.TOURNAMENT_SIZE_PCT = (double) tournamentSlider.getValue() / (double)100;
				GeneticManager.TOURNAMENT_SIZE = (int) (GeneticManager.POPULATION_SIZE*GeneticManager.TOURNAMENT_SIZE_PCT); 
			}
		});
		tournamentTextField.setHorizontalAlignment(SwingConstants.CENTER);
		tournamentTextField.setBounds(95, 87, 86, 20);
		valuesPanel.add(tournamentTextField);
		tournamentTextField.setColumns(10);

		
		
		populationTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				GeneticManager.POPULATION_SIZE = Integer.parseInt(populationTextField.getText());
				GeneticManager.TOURNAMENT_SIZE = (int) (GeneticManager.POPULATION_SIZE*GeneticManager.TOURNAMENT_SIZE_PCT); 
			}
		});
		populationTextField.setText("1000");
		
		populationTextField.setHorizontalAlignment(SwingConstants.CENTER);
		populationTextField.setBounds(414, 127, 129, 20);
		valuesPanel.add(populationTextField);
		populationTextField.setColumns(10);
		iterationTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				GeneticManager.NUM_EVOLUTION_ITERATIONS = Integer.parseInt(iterationTextField.getText());
			}
		});
		iterationTextField.setText("1000");
		
		iterationTextField.setHorizontalAlignment(SwingConstants.CENTER);
		iterationTextField.setBounds(414, 62, 129, 20);
		valuesPanel.add(iterationTextField);
		iterationTextField.setColumns(10);
		
		JLabel lblPopulationSize = new JLabel("Population size");
		lblPopulationSize.setHorizontalAlignment(SwingConstants.CENTER);
		lblPopulationSize.setBounds(414, 102, 129, 14);
		valuesPanel.add(lblPopulationSize);
		
		JLabel lblIterations = new JLabel("Iterations");
		lblIterations.setHorizontalAlignment(SwingConstants.CENTER);
		lblIterations.setBounds(414, 37, 129, 14);
		valuesPanel.add(lblIterations);
		
		JLabel lblMutationRate = new JLabel("Mutation rate (%)");
		lblMutationRate.setHorizontalAlignment(SwingConstants.CENTER);
		lblMutationRate.setBounds(379, 158, 200, 14);
		valuesPanel.add(lblMutationRate);
		
		JSlider mutationSlider = new JSlider();
		mutationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mutationTextField.setText(Integer.toString(mutationSlider.getValue()));
				GeneticManager.MUTATION_RATE = (double) mutationSlider.getValue() / (double)100;
			}
		});
		mutationSlider.setBounds(379, 183, 200, 26);
		valuesPanel.add(mutationSlider);
		mutationTextField.setText("50");
		mutationSlider.setValue(Integer.parseInt(mutationTextField.getText()));

		mutationTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (Integer.parseInt(mutationTextField.getText()) > 100) mutationTextField.setText("100");
				if (Integer.parseInt(mutationTextField.getText()) < 0) mutationTextField.setText("0");
				mutationSlider.setValue(Integer.parseInt(mutationTextField.getText()));
				GeneticManager.MUTATION_RATE = (double) mutationSlider.getValue() / (double)100;
			}
		});

		mutationTextField.setHorizontalAlignment(SwingConstants.CENTER);
		mutationTextField.setColumns(10);
		mutationTextField.setBounds(436, 220, 86, 20);
		valuesPanel.add(mutationTextField);
		
		JLabel lblCloneRate = new JLabel("Clone rate (%)");
		lblCloneRate.setHorizontalAlignment(SwingConstants.CENTER);
		lblCloneRate.setBounds(38, 158, 200, 14);
		valuesPanel.add(lblCloneRate);
		
		JSlider cloneSlider = new JSlider();
		cloneSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				cloneTextField.setText(Integer.toString(cloneSlider.getValue()));
				GeneticManager.CLONE_RATE = (double) cloneSlider.getValue() / (double)100;
			}
		});
		cloneSlider.setBounds(38, 183, 200, 26);
		valuesPanel.add(cloneSlider);
		cloneTextField.setText("1");
		cloneSlider.setValue(Integer.parseInt(cloneTextField.getText()));
		
		cloneTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (Integer.parseInt(cloneTextField.getText()) > 100) cloneTextField.setText("100");
				if (Integer.parseInt(cloneTextField.getText()) < 0) cloneTextField.setText("0");
				cloneSlider.setValue(Integer.parseInt(cloneTextField.getText()));
				GeneticManager.CLONE_RATE = (double) cloneSlider.getValue() / (double)100;
			}
		});

		cloneTextField.setHorizontalAlignment(SwingConstants.CENTER);
		cloneTextField.setColumns(10);
		cloneTextField.setBounds(95, 220, 86, 20);
		valuesPanel.add(cloneTextField);
		
		JLabel lblEliteParentRate = new JLabel("Elite parent rate (%)");
		lblEliteParentRate.setHorizontalAlignment(SwingConstants.CENTER);
		lblEliteParentRate.setBounds(379, 288, 200, 14);
		valuesPanel.add(lblEliteParentRate);
		
		JSlider eliteRateSlider = new JSlider();
		eliteRateSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				eliteRateTextField.setText(Integer.toString(eliteRateSlider.getValue()));
				GeneticManager.ELITE_PARENT_RATE= (double) eliteRateSlider.getValue() / (double)100;
			}
		});
		eliteRateSlider.setBounds(379, 313, 200, 26);
		valuesPanel.add(eliteRateSlider);
		eliteRateTextField.setText("50");
		eliteRateSlider.setValue(Integer.parseInt(eliteRateTextField.getText()));
		
		eliteRateTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (Integer.parseInt(eliteRateTextField.getText()) > 100) eliteRateTextField.setText("100");
				if (Integer.parseInt(eliteRateTextField.getText()) < 0) eliteRateTextField.setText("0");
				eliteRateSlider.setValue(Integer.parseInt(eliteRateTextField.getText()));
				GeneticManager.ELITE_PARENT_RATE = (double) eliteRateSlider.getValue() / (double)100;
			}
		});

		eliteRateTextField.setHorizontalAlignment(SwingConstants.CENTER);
		eliteRateTextField.setColumns(10);
		eliteRateTextField.setBounds(436, 350, 86, 20);
		valuesPanel.add(eliteRateTextField);
		
		JSlider eliteCountSlider = new JSlider();
		eliteCountSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				eliteCountTextField.setText(Integer.toString(eliteCountSlider.getValue()));
				GeneticManager.ELITE_PERCENT = (double) eliteCountSlider.getValue() / (double)100;
			}
		});
		eliteCountTextField.setText("50");
		eliteCountSlider.setValue(Integer.parseInt(eliteCountTextField.getText()));
		
		eliteCountTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (Integer.parseInt(eliteCountTextField.getText()) > 100) eliteCountTextField.setText("100");
				if (Integer.parseInt(eliteCountTextField.getText()) < 0) eliteCountTextField.setText("0");
				eliteCountSlider.setValue(Integer.parseInt(eliteCountTextField.getText()));
				GeneticManager.ELITE_PERCENT = (double) eliteCountSlider.getValue() / (double)100;
			}
		});

		eliteCountTextField.setHorizontalAlignment(SwingConstants.CENTER);
		eliteCountTextField.setColumns(10);
		eliteCountTextField.setBounds(95, 350, 86, 20);
		valuesPanel.add(eliteCountTextField);
		eliteCountSlider.setBounds(38, 313, 200, 26);
		valuesPanel.add(eliteCountSlider);
		
		JCheckBox chckbxAdaptingMutation = new JCheckBox("Adapting mutation feature");
		chckbxAdaptingMutation.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxAdaptingMutation.setBounds(379, 247, 200, 23);
		valuesPanel.add(chckbxAdaptingMutation);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GeneticManager.POPULATION_SIZE = Integer.parseInt(populationTextField.getText());
				GeneticManager.TOURNAMENT_SIZE_PCT = (double) tournamentSlider.getValue() / (double)100;
				GeneticManager.TOURNAMENT_SIZE = (int) (GeneticManager.POPULATION_SIZE*GeneticManager.TOURNAMENT_SIZE_PCT); 
				GeneticManager.NUM_EVOLUTION_ITERATIONS = Integer.parseInt(iterationTextField.getText());
				GeneticManager.MUTATION_RATE = (double) mutationSlider.getValue() / (double)100;
				GeneticManager.CLONE_RATE = (double) cloneSlider.getValue() / (double)100;
				GeneticManager.ELITE_PARENT_RATE= (double) eliteRateSlider.getValue() / (double)100;
				GeneticManager.ELITE_PERCENT = (double) eliteCountSlider.getValue() / (double)100;
				valuesPanel.setVisible(false);
				outputPanel.setVisible(true);
				SwingWorker<Void, String> worker = new SwingWorker<Void, String>(){
					@Override
					protected Void doInBackground() throws Exception{
						try {
							timer.start();
							pop.initializePopulationRandomly(GeneticManager.POPULATION_SIZE);
							bestInd = (Individual) deepClone(pop.getBestIndividualInPop());
							double previousCost = pop.getBestIndividualInPop().getCost();
							bestCost = previousCost;
//							outputArea.setText(outputArea.getText() + "Starting population: " + pop.individuals);
							for (int i = 0; i < GeneticManager.NUM_EVOLUTION_ITERATIONS; i++) {
	
								pop = pop.evolve();
//								outputArea.setText(outputArea.getText() + "\nEvolved population: " + pop.individuals);
								
								if (chckbxAdaptingMutation.isSelected()) {
									if (pop.getBestIndividualInPop().getCost() == previousCost) GeneticManager.MUTATION_RATE = GeneticManager.MUTATION_RATE + 0.01;
									else GeneticManager.MUTATION_RATE = GeneticManager.MUTATION_RATE - 0.01;
									if (GeneticManager.MUTATION_RATE > 1) GeneticManager.MUTATION_RATE = 1;
									if (GeneticManager.MUTATION_RATE < 0) GeneticManager.MUTATION_RATE = 0;
								}
								previousCost = pop.getBestIndividualInPop().getCost();
								if (previousCost < bestCost) {
									bestInd = (Individual) deepClone(pop.getBestIndividualInPop());
									bestInd.calculateCost();
									//outputArea.setText(outputArea.getText() + "\n!!!New Best Cost!!! " + bestInd + "\n");
									bestCost = bestInd.getCost();
								}
								if (i % 100 == 0) 
									outputArea.setText(outputArea.getText() + "\nFinished Iteration: " + i + ".\nBest Solution so far: " + bestInd + "\n");
								
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
					
					@Override
					protected void done() {
						timer.stop();
						outputArea.setText(outputArea.getText() + "\nBEST SOLUTION:");
						outputArea.setText(outputArea.getText() + bestInd);
						outputArea.setText(outputArea.getText() + "\nRUNNING TIME: "+ (timer.getFormattedTime()));
						final Runnable runnable =
							     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
							if (runnable != null) runnable.run();
					}
				
				};

				worker.execute();

			}
		});
		btnStart.setBounds(259, 441, 106, 38);
		valuesPanel.add(btnStart);
		
		
		JLabel lblEliteParents = new JLabel("Elite parents (%)");
		lblEliteParents.setHorizontalAlignment(SwingConstants.CENTER);
		lblEliteParents.setBounds(38, 288, 200, 14);
		valuesPanel.add(lblEliteParents);
		


		


	}
	
	private static Object deepClone(Object object) {
		   try {
		     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		     ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
		     outputStrm.writeObject(object);
		     ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		     ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
		     return objInputStream.readObject();
		   }
		   catch (Exception e) {
		     e.printStackTrace();
		     return null;
		   }
		 }
}
