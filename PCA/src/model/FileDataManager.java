package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class FileDataManager {
	
	/** Lista wartoœci próbki pobrana z pliku */
	private List<Variable> lValueList;	
	/** Lista próbek */
	private static List<FileDataManager>extent = new ArrayList<FileDataManager>();
	/** Nazwa próbki */
	private String sName;	
	
	public FileDataManager(String sName) {
		super();
		this.setsName(sName);
		this.lValueList = new ArrayList<Variable>();
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}
	
	public static void addFileDataManager(FileDataManager fdm){
		if(!extent.contains(fdm))
			extent.add(fdm);
	}
	
	public void addVariable(Variable var){
		this.lValueList.add(var);
	}
	
	private static boolean checkDeltaT()
	{		
		Double[] aDeltaX = new Double[extent.size()];
		int i = 0;
		for(FileDataManager f:extent){
			aDeltaX[i] = f.getDeltaX();
			i++;
		}
		for(i = 1; i<aDeltaX.length ; i++)
			if(!aDeltaX[i-1].equals(aDeltaX[i]))
				return false;
		return true;
	}
	
	private static boolean checkDataSpectrum()
	{
		Double[][] aDataSpectrum = new Double[extent.size()][2];
		int i = 0;
		for(FileDataManager f:extent){
			aDataSpectrum[i] = f.getDataSpectrum();
			i++;
		}
		for(i = 1; i<aDataSpectrum.length ; i++)
			if(!aDataSpectrum[i-1][0].equals(aDataSpectrum[i][0]) || !aDataSpectrum[i-1][1].equals(aDataSpectrum[i][1]))
				return false;
		return true;
	}
	
	private Double[] getDataSpectrum() {
		Double[] aSpectrum = {	lValueList.get(0).getTime(),
								lValueList.get(lValueList.size()-1).getTime()
							};
		
		return aSpectrum;
	}
	/**
	 * Przeliczanie œredniej czasu i wartoœci 
	 * @return 
	 */
	public List<Double> calculateMeans(){
		List<Double> lMean = new ArrayList<Double>();
		BigDecimal gdMeanTime = new BigDecimal(0);
		BigDecimal gdMeanValue = new BigDecimal(0);
		for(Variable v:lValueList){
			gdMeanTime = gdMeanTime.add(new BigDecimal(v.getTime()));
			gdMeanValue = gdMeanValue.add(new BigDecimal(v.getValue()));
		}
		gdMeanTime = gdMeanTime.divide(new BigDecimal(lValueList.size()));
		gdMeanValue = gdMeanValue.divide(new BigDecimal(lValueList.size()));
		lMean.add(gdMeanTime.doubleValue());
		lMean.add(gdMeanValue.doubleValue());
		return lMean;
		
	}
	/**
	 * Przeliczanie œrednich próbek
	 * @return
	 */
	public static List<Double> przeliczSrednie(){
		List<Double> lMean = new ArrayList<Double>();
		FileDataManager fTemp = extent.get(0);
		for(int i = 0 ; i < fTemp.lValueList.size(); i++){
			BigDecimal mean = new BigDecimal(0);
			for(FileDataManager fdm:extent){			
				mean = mean.add(new BigDecimal(fdm.lValueList.get(i).getValue()));
			}
			mean = mean.divide(new BigDecimal(extent.size()),4,RoundingMode.HALF_EVEN);
			lMean.add(mean.doubleValue());
		}		
		return lMean;
	}
	
	public static Double[][] getData(){
		Double[][] dArr = new Double[extent.get(0).lValueList.size()][3];
		for(int i = 0 ; i<extent.get(0).lValueList.size(); i++){
			//FileDataManager fdm = extent.get(i);
			for(int j = 0 ; j<extent.size(); j++)
				dArr[i][j] = extent.get(j).lValueList.get(i).getValue();
		}
		return dArr;
	}

	private Double getDeltaX(){		
		BigDecimal bg = new BigDecimal(lValueList.get(0).getTime()-lValueList.get(1).getTime());
		bg = bg.setScale(0, RoundingMode.HALF_EVEN);		
		return bg.doubleValue();		
	}

	public static void clear() {
		extent.clear();
		
	}

	public static void checkValues() throws Exception{
		if(!checkDeltaT() || !checkDataSpectrum())
			throw new Exception(" Given data doesn't have right parameters");
	}

	public static XYDataset getDataset() {
		final XYSeriesCollection dataset = new XYSeriesCollection( ); 
		for(FileDataManager f :extent)
		{
			final XYSeries dataSeries = new XYSeries( f.getsName() );
			for(Variable v: f.lValueList)
			{
				dataSeries.add(v.getTime(), v.getValue());
			}
			dataset.addSeries(dataSeries);
		}		
		return dataset;
	}
	public static int getDataCount() {
		return extent.size();
	}
	
	public static String[] getLabels(){
		String[] s = new String[extent.size()];
		int i =0;
		for(FileDataManager f:extent){
			s[i] = f.getsName();
			i++;
		}
		return s;
	}
}
