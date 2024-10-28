
public abstract class Asset {
	protected String id;
	protected String name;
	
	public Asset(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s)", this.name, this.id);
	}
	
	public abstract int calcReturn(int investment);	
}
