
public class Stock extends Asset{
	 private double return_5_year;
	 private double return_1_year;
	 private double return_90_day;
	 private boolean isMature;

	public Stock(String id, String name, double return_5_year, double return_1_year, double return_90_day) {
		super(id, name);
		this.return_5_year = return_5_year;
		this.return_1_year = return_1_year;
		this.return_90_day = return_90_day;
		this.isMature = true;
	}
	
	public Stock(String id, String name, double return_1_year, double return_90_day) {
		super(id, name);
		this.return_5_year = 0;
		this.return_1_year = return_1_year;
		this.return_90_day = return_90_day;
		this.isMature = false;
	}

	public Stock(String id, String name) {
		super(id, name);
		this.return_5_year = 0;
		this.return_1_year = 0;
		this.return_90_day = 0;
		this.isMature = false;
	}
	
	@Override
	public int calcReturn(int investment) {
	    double weightedReturn;

	    if (this.isMature) {
	        weightedReturn = (0.6 * this.return_5_year) + (0.2 * this.return_1_year) + (0.2 * this.return_90_day);
	    } else {
	        weightedReturn = (0.6 * this.return_1_year) + (0.4 * this.return_90_day);
	    }

	    return (int) (investment * (1 + weightedReturn));
	}


	
	@Override
	public String toString() {
		return super.toString();
	}
}
