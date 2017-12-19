package idv.shawnyang.poc.spring.jdbc;

public class Customer {

	public static final String COLUMN_ID = "id";

	public static final String COLUMN_FIRST_NAME = "first_name";
	
	public static final String COLUMN_LAST_NAME = "last_name";



	private long id;
	private String firstName;
	private String lastName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
