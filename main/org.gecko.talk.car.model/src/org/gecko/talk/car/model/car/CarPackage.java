/**
 */
package org.gecko.talk.car.model.car;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.gecko.talk.car.model.car.CarFactory
 * @model kind="package"
 * @generated
 */
public interface CarPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "car";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://gecko.io/car/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "car";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CarPackage eINSTANCE = org.gecko.talk.car.model.car.impl.CarPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.gecko.talk.car.model.car.impl.CarImpl <em>Car</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.gecko.talk.car.model.car.impl.CarImpl
	 * @see org.gecko.talk.car.model.car.impl.CarPackageImpl#getCar()
	 * @generated
	 */
	int CAR = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR__ID = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR__OWNER = 2;

	/**
	 * The feature id for the '<em><b>Source Container</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR__SOURCE_CONTAINER = 3;

	/**
	 * The number of structural features of the '<em>Car</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Car</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.gecko.talk.car.model.car.impl.PersonImpl <em>Person</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.gecko.talk.car.model.car.impl.PersonImpl
	 * @see org.gecko.talk.car.model.car.impl.CarPackageImpl#getPerson()
	 * @generated
	 */
	int PERSON = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON__NAME = 1;

	/**
	 * The number of structural features of the '<em>Person</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Person</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.gecko.talk.car.model.car.impl.CarResponseImpl <em>Response</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.gecko.talk.car.model.car.impl.CarResponseImpl
	 * @see org.gecko.talk.car.model.car.impl.CarPackageImpl#getCarResponse()
	 * @generated
	 */
	int CAR_RESPONSE = 2;

	/**
	 * The feature id for the '<em><b>Cars</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR_RESPONSE__CARS = 0;

	/**
	 * The feature id for the '<em><b>Result Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR_RESPONSE__RESULT_SIZE = 1;

	/**
	 * The number of structural features of the '<em>Response</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR_RESPONSE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Response</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR_RESPONSE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link org.gecko.talk.car.model.car.Car <em>Car</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Car</em>'.
	 * @see org.gecko.talk.car.model.car.Car
	 * @generated
	 */
	EClass getCar();

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.talk.car.model.car.Car#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.gecko.talk.car.model.car.Car#getId()
	 * @see #getCar()
	 * @generated
	 */
	EAttribute getCar_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.talk.car.model.car.Car#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.gecko.talk.car.model.car.Car#getType()
	 * @see #getCar()
	 * @generated
	 */
	EAttribute getCar_Type();

	/**
	 * Returns the meta object for the containment reference '{@link org.gecko.talk.car.model.car.Car#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owner</em>'.
	 * @see org.gecko.talk.car.model.car.Car#getOwner()
	 * @see #getCar()
	 * @generated
	 */
	EReference getCar_Owner();

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.talk.car.model.car.Car#getSourceContainer <em>Source Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Container</em>'.
	 * @see org.gecko.talk.car.model.car.Car#getSourceContainer()
	 * @see #getCar()
	 * @generated
	 */
	EAttribute getCar_SourceContainer();

	/**
	 * Returns the meta object for class '{@link org.gecko.talk.car.model.car.Person <em>Person</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Person</em>'.
	 * @see org.gecko.talk.car.model.car.Person
	 * @generated
	 */
	EClass getPerson();

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.talk.car.model.car.Person#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.gecko.talk.car.model.car.Person#getId()
	 * @see #getPerson()
	 * @generated
	 */
	EAttribute getPerson_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.talk.car.model.car.Person#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.gecko.talk.car.model.car.Person#getName()
	 * @see #getPerson()
	 * @generated
	 */
	EAttribute getPerson_Name();

	/**
	 * Returns the meta object for class '{@link org.gecko.talk.car.model.car.CarResponse <em>Response</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Response</em>'.
	 * @see org.gecko.talk.car.model.car.CarResponse
	 * @generated
	 */
	EClass getCarResponse();

	/**
	 * Returns the meta object for the containment reference list '{@link org.gecko.talk.car.model.car.CarResponse#getCars <em>Cars</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cars</em>'.
	 * @see org.gecko.talk.car.model.car.CarResponse#getCars()
	 * @see #getCarResponse()
	 * @generated
	 */
	EReference getCarResponse_Cars();

	/**
	 * Returns the meta object for the attribute '{@link org.gecko.talk.car.model.car.CarResponse#getResultSize <em>Result Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Result Size</em>'.
	 * @see org.gecko.talk.car.model.car.CarResponse#getResultSize()
	 * @see #getCarResponse()
	 * @generated
	 */
	EAttribute getCarResponse_ResultSize();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CarFactory getCarFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.gecko.talk.car.model.car.impl.CarImpl <em>Car</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.gecko.talk.car.model.car.impl.CarImpl
		 * @see org.gecko.talk.car.model.car.impl.CarPackageImpl#getCar()
		 * @generated
		 */
		EClass CAR = eINSTANCE.getCar();
		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CAR__ID = eINSTANCE.getCar_Id();
		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CAR__TYPE = eINSTANCE.getCar_Type();
		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CAR__OWNER = eINSTANCE.getCar_Owner();
		/**
		 * The meta object literal for the '<em><b>Source Container</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CAR__SOURCE_CONTAINER = eINSTANCE.getCar_SourceContainer();
		/**
		 * The meta object literal for the '{@link org.gecko.talk.car.model.car.impl.PersonImpl <em>Person</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.gecko.talk.car.model.car.impl.PersonImpl
		 * @see org.gecko.talk.car.model.car.impl.CarPackageImpl#getPerson()
		 * @generated
		 */
		EClass PERSON = eINSTANCE.getPerson();
		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERSON__ID = eINSTANCE.getPerson_Id();
		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERSON__NAME = eINSTANCE.getPerson_Name();
		/**
		 * The meta object literal for the '{@link org.gecko.talk.car.model.car.impl.CarResponseImpl <em>Response</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.gecko.talk.car.model.car.impl.CarResponseImpl
		 * @see org.gecko.talk.car.model.car.impl.CarPackageImpl#getCarResponse()
		 * @generated
		 */
		EClass CAR_RESPONSE = eINSTANCE.getCarResponse();
		/**
		 * The meta object literal for the '<em><b>Cars</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CAR_RESPONSE__CARS = eINSTANCE.getCarResponse_Cars();
		/**
		 * The meta object literal for the '<em><b>Result Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CAR_RESPONSE__RESULT_SIZE = eINSTANCE.getCarResponse_ResultSize();

	}

} //CarPackage
