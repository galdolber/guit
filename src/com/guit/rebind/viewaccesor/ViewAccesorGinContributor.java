package com.guit.rebind.viewaccesor;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.guit.client.Implementation;
import com.guit.client.binder.ViewAccesor;
import com.guit.rebind.gin.GinContext;
import com.guit.rebind.gin.GinContributor;

public class ViewAccesorGinContributor implements GinContributor {

	@Override
	public void collaborate(GinContext ginContext, TreeLogger logger, GeneratorContext context) throws UnableToCompleteException {
		JClassType baseType = context.getTypeOracle().findType(ViewAccesor.class.getCanonicalName());
		JClassType[] subtypes = baseType.getSubtypes();
		
		for (JClassType jClassType : subtypes) {
			if (jClassType.isInterface()!=null && !jClassType.isAnnotationPresent(Implementation.class)){
				ginContext.addProvidedType(jClassType.getQualifiedSourceName());
			}
		}
	}

}
