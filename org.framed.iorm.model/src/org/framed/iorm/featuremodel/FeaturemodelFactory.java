/**
 */
package org.framed.iorm.featuremodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.framed.iorm.featuremodel.FeaturemodelPackage
 * @generated
 */
public interface FeaturemodelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FeaturemodelFactory eINSTANCE = org.framed.iorm.featuremodel.impl.FeaturemodelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>FRa MED Feature</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>FRa MED Feature</em>'.
	 * @generated
	 */
	FRaMEDFeature createFRaMEDFeature();

	/**
	 * Returns a new object of class '<em>FRa MED Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>FRa MED Configuration</em>'.
	 * @generated
	 */
	FRaMEDConfiguration createFRaMEDConfiguration();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	FeaturemodelPackage getFeaturemodelPackage();

} //FeaturemodelFactory
