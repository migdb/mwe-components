package eu.collectionspro.mwe;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.emf.mwe.utils.SingleGlobalResourceSet;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

public class UML2StandaloneSetup extends AbstractWorkflowComponent {

	String umlExtURIRoot = null;

	ResourceSet resourceSet = SingleGlobalResourceSet.get();

	public void setUmlExtURIRoot(String umlExtURIRoot) {
		this.umlExtURIRoot = umlExtURIRoot;
	}

	public void setResourceSet(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor, Issues issues) {
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		Map uriMap = resourceSet.getURIConverter().getURIMap();
		URI uri = URI.createURI(umlExtURIRoot);
		// should work??
		// .createURI("jar:classpath:org.eclipse.uml2.uml.resources_3.1.1.v201008191505.jar!/");
		uriMap.put(URI.createURI(org.eclipse.uml2.uml.resource.UMLResource.LIBRARIES_PATHMAP), uri
				.appendSegment("libraries").appendSegment(""));
		uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri.appendSegment("metamodels")
				.appendSegment(""));
		uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri.appendSegment("profiles")
				.appendSegment(""));

	}

	@Override
	public void checkConfiguration(Issues issues) {
		if (umlExtURIRoot == null || umlExtURIRoot.isEmpty())
			issues.addError("umlExtURIRoot not set");
		if (resourceSet == null)
			issues.addError("resourceSet is null");
	}
}
