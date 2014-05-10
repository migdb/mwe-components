package eu.collectionspro.mwe;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowComponent;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.emf.mwe2.runtime.workflow.Workflow;
import org.apache.log4j.Logger;

public class TestWorkflow extends Workflow{
	protected Logger logger;
	protected List<String> failedTests;
	
	public TestWorkflow() {
		super();
		logger = Logger.getLogger(TestWorkflow.class);
		failedTests = new ArrayList<String>();
	}
	
	public void run(IWorkflowContext context) {
		super.run(context);
		int total = 0;
		int successfull = 0;
		for (IWorkflowComponent component : getChildren()) {
			if(component instanceof TestFeature){
				total++;
				if(((TestFeature)component).isSuccesfull()){
					successfull++;
				} else{
					failedTests.add(((TestFeature)component).getTestDescription());
				}
			}
		}
		logger.info("Success rate: " + successfull + "/" + total);
		if(successfull != total){
			logger.info("Failed tests:");
			for(String testDescription : failedTests){
				logger.info("   " + testDescription);
			}
		}
	}
}
