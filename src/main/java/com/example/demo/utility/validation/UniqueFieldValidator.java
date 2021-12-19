package com.example.demo.utility.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class UniqueFieldValidator implements ConstraintValidator<UniqueField, String> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String table;

	private String column;

	@Override
	public void initialize(UniqueField constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		table = constraintAnnotation.table();
		column = constraintAnnotation.column();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		var statement = """
				SELECT
				    CASE
				        WHEN COUNT(id) = 0 THEN TRUE
				        ELSE FALSE
				     END
				FROM %s
				WHERE %s = ?
				""".formatted(table, column, value);

		return jdbcTemplate.queryForObject(statement, Boolean.class, value).booleanValue();
	}

}
