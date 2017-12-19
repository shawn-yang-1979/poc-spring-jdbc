package idv.shawnyang.poc.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Tester {

	private static final Logger log = LoggerFactory.getLogger(Tester.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	void init() {
		// create table
		String dropSql = "DROP TABLE IF EXISTS customer";
		String createSql = "CREATE TABLE customer(id INTEGER PRIMARY KEY AUTOINCREMENT, first_name VARCHAR(255), last_name VARCHAR(255))";
		jdbcTemplate.execute(dropSql);
		jdbcTemplate.execute(createSql);

		Customer shawn = new Customer();
		shawn.setFirstName("Shawn");
		shawn.setLastName("Yang");

		Customer sean = new Customer();
		sean.setFirstName("Sean");
		sean.setLastName("Yang");

		// Create
		createCustomer(shawn);
		createCustomer(sean);

		// Read
		for (Customer customer : findAllcustomer()) {
			String printCustomer = customer.getId() + ": " + customer.getFirstName() + " " + customer.getLastName();
			log.info(printCustomer);
		}

		// Update
		List<Customer> shawns = findcustomerByFirstName("Shawn");
		for (Customer c : shawns) {
			c.setFirstName("Shang-Hua");
			updateCustomer(c);
		}
		for (Customer customer : findAllcustomer()) {
			String printCustomer = customer.getId() + ": " + customer.getFirstName() + " " + customer.getLastName();
			log.info(printCustomer);
		}

		// Delete
		List<Customer> toBeDeleted = findcustomerByFirstName("Shang-Hua");
		for (Customer c : toBeDeleted) {
			deleteCustomer(c.getId());
		}
		for (Customer customer : findAllcustomer()) {
			String printCustomer = customer.getId() + ": " + customer.getFirstName() + " " + customer.getLastName();
			log.info(printCustomer);
		}

	}

	private List<Customer> findcustomerByFirstName(String firstName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue(Customer.COLUMN_FIRST_NAME, firstName);
		return namedParameterJdbcTemplate.query("SELECT * FROM customer WHERE first_name = :first_name", parameters,
				new CustomerRowMapper());
	}

	private List<Customer> findAllcustomer() {
		return namedParameterJdbcTemplate.query("SELECT * FROM customer", new CustomerRowMapper());
	}

	private void createCustomer(Customer customer) {
		String insertSql = "INSERT INTO customer(first_name, last_name) VALUES (:first_name, :last_name)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue(Customer.COLUMN_FIRST_NAME, customer.getFirstName());
		parameters.addValue(Customer.COLUMN_LAST_NAME, customer.getLastName());
		namedParameterJdbcTemplate.update(insertSql, parameters);
	}

	private void updateCustomer(Customer customer) {
		String updateSql = "UPDATE customer set first_name=:first_name, last_name=:last_name where id=:id";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue(Customer.COLUMN_ID, customer.getId());
		parameters.addValue(Customer.COLUMN_FIRST_NAME, customer.getFirstName());
		parameters.addValue(Customer.COLUMN_LAST_NAME, customer.getLastName());
		namedParameterJdbcTemplate.update(updateSql, parameters);
	}

	private void deleteCustomer(long id) {
		String deleteSql = "DELETE FROM customer WHERE id=:id";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue(Customer.COLUMN_ID, id);
		namedParameterJdbcTemplate.update(deleteSql, parameters);
	}

	private static class CustomerRowMapper implements RowMapper<Customer> {

		@Override
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer customer = new Customer();
			customer.setId(rs.getLong(Customer.COLUMN_ID));
			customer.setFirstName(rs.getString(Customer.COLUMN_FIRST_NAME));
			customer.setLastName(rs.getString(Customer.COLUMN_LAST_NAME));
			return customer;
		}

	}
}
