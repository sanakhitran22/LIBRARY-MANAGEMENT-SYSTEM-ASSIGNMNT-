//PROGRAM FOR LIBRARY MANAGEMENT SYSTEM
//Importing necessary libraries
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
//Main class for the Library Management System
public class LibraryManagementSystem {
    private static Scanner input;//Scanner object to take input from the user
    //Main method to start the program
    public static void main(String[] args) {
        input = new Scanner(System.in);//Initialize scanner object to read user input
        //Array lists to store books and users
        ArrayList<Book> books = new ArrayList<>();//Initialize array list for books
        ArrayList<User> users = new ArrayList<>();//Initialize array list for users
        Library library = new Library(input, books, users);
        //Load existing data from files
        library.loadData();
        //Display menu and handle user's choice
        int choice;
        do {
            displayMenu();
            choice = getUserChoice();
            library.handleUserChoice(choice);
        } while (choice != 0);//Continue until user chooses to exit
        //Close scanner
        input.close();
        library.saveData();//Save data back to files
    }
    //Method to display the Menu options 
    private static void displayMenu() {
        System.out.println("Library Management System");
        System.out.println("1. Add a new book");
        System.out.println("2. Add a new user");
        System.out.println("3. Display all books");
        System.out.println("4. Checkout a book");
        System.out.println("5. Return a book");
        System.out.println("6. Search for a book by title or author");
        System.out.println("0. Exit");
    }
    //Method to get user's choice from the menu
    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        int choice = input.nextInt();
        return choice;
    }    
}
//Library class to manage books and users
class Library {
    private Scanner input;
    private ArrayList<Book> books;
    private ArrayList<User> users;
    //Constructor tO initialize Library object with provided data
    public Library(Scanner input, ArrayList<Book> books, ArrayList<User> users) {
        this.input = input;
        this.books = books;
        this.users = users;
    }
    //Method to handle user's choice and perform corresponding action
    public void handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                addNewBook();//Add a new book
                break;
            case 2:
                addNewUser();//Add a new user
                break;
            case 3:
                displayAllBooks();//Display all bboks
                break;
            case 4:
                checkoutBook();//Checkout a book
                break;
            case 5:
                returnBook();//Return a book
                break;
            case 6:
                searchBook();//Search for a book
                break;
            case 0:
                System.out.println("Exiting...");//Exit the program
                break;
            default:
                System.out.println("Invalid choice. Please try again.");//Invalid choice
                break;
        }
    } 
    //Method to add a new book   
    private void addNewBook() {
        System.out.print("Enter book title: ");
        String title = input.nextLine();//User input for the book title
        input.nextLine();//Clear the input buffer
        System.out.print("Enter book author: ");
        String author = input.nextLine();//Read book author from user
        System.out.print("Enter book genre: ");
        String genre = input.nextLine();//Read book genre from user
        Book newBook = new Book(title, author, genre);//Create new Book object
        books.add(newBook);//Add new book to the list of books
        System.out.println("Book added successfully.");
    }
    //Method to add a new user
    private void addNewUser(){
        System.out.print("Enter user name: ");
        String name = input.nextLine();//Read the username from user
        input.nextLine();//Clear the input buffer
        System.out.print("Enter user contact information: ");
        String contactInfo = input.nextLine();//User input for the contact information
        User newUser = new User(name, contactInfo);//Create new User object
        users.add(newUser);//Add new user to the list of users
        System.out.println("User added successfully.");
    }
    //Method to display all books
    private void displayAllBooks() {
        for (Book book : books) {//Iterate through each book in the list of books
            System.out.println("1. ID: " + book.getId() + " 2. Title: " + book.getTitle() + " 3. Author: " + book.getAuthor() + " 4. Genre: " + book.getGenre() + " 5. Availability status: " + book.isAvailable());
        }
    }
    //Method to checkout a book
    private void checkoutBook() {
        System.out.print("Enter book title or author: ");
        String searchKey = input.nextLine();//Read search key from user
        input.nextLine();
        Book bookToCheckout = findBookByTitleOrAuthor(searchKey);//Find book in the library using title or author
        if (bookToCheckout == null) {
            System.out.println("Book not found.");
            return;
        }
        if (!bookToCheckout.isAvailable()) {
            System.out.println("Book is not available.");
            return;
        }
        System.out.print("Enter user ID: ");
        int userId = input.nextInt();//Read user ID from user
        User user = findUserById(userId);//Finf user in the library by user ID
        if (user == null) {
            System.out.println("User not found.");
            return;
    }
    bookToCheckout.setAvailable(false);//Mark the book as unavailable
    user.borrowBook(bookToCheckout);//Add the book to the user's list of borrowed books
    System.out.println("Book checked out successfully.");
    }
    //Method to add a return book 
    private void returnBook() {
        System.out.print("Enter book title or author: ");
        String searchKey = input.nextLine();//Read search key from user
        Book bookToReturn = findBookByTitleOrAuthor(searchKey);//Find book by title or author
        if (bookToReturn == null) {
            System.out.println("Book not found.");
            return;
        }
        System.out.print("Enter user ID: ");
        int userId = input.nextInt();//Prompt user to enter user ID
        User user = findUserById(userId);//Find user by ID
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        user.returnBook(bookToReturn);//Return book
        bookToReturn.setAvailable(true);//Set availabilty to true
        System.out.println("Book returned successfully.");
    }
    //Method to search a book by title or author
    private void searchBook() {
        System.out.print("Enter book title or author: ");
        String searchKey = input.nextLine();//Read search key from user
        Book book = findBookByTitleOrAuthor(searchKey);//Find book by title or author
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        System.out.println(book);
    }
    //Method to find a book by title or author
    private Book findBookByTitleOrAuthor(String searchKey) {
        for (Book book : books) {//Iterate through the list of books
            //Check if title or author matches the search key
            if (book.getTitle().equalsIgnoreCase(searchKey) || book.getAuthor().equalsIgnoreCase(searchKey)) {
                return book;//Return book if found
            }
        }
        return null;//Return null if book not found
    }
    //Method to find a user by ID
    private User findUserById(int userId) {
        for (User user : users) {//Iterate through the list of users
            //Check if the user ID matches the provided ID
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }
    public void loadData() {
        //Load data for books
        File booksFile = new File("books.txt");//Create a File object representing the books.txt file
        File usersFile = new File("users.txt");//Create a File object representing the users.txt file
        //Check if files exist
        if (!booksFile.exists() || !usersFile.exists()) {
            return; //Exit method if books.txt or users.txt files don't exist
        }
        try {
            //Read data from books file
            Scanner booksScanner = new Scanner(booksFile);//Create a Scanner object to read data from the books file
            Scanner usersScanner = new Scanner(usersFile);//Create a Scanner object to read data from the users file
        
            //Loop to read data for each book from the books file
            while (booksScanner.hasNextLine()) {
                //Split each line into data components
                String[] bookData = booksScanner.nextLine().split(",");
                //Extract book details from the split data
                String title = bookData[0];//Extract the title of the book
                String author = bookData[1];//Extract the author of the book
                String genre = bookData[2];//Extract the genre of the book
                boolean availability = Boolean.parseBoolean(bookData[3]);//Extract the availability of the book
                //Create a new Book object using the extracted details and add it to the list of books
                Book newBook = new Book(title, author, genre, availability);
                books.add(newBook);
            }
            //Close scanners
            booksScanner.close();
            usersScanner.close();
        } catch (FileNotFoundException e) {
            //Handle file not found exception
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    //ethod to save data to files
    public void saveData() {
        try {
            //Create PrintWriter object for users and books files
            PrintWriter booksWriter = new PrintWriter("books.txt");
            PrintWriter usersWriter = new PrintWriter("users.txt");
            //Write data for each book to the book files
            for (Book book : books) {
                booksWriter.println(book.getTitle() + "," + book.getAuthor() + "," + book.getGenre() + "," + book.isAvailable());
            }
            //Write data for each user to the users file
            for (User user : users) {
                usersWriter.println(user.getName() + "," + user.getContactInfo());
            }
            //Close PrintWriter objects
            booksWriter.close();
            usersWriter.close();
        } catch (IOException e) {
            //Handle IO exception
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
class Book {
    // Fields to represent book attributes
    private int ID;
    private String title;
    private String author;
    private String genre;
    private boolean available;
    private static int bookIdCounter = 0;//To generate unique book IDs
    //Constructor to initialize a Book object with title, author, and genre
    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = true;//Set book availability to true by default
        this.ID = ++bookIdCounter;//Incrementing the counter to generate unique IDs
    }
    //Constructor to initialize a Book object with title, author, genre, and availability
    public Book(String title, String author, String genre, boolean availability) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = availability;//Set book availability based on the provided parameter
        this.ID = ++bookIdCounter;//Incrementing the counter to generate unique IDs
    }
    //Getter method for retrieving the book ID
    public int getId() {
        return ID;
    }
    //Getter method for retrieving the book title
    public String getTitle() {
        return title;
    }
    // Getter method for retrieving the book author
    public String getAuthor() {
        return author;
    }
    // Getter method for retrieving the book genre
    public String getGenre() {
        return genre;
    }
    // Getter method for retrieving the availability status of the book
    public boolean isAvailable() {
        return available;
    }
    // Setter method for updating the availability status of the book
    public void setAvailable(boolean availability) {
        this.available = availability;
    }
    // The @Override annotation is a concept of inheritance, indicates that a method is intended to override a method in a superclass or interface, helping catch errors at compile time.
    @Override
    public String toString() {
        return "Book ID: " + ID + "\tTitle: " + title + "\tAuthor: " + author + "\tGenre: " + genre + "\tAvailability: " + available;
    }
}
class User {
    // Fields to represent user attributes
    private String name;
    private String contactInfo;
    private ArrayList<Book> borrowedBooks;
    private static int userIdCounter = 0;//To generate unique user IDs
    private int ID1;//User ID
    //Constructor to initialize a User object with name and contact information
    public User(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.borrowedBooks = new ArrayList<>();//Initialize an empty ArrayList to store borrowed books
        this.ID1 = ++userIdCounter;//Incrementing the counter to generate unique IDs
    }
    //Getter method for retrieving the user ID
    public int getId() {
        return ID1;
    }
    //Getter method for retrieving the user's name
    public String getName() {
        return name;
    }
    //Getter method for retrieving the user's contact information
    public String getContactInfo() {
        return contactInfo;
    }
    //Getter method for retrieving the list of borrowed books by the user
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    //Method for adding a borrowed book to the user's list of borrowed books
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }
    //Method for returning a borrowed book from the user's list of borrowed books
    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }
    //Method overriding the toString() method to provide a string representation of the User object
    @Override
    public String toString() {
        return "User ID: " + ID1 + "\tName: " + name + "\tContact Information: " + contactInfo;
    }
}