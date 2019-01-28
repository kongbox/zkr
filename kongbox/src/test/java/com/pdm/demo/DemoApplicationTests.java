package com.pdm.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {


	private JdbcTemplate jdbcTemplate;
	@Autowired
	ApplicationContext applicationContext;

	private DataSource myDataSource;

	@Test
	public void contextLoads() {
		jdbcTemplate =new  JdbcTemplate(myDataSource);
		jdbcTemplate.queryForRowSet("");
	}

}

