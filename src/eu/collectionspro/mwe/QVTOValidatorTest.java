package eu.collectionspro.mwe;

import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.WorkflowComponentWithModelSlot;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.emf.mwe.utils.Writer;

import eu.collectionspro.mwe.QVTOExecutor;

public class QVTOValidatorTest extends WorkflowComponentWithModelSlot{
	protected QVTOExecutor testCreator;
	protected QVTOExecutor testExecuter;
	protected QVTOExecutor testResult;
	protected Writer resultWriter;
	protected Writer errorLogWriter;
	protected String errorModelSlot;
 	
	public void setOutputUri(String uri){
		resultWriter.setUri(uri);
	}
	
	@Override
	public void checkConfiguration(Issues issues) {
		if(testCreator.transformation == null){
			issues.addError("CreatorTransformation URI not set");
		}
		if(testExecuter.transformation == null){
			issues.addError("ExecuterTransformation URI not set");
		}
		if(resultWriter.getUri() == null){
			issues.addError("Output URI not set");
		}
		if(errorLogWriter.getUri() == null){
			issues.addError("Error URI not set");
		}
		if(errorModelSlot == null){
			issues.addError("Error model slot not specified");
		}
		resultWriter.checkConfiguration(issues);
		errorLogWriter.checkConfiguration(issues);
	}
	
	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		testCreator.invoke(ctx, monitor, issues);
		resultWriter.invoke(ctx, monitor, issues);
		testExecuter.invoke(ctx, monitor, issues);
		if(errorLogWriter.getUri() != null){
			errorLogWriter.invoke(ctx, monitor, issues);
		}
	}

	public void setCreateTransformation(String fileName){
		testCreator.setTransformationFile(fileName);
	}
	
	public void setTestTransformation(String fileName){
		testExecuter.setTransformationFile(fileName);
	}
	
	@Override
	public void setModelSlot(final String slot){
		super.setModelSlot(slot);
		testCreator.addOutputSlot(slot);
		resultWriter.setModelSlot(slot);
		testExecuter.addInOutSlot(slot);		
	}

	public void setErrorModelSlot(final String slot){
		testExecuter.addOutputSlot(slot);
		errorLogWriter.setModelSlot(slot);
		errorModelSlot = slot;
	}
	
	public void setErrorUri(final String uri){
		errorLogWriter.setUri(uri);
	}
	
	public QVTOValidatorTest(){
		super();
		testCreator = new QVTOExecutor();
		resultWriter = new Writer();
		resultWriter.setCloneSlotContents(false);
		resultWriter.setUseSingleGlobalResourceSet(false);
		testExecuter = new QVTOExecutor();
		errorLogWriter = new Writer();
		errorLogWriter.setCloneSlotContents(false);
		errorLogWriter.setUseSingleGlobalResourceSet(false);	
	}
	
}
