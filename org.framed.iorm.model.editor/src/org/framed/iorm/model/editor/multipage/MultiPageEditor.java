package org.framed.iorm.model.editor.multipage;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.framed.iorm.model.editor.literals.IdentifierLiterals;
import org.framed.iorm.model.editor.literals.NameLiterals;
import org.framed.iorm.model.editor.subeditors.DiagramEditorWithID;
import org.framed.iorm.model.editor.subeditors.TextViewerWithID;

public class MultiPageEditor extends FormEditor implements IResourceChangeListener, ISelectionListener {
	
	//name literals
	private final String BEHAVIOR_PAGE_NAME = NameLiterals.BEHAVIOR_PAGE_NAME,
						 DATA_PAGE_NAME = NameLiterals.DATA_PAGE_NAME,
						 TEXT_IORM_PAGE_NAME = NameLiterals.TEXT_IORM_PAGE_NAME,
						 TEXT_CROM_PAGE_NAME = NameLiterals.TEXT_CROM_PAGE_NAME,
						 FEATURE_PAGE_NAME = NameLiterals.FEATURE_PAGE_NAME,
						 MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	//id literals
	private final String  PAGE_ID_BEHAVIOR = IdentifierLiterals.PAGE_ID_BEHAVIOR,
			   			  PAGE_ID_DATA = IdentifierLiterals.PAGE_ID_DATA,
			   			  PAGE_ID_IORM_TEXT = IdentifierLiterals.PAGE_ID_IORM_TEXT,
			   			  PAGE_ID_CROM_TEXT = IdentifierLiterals.PAGE_ID_CROM_TEXT,
			   			  PAGE_ID_FEATURE = IdentifierLiterals.PAGE_ID_FEATURE;
	
	//editors
	private DiagramEditorWithID editorBehaviorDiagram,
								editorDataDiagram;
	private TextViewerWithID textViewerIORM,
							 textViewerCROM;
	
	//editors indexes
	private int editorBehaviorDiagramIndex,
				editorDataDiagramIndex,
				textViewerIORMIndex,
				textViewerCROMIndex;

	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
		
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	
	@Override
	protected void addPages() {
		//create editors
		editorBehaviorDiagram = new DiagramEditorWithID(PAGE_ID_BEHAVIOR);
		editorDataDiagram = new DiagramEditorWithID(PAGE_ID_DATA);
		textViewerIORM = new TextViewerWithID(PAGE_ID_IORM_TEXT);
		textViewerCROM = new TextViewerWithID(PAGE_ID_CROM_TEXT);
		
		//add pages
		try { 
			editorBehaviorDiagramIndex = addPage(editorBehaviorDiagram, getEditorInput());
			editorDataDiagramIndex = addPage(editorDataDiagram, getEditorInput());
			textViewerIORMIndex = addPage(textViewerIORM, getEditorInput());
			textViewerCROMIndex = addPage(textViewerCROM, getEditorInput());
		} catch (PartInitException e) { e.printStackTrace(); }
		//create model
		ICreateFeature createModelFeature = null;
		ICreateFeature[] createFeatures = editorBehaviorDiagram.getDiagramTypeProvider().getFeatureProvider().getCreateFeatures();
		for(int i = 0; i<createFeatures.length; i++) {
			if(createFeatures[i].getCreateName().equals(MODEL_FEATURE_NAME)) 
				createModelFeature = createFeatures[i];
		}
		ICreateContext createModelFeatureContext = new CreateContext();
		createModelFeature.create(createModelFeatureContext);	
		//set page names
		setPageText(editorBehaviorDiagramIndex, BEHAVIOR_PAGE_NAME);
		setPageText(editorDataDiagramIndex, DATA_PAGE_NAME);
		setPageText(textViewerIORMIndex, TEXT_IORM_PAGE_NAME);
		setPageText(textViewerCROMIndex, TEXT_CROM_PAGE_NAME);	
	}
	
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	public void doSave(IProgressMonitor monitor) {
		getEditor(editorBehaviorDiagramIndex).doSave(monitor);
	}
		
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
	}
		
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
						for (int i = 0; i<pages.length; i++){
							if(((FileEditorInput)editorBehaviorDiagram.getEditorInput()).getFile().getProject().equals(event.getResource())){
								IEditorPart editorPart = pages[i].findEditor(editorBehaviorDiagram.getEditorInput());
								pages[i].closeEditor(editorPart,true);
				}	}	}	}	
			);	
	}	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// Propagate the selection changed event to all sub editors
		int pageCount = getPageCount();
		for (int i = 0; i < pageCount; i++) {
			IEditorPart editor = getEditor(i);
			if (editor instanceof ISelectionListener) {
				((ISelectionListener) editor).selectionChanged(part, selection);
			}
		}
	}
	
}
