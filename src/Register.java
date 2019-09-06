import java.util.ArrayList;
import java.util.Comparator;


public class Register {
	private final ArrayList<Service> services = new ArrayList<Service>();

	public void addService(Service service){
		services.add(service);
	}

	public Service getServiceAt(int index){
		return services.size() > index ? null : services.remove(index);
	}

	public Service peekAtServiceAt(int index){
		return services.size() > index ? null : services.get(index);
	}

	public Service removeService(Service serviceToRemove){
		for(int i = 0 ; i < services.size() ; i++){
			if(services.get(i) == serviceToRemove){
				return services.remove(i);
			}
		}
		return null;
	}

	public String priceReport(){
		String result = "===PRICE REPORT===";
		for(Service s : services){
			result += "\n";
			result += s.priceReport();
			result += "\n\n";
		}
		double subTotal = this.total();
		String subTotalString = String.format("$%.2f", subTotal);
		double tax = subTotal * 0.075;
		String taxString = String.format("$%.2f", tax);
		double total = subTotal + tax;
		String totalString = String.format("$%.2f", total);
		result += "_________________________________________\n";
		result += "Subtotal: " + subTotalString + " | Tax: " + taxString + " | Total: " + totalString;
		return result;
	}

	public void Reset(){
		this.services.clear();
	}

	public String condensedReport() {
		String result = "";
		services.sort(new Comparator<Service>() {

			@Override
			public int compare(Service serviceOne, Service serviceTwo) {
				return serviceOne.getName().compareTo(serviceTwo.getName());
			}
		});

		for(Service s : services){
			result += s.condensedPriceReport();
			result += " | ";
		}
		if(result.length() > 3){
			result = result.substring(0, result.length() - 3);
		}
		result += "\n";
		return result;
	}

	public Double total(){
		double result = 0;
		for(Service s : services){
			if(!s.isPaidFor()){
				result += s.getPrice();
			}
		}
		return result;
	}
	
	public int numOfServices() {
		int number = services.size();
		return number;
	}
}
