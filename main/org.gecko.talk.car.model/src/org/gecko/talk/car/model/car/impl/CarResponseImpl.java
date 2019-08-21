/**
 */
package org.gecko.talk.car.model.car.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.gecko.talk.car.model.car.Car;
import org.gecko.talk.car.model.car.CarPackage;
import org.gecko.talk.car.model.car.CarResponse;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Response</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.gecko.talk.car.model.car.impl.CarResponseImpl#getCars <em>Cars</em>}</li>
 *   <li>{@link org.gecko.talk.car.model.car.impl.CarResponseImpl#getResultSize <em>Result Size</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CarResponseImpl extends MinimalEObjectImpl.Container implements CarResponse {
	/**
	 * The cached value of the '{@link #getCars() <em>Cars</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCars()
	 * @generated
	 * @ordered
	 */
	protected EList<Car> cars;

	/**
	 * The default value of the '{@link #getResultSize() <em>Result Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSize()
	 * @generated
	 * @ordered
	 */
	protected static final int RESULT_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getResultSize() <em>Result Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSize()
	 * @generated
	 * @ordered
	 */
	protected int resultSize = RESULT_SIZE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CarResponseImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CarPackage.Literals.CAR_RESPONSE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Car> getCars() {
		if (cars == null) {
			cars = new EObjectContainmentEList<Car>(Car.class, this, CarPackage.CAR_RESPONSE__CARS);
		}
		return cars;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getResultSize() {
		return resultSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setResultSize(int newResultSize) {
		int oldResultSize = resultSize;
		resultSize = newResultSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CarPackage.CAR_RESPONSE__RESULT_SIZE, oldResultSize, resultSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CarPackage.CAR_RESPONSE__CARS:
				return ((InternalEList<?>)getCars()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CarPackage.CAR_RESPONSE__CARS:
				return getCars();
			case CarPackage.CAR_RESPONSE__RESULT_SIZE:
				return getResultSize();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CarPackage.CAR_RESPONSE__CARS:
				getCars().clear();
				getCars().addAll((Collection<? extends Car>)newValue);
				return;
			case CarPackage.CAR_RESPONSE__RESULT_SIZE:
				setResultSize((Integer)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CarPackage.CAR_RESPONSE__CARS:
				getCars().clear();
				return;
			case CarPackage.CAR_RESPONSE__RESULT_SIZE:
				setResultSize(RESULT_SIZE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CarPackage.CAR_RESPONSE__CARS:
				return cars != null && !cars.isEmpty();
			case CarPackage.CAR_RESPONSE__RESULT_SIZE:
				return resultSize != RESULT_SIZE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (resultSize: ");
		result.append(resultSize);
		result.append(')');
		return result.toString();
	}

} //CarResponseImpl
