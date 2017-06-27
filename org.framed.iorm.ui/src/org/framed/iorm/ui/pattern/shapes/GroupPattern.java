package org.framed.iorm.ui.pattern.shapes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.pattern.IPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.NamedElement;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.LayoutLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.TextLiterals;
import org.framed.iorm.ui.util.DirectEditingUtil;
import org.framed.iorm.ui.util.MethodUtil;
import org.framed.iorm.ui.util.PropertyUtil;

public class GroupPattern extends AbstractPattern implements IPattern {
	
	//names
	private String GROUP_FEATURE_NAME = NameLiterals.GROUP_FEATURE_NAME,
				   STANDART_GROUP_NAME = NameLiterals.STANDART_GROUP_NAME;
	
	//identifier
	private String SHAPE_ID_GROUP_TYPEBODY = IdentifierLiterals.SHAPE_ID_GROUP_TYPEBODY,
				   SHAPE_ID_GROUP_SHADOW = IdentifierLiterals.SHAPE_ID_GROUP_SHADOW,
				   SHAPE_ID_GROUP_NAME = IdentifierLiterals.SHAPE_ID_GROUP_NAME, 
				   SHAPE_ID_GROUP_LINE = IdentifierLiterals.SHAPE_ID_GROUP_LINE,
				   SHAPE_ID_GROUP_MODEL = IdentifierLiterals.SHAPE_ID_GROUP_MODEL,
				   IMG_ID_FEATURE_GROUP = IdentifierLiterals.IMG_ID_FEATURE_GROUP;
	
	//text
	private String DIRECTEDITING_GROUP = TextLiterals.DIRECTEDITING_GROUP;
	
	//layout
	private int MIN_WIDTH = LayoutLiterals.MIN_WIDTH_FOR_CLASS_OR_ROLE,
				MIN_HEIGHT = LayoutLiterals.MIN_HEIGHT_FOR_CLASS_OR_ROLE,
				HEIGHT_NAME_SHAPE = LayoutLiterals.HEIGHT_NAME_SHAPE,
				PUFFER_BETWEEN_ELEMENTS = LayoutLiterals.PUFFER_BETWEEN_ELEMENTS,
				GROUP_CORNER_RADIUS = LayoutLiterals.GROUP_CORNER_RADIUS,
				SHADOW_SIZE = LayoutLiterals.SHADOW_SIZE;
	private IColorConstant COLOR_TEXT = LayoutLiterals.COLOR_TEXT,
			   			   COLOR_LINES = LayoutLiterals.COLOR_LINES,
			   			   COLOR_BACKGROUND = LayoutLiterals.COLOR_BACKGROUND,
			   	  		   COLOR_SHADOW = LayoutLiterals.COLOR_SHADOW;
	
	public GroupPattern() {
		super(null);
	}
	
	@Override
	public String getCreateName() {
		return GROUP_FEATURE_NAME;
	}
	
	@Override
	public String getCreateImageId() {
		return IMG_ID_FEATURE_GROUP;
	}
	
