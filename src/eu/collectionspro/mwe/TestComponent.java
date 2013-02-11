package eu.collectionspro.mwe;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.emf.mwe.utils.Writer;
import eu.collectionspro.mwe.QVTOExecutor;

/**
 *  Testing component that should be used in MWE2 for Test Execution and possibly comparison.
 *  You must specify xmi's creating models. Input models MUST be in the order input parameters
 *  specified in tested transformation. You have to specify comparison models - their amount and 
 *  order MUST be same as output parameters of transformation. It is possible to use double quotes 
 *  if no comparison needed to skip creating expected result model.   
 *  
 * @author woxie
 *
 */
public class TestComponent extends AbstractWorkflowComponent{
	protected List<String> inputUris;
	protected List<String> comparisonUris;

	/** tested transformation URI **/
	protected String transformationFile;
	/**
	 * qvt output dir parent
	 */
	protected String outputParentUri;
	/**
	 * Optional description of test. If not specified, default value
	 *  "TEST" is used 
	 */
	protected String testDescription;
	
	protected boolean isSuccesful;
	//hidden Comparators
	protected List<ModelComparator> modelComparators;
	protected final String INPUT_PREFIX = "INPUT_";
	protected final String RESULT_PREFIX = "RESULT_";
	protected final String COMPARISON_PREFIX = "COMPARISON_";
	
	public TestComponent(){
		inputUris = new ArrayList<String>();
		comparisonUris = new ArrayList<String>();
		testDescription = "TEST";
		isSuccesful = true;
	}
	
	/** Processes QVTExecutor subtransformation that MUST have only **/
	protected void fulfillSlots(List <String> transformationList, String slotPrefix, 
			WorkflowContext ctx, ProgressMonitor monitor, Issues issues){
		for( int i = 0 ; i < transformationList.size() ; i++){
			if(!transformationList.get(i).equals("")){
				String inputUri = transformationList.get(i);
				String outputSlot = slotPrefix + i;
	
				//creating 
				QVTOExecutor inputExecutor = new QVTOExecutor();
				inputExecutor.setTransformationFile(inputUri);
				inputExecutor.addOutputSlot(outputSlot);
				inputExecutor.checkConfiguration(issues);
				inputExecutor.invoke(ctx, monitor, issues);
			}
		}
	}
	
	/**
	 *  Stores slots' contents into files, skips slots with transformation ""
	 * @param transformationList origin model transformation lists 
	 * @param slotPrefix prefix of slots to be stored
	 * @param ctx
	 * @param monitor
	 * @param issues
	 */
	protected void storeSlots(List <String> transformationList, String slotPrefix, 
			WorkflowContext ctx, ProgressMonitor monitor, Issues issues, boolean isStoringAll){
		for( int i = 0 ; i < transformationList.size() ; i++){
			if(isStoringAll || !transformationList.get(i).equals("")){
				String slot = slotPrefix + i;
				Writer slotWriter = new Writer();
				slotWriter.setUri(outputParentUri + "/" + slotPrefix + "X/" + slot + ".xmi");
				slotWriter.setCloneSlotContents(false);
				slotWriter.setUseSingleGlobalResourceSet(false);
				slotWriter.setModelSlot(slot);
				slotWriter.checkConfiguration(issues);
				slotWriter.invoke(ctx, monitor, issues);
			}
		}
	}
		
	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		QVTOExecutor testTransformationExecuter = new QVTOExecutor();
		testTransformationExecuter.setTransformationFile(transformationFile);
		fulfillSlots(inputUris, INPUT_PREFIX, ctx, monitor, issues);
		//store all models
		storeSlots(inputUris, INPUT_PREFIX, ctx, monitor, issues, true);
		
		//adding input slots into test transformation
		for( int i = 0 ; i < inputUris.size() ; i++){
			String inputSlot = INPUT_PREFIX + i;
			testTransformationExecuter.addInputSlot(inputSlot);
		}
		
		//adding output slots into test transformation
		for(int i = 0 ; i < comparisonUris.size() ; i++){
			String resultSlot = RESULT_PREFIX + i;
			testTransformationExecuter.addOutputSlot(resultSlot);
		}
		
		//check and process test
		testTransformationExecuter.checkConfiguration(issues);
		testTransformationExecuter.invoke(ctx, monitor, issues);
		
		//store ALL result slots which count equals to comparison slots count
		storeSlots(comparisonUris, RESULT_PREFIX, ctx, monitor, issues, true);
		
		//create and store comparison models
		fulfillSlots(comparisonUris, COMPARISON_PREFIX, ctx, monitor, issues);
		//store only not "" models
		storeSlots(comparisonUris, COMPARISON_PREFIX, ctx, monitor, issues, false);
				
		//make comparations between result and expected result(comparison)
		for(int i = 0 ; i < comparisonUris.size() ; i++){
			if(!comparisonUris.get(i).equals("")){
				String comparisonXMI = outputParentUri+"/" + COMPARISON_PREFIX + "X/" 
						+ COMPARISON_PREFIX + i + ".xmi";
				String resultXMI = outputParentUri+"/" + RESULT_PREFIX + "X/" 
						+ RESULT_PREFIX + i + ".xmi";
				ModelComparator comparator = new ModelComparator();
				comparator.setTestDescription(testDescription + i);
				comparator.setTestedURI(resultXMI);
				comparator.setResultURI(comparisonXMI);
				comparator.invoke(ctx, monitor, issues);
				isSuccesful = isSuccesful && comparator.getSuccess();
			}
		}

		System.out.println("Testing " + testDescription + " finisthed\n");
	}

	/** 
	 * Adds input uri to be used by test transformation.
	 */
	public void addQvtInput(final String inputUri){
		this.inputUris.add(inputUri);
	}
	
	/** 
	 * Adds expected result uri to be compared after test transformation is performed. Order(types) 
	 * and amount must equal to transformation outputs. Uri "" can be used when no comparison needed
	 * @param comparisonUri
	 */
	public void addQvtComparison(final String comparisonUri){
		this.comparisonUris.add(comparisonUri);
	}
	
	/**
	 *  Sets location where output xmi files should  be stored into
	 * @param uri
	 */
	public void setOutputParentUri(String uri){
		outputParentUri = uri;
	}
	
	/**
	 * @param description
	 */
	public void setTestDescription(String description){
		testDescription = description;
	}
	
	/** sets uri of tested transformation **/
	public void setTransformationFile(String transformationUri){
		transformationFile = transformationUri;
	}

	
	@Override
	/**
	 *  checks configuration of Component. Doesn't check configurations of embedded components
	 */
	public void checkConfiguration(Issues issues) {
		if(transformationFile == null || transformationFile.equals("")){
			issues.addError("Missing tested transformation URI");
		}
		if(outputParentUri == null || outputParentUri.equals("")){
			issues.addError("Missing output directory URI");
		}
	}
	
	public boolean isSuccesfull(){
		return isSuccesful;
	}
}
