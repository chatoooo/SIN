package cz.vutbr.fit.sin;


import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;

import cz.vutbr.fit.sin.stats.ComparsionChart;
import cz.vutbr.fit.sin.stats.TrafficLightsStatsTL0;

public class MainRunner {
	
	
	public static void showChart(JFreeChart chart, String title){
		final ApplicationFrame frame = new ApplicationFrame(title);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.toFront();
		frame.setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		SimulationRunner simulationRunner = new SimulationRunner(false);
		simulationRunner.runStatic();
		simulationRunner.runControlled();
		
		TrafficLightsStatsTL0 staticStats = simulationRunner.getStatsStatic();
		TrafficLightsStatsTL0 intelligentStats = simulationRunner.getStatsIntelligent();
		
		ComparsionChart chartZ = new ComparsionChart(staticStats.getZid(), intelligentStats.getZid());
		ComparsionChart chartM = new ComparsionChart(staticStats.getMal(), intelligentStats.getMal());
		ComparsionChart chartV = new ComparsionChart(staticStats.getVin(), intelligentStats.getVin());
		
		showChart(chartZ.createChart("Zid"), "Zid");
		showChart(chartM.createChart("Mal"), "Mal");
		showChart(chartV.createChart("Vin"), "Vin");
		
		
		
		
	}
}
