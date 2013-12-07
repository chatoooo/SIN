package cz.vutbr.fit.sin.stats;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import java.awt.Color;


public class ComparsionChart {
	
	List<StatsPoint<Integer>> mStatic;
	List<StatsPoint<Integer>> mInteligent;
	
	public ComparsionChart(List<StatsPoint<Integer>> staticTimig, List<StatsPoint<Integer>> intelligentTiming) {
		mStatic = staticTimig;
		mInteligent = intelligentTiming;
	}
	
	public JFreeChart createChart(String title){
		XYSeries staticSeries = new XYSeries("Static");
		XYSeries intelligentSeries = new XYSeries("Intelligent");
		
		for(StatsPoint<Integer> item: mStatic){
			staticSeries.add(item.time, item.value);
		}
		
		for(StatsPoint<Integer> item: mInteligent){
			intelligentSeries.add(item.time, item.value);
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		dataset.addSeries(intelligentSeries);
		dataset.addSeries(staticSeries);
		
		final JFreeChart chart = ChartFactory.createXYLineChart(
	            title,      // chart title
	            "Time[s]",                      // x axis label
	            "Number of Cars",                      // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
		chart.setBackgroundPaint(Color.white);
		
		final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesPaint(0, new Color(0, 128, 0));
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
	}
}
