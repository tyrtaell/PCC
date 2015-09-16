
/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 */

package GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import model.FileDataManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import GUI.Chart.SingleLineLegend;

@SuppressWarnings("serial")
public class DataChartPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	
	private ChartPanel chartPanel;

    private Crosshair xCrosshair;

    private Crosshair yCrosshair[];
    
    private int datasize ;
	
	
	public DataChartPanel() {
		setPreferredSize(new Dimension(1000, 760));
		setMinimumSize(new Dimension(1000, 760));
		setMaximumSize(new Dimension(1000, 760));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{738, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JFreeChart datachart = ChartFactory.createXYLineChart(
				"Dane pocz¹tkowe",
				"X Axis",
				"Y Axis",
				FileDataManager.getDataset());
		
		datasize = FileDataManager.getDataCount();
		chartPanel = new ChartPanel(datachart);
		SingleLineLegend sll = new SingleLineLegend(datachart.getPlot());
		sll.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
		sll.setFrame(new LineBorder());
		sll.setBackgroundPaint(Color.white);
		sll.setPosition(RectangleEdge.BOTTOM);
		datachart.removeLegend();
		datachart.addLegend(sll);
		yCrosshair = new Crosshair[datasize];
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);
        for(int i = 0 ; i< datasize; i++)
        {
        	this.yCrosshair[i] = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        this.yCrosshair[i].setLabelVisible(true);
	        crosshairOverlay.addRangeCrosshair(yCrosshair[i]);
        }
	        
        
        
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        
        chartPanel.addOverlay(crosshairOverlay);
        
		chartPanel.addChartMouseListener(new ChartMouseListener() {
			
			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
				Rectangle2D dataArea = chartPanel.getScreenDataArea();
		        JFreeChart chart = arg0.getChart();
		        XYPlot plot = (XYPlot) chart.getPlot();
		        ValueAxis xAxis = plot.getDomainAxis();
		        double x = xAxis.java2DToValue(arg0.getTrigger().getX(), dataArea, 
		                RectangleEdge.BOTTOM);
		        double y = 0;	        
		        xCrosshair.setValue(x);
		        for(int i = 0 ; i <datasize ; i++)
		        {
			        y = DatasetUtilities.findYValue(plot.getDataset(), i, x);
			        yCrosshair[i].setValue(y);
		        }
				
			}
			
			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});	
		
		chartPanel.setPreferredSize( new java.awt.Dimension( 1000 , 720 ) );
		/*
	      final XYPlot plot = datachart.getXYPlot( );
	      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	      renderer.setSeriesPaint( 0 , Color.RED );
	      renderer.setSeriesPaint( 1 , Color.GREEN );
	      renderer.setSeriesPaint( 2 , Color.YELLOW );
	      renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
	      renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
	      renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
	      plot.setRenderer( renderer ); 
	      setContentPane( chartPanel ); 
		*/
		
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(chartPanel, gbc_lblNewLabel);

	}

}
