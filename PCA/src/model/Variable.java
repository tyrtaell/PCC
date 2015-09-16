package model;

public class Variable {
	private Double time;
	private Double value;
	
	public Variable(Double time, Double value) {
		super();
		this.setTime(time);
		this.setValue(value);
	}

	public Variable(String string, String string2, Character c) throws NullPointerException{
		string = string.replaceAll(c.toString(), ".");
		string2 = string2.replaceAll(c.toString(), ".");
		try{
			this.setTime(new Double(string));
			this.setValue(new Double(string2));
		}catch (Exception e)
		{
			throw new NullPointerException("B³¹d parsowania liczb "+string+" "+string2);
		}
	}

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	

}
