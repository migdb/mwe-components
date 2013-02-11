package eu.collectionspro.mwe;

import org.apache.log4j.Logger;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowComponent;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.emf.mwe2.runtime.workflow.Workflow;
import org.apache.log4j.Logger;

public class TestWorkflow extends Workflow{
	protected Logger logger;
	
	public TestWorkflow() {
		super();
		logger = Logger.getLogger(TestWorkflow.class);
	}
	
	public void run(IWorkflowContext context) {
		super.run(context);
		int total = 0;
		int successfull = 0;
		for (IWorkflowComponent component : getChildren()) {
			if(component.getClass() == TestComponent.class){
					total++;
					if(((TestComponent)component).isSuccesfull()){
					successfull++;
				}
			}
		}
		logger.info("Success rate: " + successfull + "/" + total);
	}
}
