/**
 */
package org.framed.iorm.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.framed.iorm.featuremodel.FRaMEDConfiguration;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.framed.iorm.model.Model#getElements <em>Elements</em>}</li>
 *   <li>{@link org.framed.iorm.model.Model#getParent <em>Parent</em>}</li>
 *   <li>{@link org.framed.iorm.model.Model#getFramedConfiguration <em>Framed Configuration</em>}</li>
 * </ul>
 *
 * @see org.framed.iorm.model.OrmPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject {
	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.framed.iorm.model.ModelElement}.
	 * It is bidirectional and its opposite is '{@link org.framed.iorm.model.ModelElement#getContainer <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see org.framed.iorm.model.OrmPackage#getModel_Elements()
	 * @see org.framed.iorm.model.ModelElement#getContainer
	 * @model opposite="container" containment="true"
	 * @generated
	 */
	EList<ModelElement> getElements();

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.framed.iorm.model.Shape#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(Shape)
	 * @see org.framed.iorm.model.OrmPackage#getModel_Parent()
	 * @see org.framed.iorm.model.Shape#getModel
	 * @model opposite="model" transient="false"
	 * @generated
	 */
	Shape getParent();

	/**
	 * Sets the value of the '{@link org.framed.iorm.model.Model#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(Shape value);

	/**
	 * Returns the value of the '<em><b>Framed Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Framed Configuration</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Framed Configuration</em>' containment reference.
	 * @see #setFramedConfiguration(FRaMEDConfiguration)
	 * @see org.framed.iorm.model.OrmPackage#getModel_FramedConfiguration()
	 * @model containment="true"
	 * @generated
	 */
	FRaMEDConfiguration getFramedConfiguration();

	/**
	 * Sets the value of the '{@link org.framed.iorm.model.Model#getFramedConfiguration <em>Framed Configuration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Framed Configuration</em>' containment reference.
	 * @see #getFramedConfiguration()
	 * @generated
	 */
	void setFramedConfiguration(FRaMEDConfiguration value);

} // Model
