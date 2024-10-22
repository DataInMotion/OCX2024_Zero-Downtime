/*
 */
package org.gecko.talk.car.model.car;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Response</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.talk.car.model.car.CarResponse#getCars <em>Cars</em>}</li>
 *   <li>{@link org.gecko.talk.car.model.car.CarResponse#getResultSize <em>Result Size</em>}</li>
 * </ul>
 *
 * @see org.gecko.talk.car.model.car.CarPackage#getCarResponse()
 * @model
 * @generated
 */
@ProviderType
public interface CarResponse extends EObject {
	/**
	 * Returns the value of the '<em><b>Cars</b></em>' containment reference list.
	 * The list contents are of type {@link org.gecko.talk.car.model.car.Car}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cars</em>' containment reference list.
	 * @see org.gecko.talk.car.model.car.CarPackage#getCarResponse_Cars()
	 * @model containment="true"
	 * @generated
	 */
	EList<Car> getCars();

	/**
	 * Returns the value of the '<em><b>Result Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result Size</em>' attribute.
	 * @see #setResultSize(int)
	 * @see org.gecko.talk.car.model.car.CarPackage#getCarResponse_ResultSize()
	 * @model
	 * @generated
	 */
	int getResultSize();

	/**
	 * Sets the value of the '{@link org.gecko.talk.car.model.car.CarResponse#getResultSize <em>Result Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Size</em>' attribute.
	 * @see #getResultSize()
	 * @generated
	 */
	void setResultSize(int value);

} // CarResponse
