
public class Budget {
	private double targetMax;
	private double currentTotal;
	
	
	public Budget(double targetMax) {
		this.targetMax = targetMax;
	}
	
	
	
	public double getTargetMax() {
		return targetMax;
	}
	public void setTargetMax(double targetMax) {
		this.targetMax = targetMax;
	}
	public double getCurrentTotal() {
		return currentTotal;
	}
	public void setCurrentTotal(double currentTotal) {
		this.currentTotal = currentTotal;
	}

}
