package com.guit.client.binder.viewaccesor.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provider;
import com.guit.client.binder.viewaccesor.client.ViewAccesorPresenter.AccesorWithoutImplementation;
import com.guit.client.binder.viewaccesor.client.ViewAccesorPresenter.AccesorWithoutImplementationImpl;

public class ViewAccesorGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AccesorWithoutImplementation.class).toProvider(ViewAccesorProvider.class);
	}
	
	public static class ViewAccesorProvider implements Provider<AccesorWithoutImplementation> {

		@Override
		public AccesorWithoutImplementation get() {
			return new AccesorWithoutImplementationImpl();
		}
		
	}
}
