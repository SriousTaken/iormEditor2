package org.framed.iorm.model.editor.subeditors;

import org.eclipse.graphiti.ui.editor.DiagramEditor;

public class DiagramEditorWithID extends DiagramEditor  {

	private String id;
	
	public DiagramEditorWithID(String id) {
		super();
		this.id = id;
	}
	
	
	public String getId() {
		return id;
	}
	
}
