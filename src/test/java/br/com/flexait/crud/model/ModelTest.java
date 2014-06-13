package br.com.flexait.crud.model;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.flexait.cdi.integration.Db;
import br.com.flexait.cdi.integration.Jpa;

@RunWith(CdiRunner.class)
public class ModelTest {
	
	@Inject	Db db;
	@Inject ContextController ctx;
	private ModelImpl model;
	
	@Before
	public void setUp() throws Exception {
		ctx.openRequest();
		db.init(ModelImpl.class);
		model = new ModelImpl();
		model.name = "Foo";
	}

	@After
	public void tearDown() throws Exception {
		db.clean();
		db.commit();
		ctx.closeRequest();
	}
	
	@Test
	public void shouldSaveAModelImplementation() throws Exception {
		ModelImpl merged = db.em().merge(model);
		
		assertThat(merged.getId(), notNullValue());
	}
	
	@Test
	public void shouldConfigDateOnInsert() {
		ModelImpl merged = db.em().merge(model);
		
		assertThat(merged.getCreatedAt(), notNullValue());
	}
	
	@Test
	public void shouldConfigDateOnUpdate() {
		ModelImpl merged = db.em().merge(model);
		assertThat(merged.getUpdatedAt(), nullValue());
		db.commit();
		
		merged.name = "Bar";
		merged = db.em().merge(merged);
		db.commit();
		assertThat(merged.getUpdatedAt(), notNullValue());
	}

}