	@Override
	public boolean isMainBusinessObjectApplicable(Object businessObject) {
		if(businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			if(shape.getType() == Type.GROUP) return true;
		}
		return false;
	}

	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}

	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}
	
	//services
	private static final IPeCreateService pictogramElementCreateService = Graphiti.getPeCreateService();
	private static final IGaService graphicAlgorithmService = Graphiti.getGaService();
	
	// add features
	//~~~~~~~~~~~~~
	@Override
	public boolean canAdd(IAddContext addContext) {
		System.out.println("d");
		//new Object is a group
		if(addContext.getNewObject() instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) addContext.getNewObject();
			if(shape.getType()==Type.GROUP) {
				//target container is diagram with root model
				ContainerShape containerShape = getDiagram();
				if(containerShape instanceof Diagram) {
					if(MethodUtil.getDiagramRootModel((Diagram) containerShape) != null)
						return true;
		}	}	}
		System.out.println("a");
		return false;
	}

	@Override
	public PictogramElement add(IAddContext addContext) {
		System.out.println("b");
		//get container and new object
		org.framed.iorm.model.Shape addedGroup = (org.framed.iorm.model.Shape) addContext.getNewObject();
		ContainerShape targetDiagram = getDiagram();
				
		//get width and height
		int width = addContext.getWidth(), height = addContext.getHeight();
		if(addContext.getWidth() < MIN_WIDTH) width = MIN_WIDTH;
		if(addContext.getHeight() < MIN_HEIGHT) height = MIN_HEIGHT;
				
		//container for body shape and shadow
		ContainerShape containerShape = pictogramElementCreateService.createContainerShape(targetDiagram, false);
							  
		//drop shadow
		ContainerShape dropShadowShape = pictogramElementCreateService.createContainerShape(containerShape, true);
		RoundedRectangle dropShadowRectangle = graphicAlgorithmService.createRoundedRectangle(dropShadowShape, GROUP_CORNER_RADIUS, GROUP_CORNER_RADIUS);
		dropShadowRectangle.setForeground(manageColor(COLOR_SHADOW));
		dropShadowRectangle.setBackground(manageColor(COLOR_SHADOW));
		graphicAlgorithmService.setLocationAndSize(dropShadowRectangle, addContext.getX()+SHADOW_SIZE, addContext.getY()+SHADOW_SIZE, width, height);
				
		//body shape of type
		ContainerShape typeBodyShape = pictogramElementCreateService.createContainerShape(containerShape, true);		
		RoundedRectangle typeBodyRectangle = graphicAlgorithmService.createRoundedRectangle(typeBodyShape, GROUP_CORNER_RADIUS, GROUP_CORNER_RADIUS);
		typeBodyRectangle.setForeground(manageColor(COLOR_LINES));
		typeBodyRectangle.setBackground(manageColor(COLOR_BACKGROUND));
		graphicAlgorithmService.setLocationAndSize(typeBodyRectangle, addContext.getX(), addContext.getY(), width, height);
				
		//name
		Shape nameShape = pictogramElementCreateService.createShape(typeBodyShape, false);
		Text text = graphicAlgorithmService.createText(nameShape, addedGroup.getName());	
		text.setForeground(manageColor(COLOR_TEXT));	
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);	
		graphicAlgorithmService.setLocationAndSize(text, 0, 0, width, HEIGHT_NAME_SHAPE);	
				
		//line
		Shape firstLineShape = pictogramElementCreateService.createShape(typeBodyShape, false);
		Polyline firstPolyline = graphicAlgorithmService.createPolyline(firstLineShape, new int[] {0, HEIGHT_NAME_SHAPE, width, HEIGHT_NAME_SHAPE});
		firstPolyline.setForeground(manageColor(COLOR_LINES));
				
		//model container
		ContainerShape modelContainer = pictogramElementCreateService.createContainerShape(typeBodyShape, false);
		Rectangle modelRectangle = graphicAlgorithmService.createRectangle(modelContainer);
		//modelRectangle.setLineVisible(false);
		modelRectangle.setBackground(manageColor(COLOR_BACKGROUND));
		graphicAlgorithmService.setLocationAndSize(modelRectangle, PUFFER_BETWEEN_ELEMENTS, HEIGHT_NAME_SHAPE+PUFFER_BETWEEN_ELEMENTS, 
												   width-2*PUFFER_BETWEEN_ELEMENTS, height-GROUP_CORNER_RADIUS-2*PUFFER_BETWEEN_ELEMENTS);
		
		//setProperties
		PropertyUtil.setShape_IdValue(typeBodyRectangle, SHAPE_ID_GROUP_TYPEBODY);
		PropertyUtil.setShape_IdValue(dropShadowRectangle, SHAPE_ID_GROUP_SHADOW);
		PropertyUtil.setShape_IdValue(text, SHAPE_ID_GROUP_NAME);
		PropertyUtil.setShape_IdValue(firstPolyline, SHAPE_ID_GROUP_LINE);
		PropertyUtil.setShape_IdValue(modelRectangle, SHAPE_ID_GROUP_MODEL);
		
		//set links
		link(containerShape, addedGroup);
		link(typeBodyShape, addedGroup);
		link(dropShadowShape, addedGroup);
		link(nameShape, addedGroup);
		link(firstLineShape, addedGroup);
		link(modelContainer, addedGroup);
				
		//directEditing right at creation
		IDirectEditingInfo directEditingInfo = getFeatureProvider().getDirectEditingInfo();
		directEditingInfo.setMainPictogramElement(typeBodyShape);
		directEditingInfo.setPictogramElement(nameShape);
		directEditingInfo.setGraphicsAlgorithm(text);
		//add anchors to the container
		pictogramElementCreateService.createChopboxAnchor(typeBodyShape);
		//set container as layout target
		layoutPictogramElement(containerShape);
		return containerShape;
	}
		
	//create feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canCreate(ICreateContext createContext) {
		//target container is either diagram with model
		ContainerShape containerShape = getDiagram();
		if(containerShape instanceof Diagram) {
			if(MethodUtil.getDiagramRootModel((Diagram) containerShape) != null)
				return true;
		}
		return false;
	}

	@Override
	public Object[] create(ICreateContext createContext) {
		//create new natural type
		org.framed.iorm.model.Shape newGroup = OrmFactory.eINSTANCE.createShape();
		newGroup.setType(Type.GROUP);
		newGroup.setName(STANDART_GROUP_NAME);
		//add new group to the elements of the model
		Model model = MethodUtil.getDiagramRootModel((Diagram) getDiagram());
		if(newGroup.eResource() != null) getDiagram().eResource().getContents().add(newGroup);
		model.getElements().add(newGroup);
		newGroup.setContainer(model);
		//set inner model of the group
		Model groupModel = OrmFactory.eINSTANCE.createModel();
		getDiagram().eResource().getContents().add(groupModel);
		newGroup.setModel(groupModel);
		//enable direct editing
		getFeatureProvider().getDirectEditingInfo().setActive(true);
		//add to graphiti representation
		System.out.println("c");
		addGraphicalRepresentation(createContext, newGroup);
		return new Object[] { newGroup };	
	}
	
	//direct editing
	//~~~~~~~~~~~~~~
	private Object getBusinessObject(IDirectEditingContext editingContext) {
		PictogramElement pictogramElement = editingContext.getPictogramElement();
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return businessObject;
	}
		
	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}
		
	@Override
	public boolean canDirectEdit(IDirectEditingContext editingContext) {
		Object businessObject = getBusinessObject(editingContext);
		GraphicsAlgorithm graphicsAlgorithm = editingContext.getGraphicsAlgorithm();
		if(businessObject instanceof org.framed.iorm.model.Shape && graphicsAlgorithm instanceof Text) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			if(shape.getType() == Type.GROUP) {
				return true;
		}	}
		return false;
	}

	@Override
	public String getInitialValue(IDirectEditingContext editingContext) {
		org.framed.iorm.model.Shape group = (org.framed.iorm.model.Shape) getBusinessObject(editingContext);
		return group.getName();
	}
		
	@Override
	public String checkValueValid(String newName, IDirectEditingContext editingContext) {
		if(!(DirectEditingUtil.matchesIdentifier(newName))) return DIRECTEDITING_GROUP;
	    return null;
	}
		
	@Override
	public void setValue(String value, IDirectEditingContext editingContext) {	     
		org.framed.iorm.model.Shape group = (org.framed.iorm.model.Shape) getBusinessObject(editingContext);
		group.setName(value);
		updatePictogramElement(((Shape) editingContext.getPictogramElement()).getContainer());
	}
		
	//layout feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canLayout(ILayoutContext layoutContext) {
		PictogramElement element = layoutContext.getPictogramElement();
		if(element instanceof ContainerShape) {
			EList<EObject> businessObjects = element.getLink().getBusinessObjects();
			if(businessObjects.size()==1) {
				if(businessObjects.get(0) instanceof org.framed.iorm.model.Shape) {
					org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObjects.get(0);
					if(shape.getType() == Type.GROUP) return true;
		}	}	}
		return false;
	}

	@Override
	public boolean layout(ILayoutContext layoutContext) {	
		boolean layoutChanged = false;
		ContainerShape container = (ContainerShape) layoutContext.getPictogramElement();
		RoundedRectangle typeBodyRectangle = null;
		//return false is container is overall container that has typeBodyShape and dropShadowShape as children
		if(container.getGraphicsAlgorithm() == null)  return false; 
		//container is typeBodyShape, else return false
		if(PropertyUtil.isShape_IdValue(container.getGraphicsAlgorithm(), SHAPE_ID_GROUP_TYPEBODY))
			typeBodyRectangle = (RoundedRectangle) container.getGraphicsAlgorithm(); 
		else return false;
		//get the drop shadow rectangle to the type body rectangle
		RoundedRectangle dropShadowRectangle = (RoundedRectangle) container.getContainer().getChildren().get(0).getGraphicsAlgorithm();
			        
		//ensure the minimal width and height
	    if(typeBodyRectangle.getWidth() < MIN_WIDTH) typeBodyRectangle.setWidth(MIN_WIDTH);
		if(typeBodyRectangle.getHeight() < MIN_HEIGHT) typeBodyRectangle.setHeight(MIN_HEIGHT);
		int containerWidth = typeBodyRectangle.getWidth();
	    int containerHeight = typeBodyRectangle.getHeight();
	    //set the size of the drop shadow to the new size of the type body
	    dropShadowRectangle.setWidth(containerWidth);
	    dropShadowRectangle.setHeight(containerHeight);
	    //set the x and y value of the drop shadow to the values of the type body
	    dropShadowRectangle.setX(typeBodyRectangle.getX()+SHADOW_SIZE);
	    dropShadowRectangle.setY(typeBodyRectangle.getY()+SHADOW_SIZE);
	        
	    for (Shape shape : container.getChildren()){
	    	GraphicsAlgorithm graphicsAlgorithm = shape.getGraphicsAlgorithm();                         	                 
	        //name
	        if (graphicsAlgorithm instanceof Text) {
	        	Text text = (Text) graphicsAlgorithm;	
	            if(PropertyUtil.isShape_IdValue(text, SHAPE_ID_GROUP_NAME)) {
	            	graphicAlgorithmService.setLocationAndSize(text, 0, 0, containerWidth, HEIGHT_NAME_SHAPE);
	            	layoutChanged=true;
	        }	}
	        //first line
	        if (graphicsAlgorithm instanceof Polyline) {	   
	        	Polyline polyline = (Polyline) graphicsAlgorithm;  
	        	if(PropertyUtil.isShape_IdValue(polyline, SHAPE_ID_GROUP_LINE)) {
		            polyline.getPoints().set(1, graphicAlgorithmService.createPoint(containerWidth, polyline.getPoints().get(1).getY()));
		            layoutChanged=true;
		    }	}
	        //model container
	        if (graphicsAlgorithm instanceof Rectangle) {
	        	Rectangle rectangle = (Rectangle) graphicsAlgorithm;  
		        if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_GROUP_MODEL)) {
		        	int newHeight = (containerHeight-GROUP_CORNER_RADIUS-2*PUFFER_BETWEEN_ELEMENTS),
		            	newWidth = (containerWidth-2*PUFFER_BETWEEN_ELEMENTS);            				
		        	rectangle.setHeight(newHeight);
		            rectangle.setWidth(newWidth);
		            layoutChanged = true;
		 }	}	}        
	     return layoutChanged;
	}
	
	//update feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canUpdate(IUpdateContext updateContext) {
		//check if object to update is a group
		PictogramElement pictogramElement = updateContext.getPictogramElement();
		Object businessObject =  getBusinessObjectForPictogramElement(pictogramElement);
		if(businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			if(shape.getType() == Type.GROUP) {
				return true;
		}	}
		return false;
	}

	@Override
	public IReason updateNeeded(IUpdateContext updateContext) {
		PictogramElement pictogramElement = updateContext.getPictogramElement();
			
		if(pictogramElement.getGraphicsAlgorithm() != null &&
		   PropertyUtil.isShape_IdValue(pictogramElement.getGraphicsAlgorithm(), SHAPE_ID_GROUP_TYPEBODY)) {
			//pictogram name of natural type, attributes and operations
			String pictogramTypeName = MethodUtil.getPictogramTypeName(pictogramElement, SHAPE_ID_GROUP_NAME);
			//business name and attributes
			String businessTypeName = MethodUtil.getBusinessTypeName(getBusinessObjectForPictogramElement(pictogramElement));
				
			//check for update: different names, different amount of attibutes/ operations
			if(pictogramTypeName==null || businessTypeName==null) return Reason.createTrueReason("Name is null.");
			if(!(pictogramTypeName.equals(businessTypeName))) return Reason.createTrueReason("Name is out of date.");  
		}
		return Reason.createFalseReason();
	}
		
	@Override
	public boolean update(IUpdateContext updateContext) {
		boolean changed = false;
	         
		PictogramElement pictogramElement = updateContext.getPictogramElement();
		//business names of natural type, attributes and operations
		String businessTypeName = MethodUtil.getBusinessTypeName(getBusinessObjectForPictogramElement(pictogramElement));
			
		//set type name in pictogram model
	    if (pictogramElement instanceof ContainerShape) {     
	    	ContainerShape containerShape = (ContainerShape) pictogramElement;
	        for (Shape shape : containerShape.getChildren()) {
	        	if (shape.getGraphicsAlgorithm() instanceof Text) {
	        		Text text = (Text) shape.getGraphicsAlgorithm();
	                if(PropertyUtil.isShape_IdValue(text, SHAPE_ID_GROUP_NAME)) {
	                    	text.setValue(businessTypeName);
	                    	changed = true;
	    }	}	}	} 
	    return changed;
	}
	
	//move feature
	//~~~~~~~~~~~~
	//disable that the user can move the drop shadow manually
	@Override
	public boolean canMoveShape(IMoveShapeContext moveContext) {
		if(PropertyUtil.isShape_IdValue(moveContext.getPictogramElement().getGraphicsAlgorithm(), SHAPE_ID_GROUP_SHADOW)) {
			return false;
		}
		ContainerShape typeBodyShape = (ContainerShape) moveContext.getPictogramElement();
		ContainerShape dropShadowShape = (ContainerShape) ((ContainerShape) typeBodyShape).getContainer().getChildren().get(0);
		//copied and expanded from super.canMoveShape(IMoveShapeContext moveContext)
		return moveContext.getSourceContainer() != null && 
			  (moveContext.getSourceContainer().equals(moveContext.getTargetContainer()) ||
			   moveContext.getTargetContainer().equals(dropShadowShape)) && 
			   isPatternRoot(moveContext.getPictogramElement());
	}
		
	//move the type body and the drop shadow 
	@Override
	public void moveShape(IMoveShapeContext moveContext) {
		ContainerShape typeBodyShape = (ContainerShape) moveContext.getPictogramElement();
		RoundedRectangle typeBodyRectangle = (RoundedRectangle) typeBodyShape.getGraphicsAlgorithm();
		ContainerShape dropShadowShape = (ContainerShape) ((ContainerShape) typeBodyShape).getContainer().getChildren().get(0);
		RoundedRectangle dropShadowRectangle = (RoundedRectangle) dropShadowShape.getGraphicsAlgorithm();
			
		if(moveContext.getSourceContainer().equals(moveContext.getTargetContainer())) {
			dropShadowRectangle.setX(moveContext.getX()+SHADOW_SIZE);
			dropShadowRectangle.setY(moveContext.getY()+SHADOW_SIZE);
			super.moveShape(moveContext);
		} else {
			//targetContainer of moveContext is dropShadowShape
			//set targetContainer to diagram and use special calculation for the new position of type body and drop shadow 
			dropShadowRectangle.setX(typeBodyRectangle.getX()+moveContext.getX()+2*SHADOW_SIZE);
			dropShadowRectangle.setY(typeBodyRectangle.getY()+moveContext.getY()+2*SHADOW_SIZE);
			MoveShapeContext changedMoveContextForTypeBody = new MoveShapeContext(moveContext.getShape());
			changedMoveContextForTypeBody.setTargetContainer(dropShadowShape.getContainer());
			changedMoveContextForTypeBody.setX(typeBodyRectangle.getX()+moveContext.getX()+SHADOW_SIZE);
			changedMoveContextForTypeBody.setY(typeBodyRectangle.getY()+moveContext.getY()+SHADOW_SIZE);
			super.moveShape(changedMoveContextForTypeBody);
	}	}
		
	//resize feature
	//~~~~~~~~~~~~~~
	//disable that the user can resize the drop shadow manually
	@Override
	public boolean canResizeShape(IResizeShapeContext resizeContext) {
		if(PropertyUtil.isShape_IdValue(resizeContext.getPictogramElement().getGraphicsAlgorithm(), SHAPE_ID_GROUP_SHADOW)) {
			return false;
		}
		return super.canResizeShape(resizeContext);
	}
		
	//delete feature
	//~~~~~~~~~~~~~~
	//disable that the user can delete the drop shadow manually
	@Override
	public boolean canDelete(IDeleteContext deleteContext) {
		if(PropertyUtil.isShape_IdValue(deleteContext.getPictogramElement().getGraphicsAlgorithm(), SHAPE_ID_GROUP_SHADOW)) {
			return false;
		}
		return super.canDelete(deleteContext);
	}
			
	//delete parent container (the one that contains drop shadow shape and type body shape)
	@Override
	public void delete(IDeleteContext deleteContext) {
		ContainerShape containerShape = (ContainerShape) ((ContainerShape) deleteContext.getPictogramElement()).getContainer();
		IDeleteContext deleteContextForAllShapes = new DeleteContext(containerShape);
		super.delete(deleteContextForAllShapes);
	}
}
