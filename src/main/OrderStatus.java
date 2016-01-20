package main;

public enum OrderStatus {
	
	INIT("Otrzymano"), ACCEPTED("Potwierdzono"), SENT("Wyslano"), REJECTED("Odrzucono");
	

	private final String label;


	OrderStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
