
public class Service implements Comparable<Service>{

	private final String name;
	private final String shortName;

	private final String receipt;
	private final String specialInfo;

	private Double price;
	private final int sku;


	public Service(String name, Double price){
		this.name = name;
		this.price = price;
		this.sku = 0;
		this.shortName = this.name;
		this.receipt = "";
		this.specialInfo = "";
	}

	public Service(String name, Double price, String shortName, int sku){
		this.name = name;
		this.price = price;
		this.sku = sku;
		this.shortName = shortName;
		this.receipt = "";
		this.specialInfo = "";
	}

	public Service(String name, Double price, int sku, String receipt){
		this.name = name;
		this.price = price;
		this.sku = sku;
		this.receipt = receipt.trim();
		this.shortName = name;
		this.specialInfo = "";
	}

	public Service(String name, Double price, int sku, String receipt, String specialInfo){
		this.name = name;
		this.price = price;
		this.sku = sku;
		this.receipt = receipt.trim();
		this.shortName = name;
		this.specialInfo = specialInfo;
	}


	public Double getPrice() {
		return price;
	}


	public String getName() {
		return name;
	}

	@Override
	public String toString(){
		return String.format("%s ($%.2f)", name, price);
	}


	@Override
	public int compareTo(Service o) {
		return this.getName().compareTo(o.getName());
	}

	public int getSku() {
		return sku;
	}

	public String getShortName() {
		return shortName;
	}

	public String getSpecialInfo() {
		return specialInfo;
	}

	public String getReceipt() {
		return receipt;
	}

	public String condensedPriceReport() {
		String result = "";
		result += String.format("$%.2f", this.price);
		result += " for ";
		result += this.name;
		result += " ";
		result += this.specialInfo;
		if(!this.receipt.trim().isEmpty()){
			result += "(Paid on: " + this.receipt.trim() + ")";
		}else{
			if(this.price > 0.01){
				result += "(Not Paid)";
			}
		}
		return result;
	}

	public String priceReport() {
		boolean paid = !this.receipt.trim().isEmpty();
		String result = "";
		result += String.format("$%-7.2f", this.price);
		result += " for ";
		result += this.name;
		result += " ";
		result += this.specialInfo;
		if(paid){
			result += "\n";
			result += String.format("-$%-6.2f", this.price);
			result += " paid on " + this.receipt.trim();
		}
		return result;
	}

	public boolean isPaidFor() {
		return !this.receipt.trim().isEmpty();
	}
	
	public void discount(double amount) {
		this.price = this.price - amount;
	}

}
