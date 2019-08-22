/**
 */
package org.gecko.talk.car.model.car;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Car</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.talk.car.model.car.Car#getId <em>Id</em>}</li>
 *   <li>{@link org.gecko.talk.car.model.car.Car#getType <em>Type</em>}</li>
 *   <li>{@link org.gecko.talk.car.model.car.Car#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.gecko.talk.car.model.car.Car#getSourceContainer <em>Source Container</em>}</li>
 * </ul>
 *
 * @see org.gecko.talk.car.model.car.CarPackage#getCar()
 * @model
 * @generated
 */
public interface Car extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.gecko.talk.car.model.car.CarPackage#getCar_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.gecko.talk.car.model.car.Car#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.gecko.talk.car.model.car.CarPackage#getCar_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.gecko.talk.car.model.car.Car#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Owner</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner</em>' containment reference.
	 * @see #setOwner(Person)
	 * @see org.gecko.talk.car.model.car.CarPackage#getCar_Owner()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Person getOwner();

	/**
	 * Sets the value of the '{@link org.gecko.talk.car.model.car.Car#getOwner <em>Owner</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner</em>' containment reference.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(Person value);

	/**
	 * Returns the value of the '<em><b>Source Container</b></em>' attribute.
	 * The default value is <code>"none"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Container</em>' attribute.
	 * @see #setSourceContainer(String)
	 * @see org.gecko.talk.car.model.car.CarPackage#getCar_SourceContainer()
	 * @model default="none"
	 * @generated
	 */
	String getSourceContainer();

	/**
	 * Sets the value of the '{@link org.gecko.talk.car.model.car.Car#getSourceContainer <em>Source Container</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Container</em>' attribute.
	 * @see #getSourceContainer()
	 * @generated
	 */
	void setSourceContainer(String value);

} // Car
