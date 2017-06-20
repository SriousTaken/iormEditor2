package org.framed.iorm.ui.literals;

import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class LayoutLiterals {

	//general
	public static final int PUFFER_BETWEEN_ELEMENTS = 3,
							SHADOW_SIZE = 5;
	public static final IColorConstant COLOR_TEXT = IColorConstant.BLACK,
			   						   COLOR_LINES = IColorConstant.BLACK,
			   						   COLOR_BACKGROUND = IColorConstant.WHITE,
			   						   COLOR_SHADOW = IColorConstant.GRAY;
		
	//wizard
	public static final int LENGHT_TEXTFIELD_WIZARD = 250;
	
	//feature editor
	public static Color COLOR_VALID_CONFIGURATION = new Color(Display.getCurrent(), 0, 0, 255),
						COLOR_INVALID_CONFIGURATION = new Color(Display.getCurrent(), 255, 0, 0);
	
	//class or roles
	public static final int HEIGHT_NAME_SHAPE = 20,
							HEIGHT_ATTRITBUTE_SHAPE = 15,
							HEIGHT_OPERATION_SHAPE = 15,
							MIN_WIDTH_FOR_CLASS_OR_ROLE = 200,
							MIN_HEIGHT_FOR_CLASS_OR_ROLE = 100;
	
	//datatype
	public static final int DATATYPE_CORNER_SIZE = HEIGHT_NAME_SHAPE;
	
	//connections
	public static final IColorConstant COLOR_CONNECTIONS = IColorConstant.BLACK;
	
	//inheritance
	public static final int INHERITANCE_ARROWHEAD_LENGTH = 15,	
							INHERITANCE_ARROWHEAD_HEIGHT = 10;
	public static final IColorConstant COLOR_INHERITANCE_ARROWHEAD = IColorConstant.WHITE;
	
}
