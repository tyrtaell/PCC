package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PCACalc {
	
	private Double[][] data;
	private List<Double> means;
	private Double[][] covMatrix;
	
	public PCACalc(Double[][] data, List<Double> means) {
		super();
		this.data = data;
		this.means = means;
		calculateCovMatrix();
	}
	
	private void calculateCovMatrix(){
		Double[][] A = calculateMeanDataMatrix();
		Double[][] AT = calculateTransposeMatrix(A);
		setCovMatrix(calculateCovarianceMatrix(AT,A));
	}
	
	/**
	 * 
	 * @param aT transposed matrix
	 * @param a  original matrix
	 * @return  covariance matrix 
	 */
	private Double[][] calculateCovarianceMatrix(Double[][] aT, Double[][] a) {
		Double[][] CM = new Double[aT.length][a[0].length];
		for(int i = 0 ; i< CM.length; i++)
			for(int j = 0 ; j<CM[i].length ;j++){
				CM[i][j] = 0.0;
				for(int k = 0 ;k<a.length;k++){
					CM[i][j] = ((new BigDecimal(CM[i][j]).add(new BigDecimal(aT[i][k]*a[k][j]))).setScale(4, RoundingMode.HALF_EVEN)).doubleValue();					
				}
				CM[i][j] = ((new BigDecimal((1.0/(CM.length-1))*CM[i][j])).setScale(4, RoundingMode.HALF_EVEN)).doubleValue();
				System.out.println(i+"  "+j+" "+CM[i][j]);
			}
		return CM;
	}

	private Double[][] calculateTransposeMatrix(Double[][] A) {
		Double[][] transpose = new Double[A[0].length][A.length];
		for(int i = 0; i<A[0].length; i++)
			for(int j = 0 ; j<A.length ; j++)
				transpose[i][j] = A[j][i]; 
		return transpose;
	}

	private Double[][] calculateMeanDataMatrix(){
		Double[][] newData = data.clone();
		for(int i =0 ; i<newData.length ; i++){
			BigDecimal mean = new BigDecimal(means.get(i));
			for(int j = 0 ; j<newData[i].length ; j++)
				newData[i][j] = ((new BigDecimal(newData[i][j]).subtract(mean)).setScale(4, RoundingMode.HALF_EVEN)).doubleValue();
		}		
		return newData;
	}

	public Double[][] getCovMatrix() {
		return covMatrix;
	}

	public void setCovMatrix(Double[][] covMatrix) {
		this.covMatrix = covMatrix;
	}
	
	

}
