
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

import java.awt.Dimension;

import javax.swing.JPanel;

import model.FileDataManager;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ShapeUtilities;

import GUI.Chart.SingleLineLegend;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class PCAChartPanel extends JPanel{
	final static Logger logger = Logger.getLogger(PCAChartPanel.class);
	private JFreeChart jfchartPCA;
	private Double[][] dPCAMatrix;
	private ChartPanel chartPanel;
	
	public PCAChartPanel(Double[][] matrix){
		this.dPCAMatrix = matrix;
		setPreferredSize(new Dimension(1000, 760));
		setMinimumSize(new Dimension(1000, 760));
		setMaximumSize(new Dimension(1000, 760));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 200, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		try{
			buildDatachart(2,3);
		}catch (Exception e){
			logger.error("B³¹d generowania wykresu PCA", e);
		}
		chartPanel = new ChartPanel(jfchartPCA);
		GridBagConstraints gbc_chartPanel = new GridBagConstraints();
		gbc_chartPanel.insets = new Insets(0, 0, 0, 5);
		gbc_chartPanel.fill = GridBagConstraints.BOTH;
		gbc_chartPanel.gridx = 0;
		gbc_chartPanel.gridy = 0;
		add(chartPanel, gbc_chartPanel);
		chartPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel switchPanel = new JPanel();
		switchPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_switchPanel = new GridBagConstraints();
		gbc_switchPanel.fill = GridBagConstraints.BOTH;
		gbc_switchPanel.gridx = 1;
		gbc_switchPanel.gridy = 0;
		add(switchPanel, gbc_switchPanel);
		GridBagLayout gbl_switchPanel = new GridBagLayout();
		gbl_switchPanel.columnWidths = new int[]{0};
		gbl_switchPanel.rowHeights = new int[]{0};
		gbl_switchPanel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_switchPanel.rowWeights = new double[]{Double.MIN_VALUE};
		switchPanel.setLayout(gbl_switchPanel);
	}
	
	
	public void buildDatachart(int xaxispc, int yaxispc){
		String s[] = FileDataManager.getLabels();
		XYSeriesCollection dataset = new XYSeriesCollection();
		for(int i =0 ; i<dPCAMatrix[0].length ; i++){
			XYSeries series = new XYSeries(s[i]);
	        series.add(dPCAMatrix[xaxispc-1][i], dPCAMatrix[yaxispc-1][i]);
	        
	        dataset.addSeries(series);
		}        
        jfchartPCA = ChartFactory.createScatterPlot("PCA", "PC"+xaxispc, "PC"+yaxispc,
                dataset);
        XYPlot xyPlot = (XYPlot) jfchartPCA.getPlot();
        XYItemRenderer renderer = xyPlot.getRenderer(); 
        Shape rect = new Rectangle2D.Double(0, 0, 8, 8);
        renderer.setSeriesShape(0, rect);
        renderer.setSeriesPaint(0, Color.red);
        
        
        Shape elipse  = new Ellipse2D.Double(0,0,8,8);
        renderer.setSeriesShape(1, elipse);
        renderer.setSeriesPaint(1, Color.blue);
        
        Shape cross = ShapeUtilities.createDownTriangle(6);//createDiagonalCross(3, 1);
        renderer.setSeriesShape(2, cross);
        renderer.setSeriesPaint(2, Color.yellow);
        
        SingleLineLegend sll = new SingleLineLegend(xyPlot);
		sll.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
		sll.setFrame(new LineBorder());
		sll.setBackgroundPaint(Color.white);
		sll.setPosition(RectangleEdge.BOTTOM);
		jfchartPCA.removeLegend();
		jfchartPCA.addLegend(sll);
	}
	
}
