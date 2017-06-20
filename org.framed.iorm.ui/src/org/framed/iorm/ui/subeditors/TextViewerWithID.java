package org.framed.iorm.ui.subeditors;

import org.eclipse.ui.editors.text.TextEditor;

public class TextViewerWithID extends TextEditor {
	
	private String id;
	
	public TextViewerWithID(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public boolean isEditable() {
	    return false;
	}

	@Override
	public boolean isEditorInputModifiable() {
	    return false;
	}

	@Override
	public boolean isEditorInputReadOnly() {
	    return true;
	}

	@Override
	public boolean isDirty() {
	    return false;
	}
}