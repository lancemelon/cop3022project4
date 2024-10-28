
public class StableAsset extends Asset{
	private double return_rate;

	public StableAsset(String id, String name, double return_rate) {
		super(id, name);
		this.return_rate = return_rate;
	}
	
	public StableAsset(String id, String name) {
		super(id, name);
		this.return_rate = 0;
	}

	@Override
	public int calcReturn(int investment) {
		double calc = investment * this.return_rate;
		return (int) (investment + calc);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
