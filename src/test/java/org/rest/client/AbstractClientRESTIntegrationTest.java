package org.rest.client;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.rest.common.IEntity;

public abstract class AbstractClientRESTIntegrationTest< T extends IEntity >{
	
	@Rule public ExpectedException thrown = ExpectedException.none();

	public AbstractClientRESTIntegrationTest(){
		super();
	}
	
	// tests
	
	// find one
	
	@Test
	public final void givenResourceExists_whenResourceIsRetrieved_thenResourceIsCorrectlyRetrieved(){
		// Given
		final T newResource = getTemplate().createNewEntity();
		final String uriOfExistingResource = getTemplate().createAsURI( newResource );
		
		// When
		final T createdResource = getTemplate().findOneByURI( uriOfExistingResource );
		
		// Then
		assertEquals( createdResource, newResource );
	}
	
	@Test
	public final void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoResourceIsReceived(){
		// When
		final T createdResource = getTemplate().findOne( randomPositiveLong() );
		
		// Then
		assertNull( createdResource );
	}
	
	// update
	
	@Test
	public final void whenResourceIsUpdated_thenTheChangesAreCorrectlyPersisted(){
		final T existingResource = getTemplate().givenAuthenticated().create( getTemplate().createNewEntity() );
		
		// When
		getTemplate().change( existingResource );
		getTemplate().givenAuthenticated().update( existingResource );
		final T updatedResource = getTemplate().givenAuthenticated().findOne( existingResource.getId() );
		
		// Then
		assertThat( existingResource, equalTo( updatedResource ) );
	}
	
	// delete
	
	@Test
	public final void givenResourceExists_whenResourceIsDeleted_thenResourceNoLongerExists(){
		// Given
		final T existingResource = getTemplate().givenAuthenticated().create( getTemplate().createNewEntity() );
		
		// When
		getTemplate().delete( existingResource.getId() );
		
		// Then
		assertNull( getTemplate().findOne( existingResource.getId() ) );
	}
	
	// template method
	
	protected abstract AbstractClientRESTTemplate< T > getTemplate();
	protected abstract String getURI();
	
	// util
	
	private final long randomPositiveLong(){
		long id = new Random().nextLong() * 10000;
		id = ( id < 0 ) ? ( -1 * id ) : id;
		return id;
	}
	
}
