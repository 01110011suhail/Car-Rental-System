import java.util.*;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePrice;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePrice) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePrice = basePrice;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int days) {
        return basePrice * days;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

public class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;
    private String adminPassword;

    public CarRentalSystem(String adminPassword) {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
        this.adminPassword = adminPassword;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented.");
        }
    }

    public void showAdminMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter Admin Password: ");
        String passwordAttempt = scanner.nextLine();

        if (!passwordAttempt.equals(adminPassword)) {
            System.out.println("Incorrect password. Exiting Admin Menu.");
            return;
        }

        while (true) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Add Car");
            System.out.println("2. View Rented Cars");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                addCarFromAdmin();
            } else if (choice == 2) {
                viewRentedCars();
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nExiting Admin Menu.");
    }

    private void addCarFromAdmin() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n== Add Car ==\n");
        System.out.print("Enter Car ID: ");
        String carId = scanner.nextLine();

        System.out.print("Enter Brand: ");
        String brand = scanner.nextLine();

        System.out.print("Enter Model: ");
        String model = scanner.nextLine();

        System.out.print("Enter Base Price per Day: ");
        double basePrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Car newCar = new Car(carId, brand, model, basePrice);
        addCar(newCar);
        System.out.println("\nCar added successfully.");
    }

    private void viewRentedCars() {
        System.out.println("\n===== Rented Cars =====");
        for (Rental rental : rentals) {
            Car car = rental.getCar();
            Customer customer = rental.getCustomer();
            int days = rental.getDays();

            System.out.println("Car ID: " + car.getCarId());
            System.out.println("Brand: " + car.getBrand());
            System.out.println("Model: " + car.getModel());
            System.out.println("Rented by: " + customer.getName());
            System.out.println("Rental Days: " + days);
            System.out.println("-------------------------");
        }
    }

    public void showCustomerMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Customer Menu =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. View Rental Receipts");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                rentCarFromCustomer();
            } else if (choice == 2) {
                returnCarFromCustomer();
            } else if (choice == 3) {
                viewRentalReceipts();
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nExiting Customer Menu.");
    }

    private void rentCarFromCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n== Rent a Car ==\n");
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
            }
        }

        System.out.print("\nEnter the car ID you want to rent: ");
        String carId = scanner.nextLine();

        System.out.print("Enter the number of days for rental: ");
        int rentalDays = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
        addCustomer(newCustomer);

        Car selectedCar = null;
        for (Car car : cars) {
            if (car.getCarId().equals(carId) && car.isAvailable()) {
                selectedCar = car;
                break;
            }
        }

        if (selectedCar != null) {
            double totalPrice = selectedCar.calculatePrice(rentalDays);
            System.out.println("\n== Rental Information ==\n");
            System.out.println("Customer ID: " + newCustomer.getCustomerId());
            System.out.println("Customer Name: " + newCustomer.getName());
            System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
            System.out.println("Rental Days: " + rentalDays);
            System.out.printf("Total Price: $%.2f%n", totalPrice);

            System.out.print("\nConfirm rental (Y/N): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                rentCar(selectedCar, newCustomer, rentalDays);
                System.out.println("\nCar rented successfully.");
            } else {
                System.out.println("\nRental canceled.");
            }
        } else {
            System.out.println("\nInvalid car selection or car not available for rent.");
        }
    }

    private void returnCarFromCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n== Return a Car ==\n");
        System.out.print("Enter the car ID you want to return: ");
        String carId = scanner.nextLine();

        Car carToReturn = null;
        for (Car car : cars) {
            if (car.getCarId().equals(carId) && !car.isAvailable()) {
                carToReturn = car;
                break;
            }
        }

        if (carToReturn != null) {
            Customer customer = null;
            for (Rental rental : rentals) {
                if (rental.getCar() == carToReturn) {
                    customer = rental.getCustomer();
                    break;
                }
            }

            if (customer != null) {
                returnCar(carToReturn);
                System.out.println("Car returned successfully by " + customer.getName());
            } else {
                System.out.println("Car was not rented or rental information is missing.");
            }
        } else {
            System.out.println("Invalid car ID or car is not rented.");
        }
    }

    public void viewRentalReceipts() {
        System.out.println("\n===== Rental Receipts =====");
        for (Rental rental : rentals) {
            Car car = rental.getCar();
            Customer customer = rental.getCustomer();
            int days = rental.getDays();
            double totalPrice = car.calculatePrice(days);

            System.out.println("Customer ID: " + customer.getCustomerId());
            System.out.println("Customer Name: " + customer.getName());
            System.out.println("Car: " + car.getBrand() + " " + car.getModel());
            System.out.println("Rental Days: " + days);
            System.out.printf("Total Price: $%.2f%n", totalPrice);
            System.out.println("-------------------------");
        }
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Login =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                showAdminMenu();
            } else if (choice == 2) {
                showCustomerMenu();
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nExiting Car Rental System.");
    }

    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem("admin123");

        for (int i = 1; i <= 20; i++) {
            Car car = new Car("C" + String.format("%03d", i), "Brand" + i, "Model" + i, i * 10.0);
            rentalSystem.addCar(car);
        }

        rentalSystem.login();
    }
}
