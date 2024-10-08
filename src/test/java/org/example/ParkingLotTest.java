package org.example;

import org.example.Enums.Color;
import org.example.Exceptions.*;
import org.example.Implementations.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {


    @Test
    public void testExceptionNewParkingLotIsEmpty() {
        Owner owner = new Owner();
        assertThrows(CannotCreateParkingLotException.class, () -> owner.createParkingLot(0));
    }

    @Test
    public void testExceptionForNegativeParkingSlots() {
        Owner owner = new Owner();
        assertThrows(CannotCreateParkingLotException.class, () -> owner.createParkingLot(-1));
    }

    @Test
    public void testCreateParkingLotWith5Slots() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);

        assertNotNull(parkingLot);
    }

    // Tests for park() in ParkingLot

    @Test
    public void testParkWith5Slots() throws Exception {
        Owner owner = new Owner();
        Car car = new Car("AP-9876", Color.BLACK);
        ParkingLot parkingLot = owner.createParkingLot(5);
        Ticket ticket = parkingLot.park(car);

        assertNotNull(ticket);
        assertTrue(parkingLot.isCarAlreadyParked(car));
    }

    @Test
    public void testCannotParkSameCarTwice() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);
        Car car = new Car("AP-1234", Color.RED);

        parkingLot.park(car);
        Exception exception = assertThrows(CarAlreadyParkedException.class, () -> parkingLot.park(car));

        assertEquals("Car is already parked", exception.getMessage());
    }

    @Test
    public void testParkingLotWithOneSlotIsFullWhenCarParked() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car("AP-1234", Color.RED);
        Ticket ticket = parkingLot.park(car);

        assertNotNull(ticket);
        assertTrue(parkingLot.isCarAlreadyParked(car));
    }

    @Test
    public void testParkingLotWithTwoSlotsIsNotFullWhenOneCarParked() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(2);
        Car car = new Car("AP-1431", Color.BLUE);
        Ticket ticket = parkingLot.park(car);

        assertNotNull(ticket);
        assertTrue(parkingLot.isCarAlreadyParked(car));
        assertFalse(parkingLot.isFull());
    }

    @Test
    public void testParkInFullParkingLot() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car firstCar = new Car("AP-1234", Color.RED);
        Car secondCar = new Car("AP-5678", Color.BLUE);

        parkingLot.park(firstCar);
        Exception exception = assertThrows(ParkingLotIsFullException.class, () -> parkingLot.park(secondCar));

        assertEquals("Parking lot is full.", exception.getMessage());
    }

    @Test
    public void testParkInNearestAvailableSlot() throws Exception {
        Owner owner = new Owner();
        Car firstCar = new Car("AP-1234", Color.RED);
        Car secondCar = new Car("AP-9999", Color.BLUE);
        ParkingLot parkingLot = owner.createParkingLot(5);

        parkingLot.park(firstCar);
        parkingLot.park(secondCar);

        assertTrue(parkingLot.isCarAlreadyParked(firstCar));
        assertTrue(parkingLot.isCarAlreadyParked(secondCar));
    }

    @Test
    public void testParkInNearestAvailableSlotAfterUnparking() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);
        Car firstCar = new Car("AP-1234", Color.RED);
        Car secondCar = new Car("AP-5678", Color.BLUE);
        Car thirdCar = new Car("AP-9999", Color.GREEN);
        Ticket firstCarTicket = parkingLot.park(firstCar);

        parkingLot.park(secondCar);
        parkingLot.unpark(firstCarTicket);
        parkingLot.park(thirdCar);

        assertTrue(parkingLot.isCarAlreadyParked(thirdCar));
    }

    @Test
    public void testIsCarAlreadyParkedForNonParkedCar() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);
        Car car = new Car("AP-1432", Color.YELLOW);

        assertFalse(parkingLot.isCarAlreadyParked(car));
    }

    @Test
    public void testIsParkingLotFull() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car("AP-4321", Color.BLUE);

        parkingLot.park(car);
        assertTrue(parkingLot.isFull());
    }

    @Test
    public void testIsParkingLotNotFull() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);
        Car car = new Car("AP-9876", Color.GREEN);

        parkingLot.park(car);
        assertFalse(parkingLot.isFull());
    }

    // Tests for unpark() in ParkingLot

    @Test
    public void testUnpark() throws Exception {
        Owner owner = new Owner();
        Car car = new Car("AP-1234", Color.RED);
        ParkingLot parkingLot = owner.createParkingLot(5);
        Ticket ticket = parkingLot.park(car);
        Car unparkedCar = parkingLot.unpark(ticket);

        assertEquals(car, unparkedCar);
    }

    @Test
    public void testUnparkCarThatIsNotParked() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);
        Ticket invalidTicket = new Ticket();

        Exception exception = assertThrows(CarNotFoundException.class, () -> parkingLot.unpark(invalidTicket));

        assertEquals("Car not found in the slot.", exception.getMessage());
    }

    @Test
    public void testUnparkCarWithInvalidTicket() {
        Owner owner = new Owner();
        ParkingLot parkingLot = new ParkingLot(2, owner);
        Car firstCar = new Car("AP-1234", Color.RED);
        parkingLot.park(firstCar);

        Ticket invalidTicket = new Ticket();

        assertThrows(CarNotFoundException.class, () -> parkingLot.unpark(invalidTicket));
    }

    @Test
    public void testUnparkCarFromEmptyParkingLot() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);
        Ticket invalidTicket = new Ticket();  // Empty parking lot

        Exception exception = assertThrows(CarNotFoundException.class, () -> parkingLot.unpark(invalidTicket));

        assertEquals("Car not found in the slot.", exception.getMessage());
    }

    // Tests for countCarsByColor() in ParkingLot

    @Test
    public void testCountCarsByColor() throws Exception {
        Owner owner = new Owner();
        Car firstCar = new Car("AP-1234", Color.RED);
        Car secondCar = new Car("AP-9999", Color.RED);
        Car thirdCar = new Car("AP-0001", Color.BLUE);
        ParkingLot parkingLot = owner.createParkingLot(5);

        parkingLot.park(firstCar);
        parkingLot.park(secondCar);
        parkingLot.park(thirdCar);

        assertEquals(2, parkingLot.countCarsByColor(Color.RED));
    }

    @Test
    public void testCountCarsByRedColorIsNotFoundInParkingLot() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        int count = parkingLot.countCarsByColor(Color.RED);

        assertEquals(0, count);
    }

    @Test
    public void testCountCarsByColorNotPresent() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car("AP-1234", Color.BLUE);

        parkingLot.park(car);
        assertEquals(0, parkingLot.countCarsByColor(Color.YELLOW));
    }

    // Tests for isCarWithRegistrationNumberParked() in ParkingLot

    @Test
    public void testIsCarWithRegistrationNumberParked() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);
        Car car = new Car("AP-1234", Color.RED);

        parkingLot.park(car);
        assertTrue(parkingLot.isCarWithRegistrationNumberParked("AP-1234"));
    }

    @Test
    public void testIsCarWithoutRegistrationNumberCannotParked() throws Exception {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(5);
        Car car = new Car(null, Color.RED);  // Car without a registration number

        Exception exception = assertThrows(CarNeedsRegistrationNumberException.class, () -> parkingLot.isCarWithRegistrationNumberParked(null));

        assertEquals("Car needs registration number.", exception.getMessage());
    }

}
