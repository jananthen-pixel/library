
import java.util.Scanner;
import java.util.InputMismatchException;

class Book {
    private int bookId;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    @Override
    public String toString() {
        return String.format("%-6d | %-30s | %-20s | %-10s",
                bookId, title, author, (isAvailable ? "Available" : "Borrowed"));
    }
}


class BookNode {
    Book data;
    BookNode next;

    public BookNode(Book data) {
        this.data = data;
        this.next = null;
    }
}
class BookLinkedList {
    private BookNode head;
    private int size;

    public BookLinkedList() {
        head = null;
        size = 0;
    }

    public int getSize() { return size; }
    public void addBook(Book book) {
        BookNode newNode = new BookNode(book);
        newNode.next = head;
        head = newNode;
        size++;
    }
    public Book deleteBook(int bookId) {
        if (head == null) return null;
        if (head.data.getBookId() == bookId) {
            Book removed = head.data;
            head = head.next;
            size--;
            return removed;
        }
        BookNode current = head;
        while (current.next != null && current.next.data.getBookId() != bookId) {
            current = current.next;
        }

        if (current.next == null) {
            return null; // Book ID not found
        }

        Book removed = current.next.data;
        current.next = current.next.next; // unlink
        size--;
        return removed;
    }
    public void restoreBook(Book book) {
        addBook(book);
    }
    public Book searchById(int bookId) {
        BookNode current = head;
        while (current != null) {
            if (current.data.getBookId() == bookId) return current.data;
            current = current.next;
        }
        return null;
    }
    public void searchByTitle(String keyword) {
        BookNode current = head;
        boolean found = false;
        String lowerKeyword = keyword.toLowerCase();
        while (current != null) {
            if (current.data.getTitle().toLowerCase().contains(lowerKeyword)) {
                if (!found) {
                    System.out.println("\n--- Search Results ---");
                    printHeader();
                }
                System.out.println(current.data);
                found = true;
            }
            current = current.next;
        }
        if (!found) {
            System.out.println("No books found matching title: \"" + keyword + "\"");
        }
    }
    public boolean idExists(int bookId) {
        return searchById(bookId) != null;
    }
    private void printHeader() {
        System.out.println(String.format("%-6s | %-30s | %-20s | %-10s",
                "ID", "Title", "Author", "Status"));
        System.out.println("-------------------------------------------------------------------------");
    }
    public void displayAll() {
        if (head == null) {
            System.out.println("The library catalogue is currently empty.");
            return;
        }
        System.out.println("\n--- Full Book Catalogue (" + size + " books) ---");
        printHeader();
        BookNode current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
    public void sortByTitle() {
        if (head == null || head.next == null) return;
        boolean swapped;
        do {
            swapped = false;
            BookNode current = head;
            while (current.next != null) {
                if (current.data.getTitle().compareToIgnoreCase(current.next.data.getTitle()) > 0) {
                    Book temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                    swapped = true;
                }
                current = current.next;
            }
        } while (swapped);
        System.out.println("Catalogue successfully sorted alphabetically by title.");
    }
}
class DeletionRecord {
    Book book;
    String action;

    public DeletionRecord(Book book, String action) {
        this.book = book;
        this.action = action;
    }

    @Override
    public String toString() {
        return action + " -> [ID: " + book.getBookId() + ", Title: " + book.getTitle() + "]";
    }
}
class HistoryStack {
    private DeletionRecord[] stackArray;
    private int top;
    private int capacity;

    public HistoryStack(int capacity) {
        this.capacity = capacity;
        this.stackArray = new DeletionRecord[capacity];
        this.top = -1;
    }

    public boolean isEmpty() { return top == -1; }
    public boolean isFull() { return top == capacity - 1; }
    public void push(DeletionRecord record) {
        if (isFull()) {
            
            resize();
        }
        stackArray[++top] = record;
    }
    public DeletionRecord pop() {
        if (isEmpty()) return null;
        return stackArray[top--];
    }
    public DeletionRecord peek() {
        if (isEmpty()) return null;
        return stackArray[top];
    }

    private void resize() {
        capacity *= 2;
        DeletionRecord[] newArray = new DeletionRecord[capacity];
        for (int i = 0; i <= top; i++) newArray[i] = stackArray[i];
        stackArray = newArray;
    }
    public void displayHistory() {
        if (isEmpty()) {
            System.out.println("No deletion/borrow history recorded yet.");
            return;
        }
        System.out.println("\n--- History (Most Recent First) ---");
        for (int i = top; i >= 0; i--) {
            System.out.println((top - i + 1) + ". " + stackArray[i]);
        }
    }
}
class ReservationNode {
    String studentName;
    int studentId;
    int requestedBookId;
    ReservationNode next;

    public ReservationNode(int studentId, String studentName, int requestedBookId) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.requestedBookId = requestedBookId;
        this.next = null;
    }
}
class ReservationQueue {
    private ReservationNode front;
    private ReservationNode rear;
    private int size;

    public ReservationQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    public boolean isEmpty() { return front == null; }
    public int getSize() { return size; }
    public void enqueue(int studentId, String studentName, int requestedBookId) {
        ReservationNode newNode = new ReservationNode(studentId, studentName, requestedBookId);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    public ReservationNode dequeue() {
        if (isEmpty()) return null;
        ReservationNode served = front;
        front = front.next;
        if (front == null) rear = null; 
        size--;
        return served;
    }
    public ReservationNode peekFront() {
        return front;
    }
    public void displayQueue() {
        if (isEmpty()) {
            System.out.println("The reservation waiting list is currently empty.");
            return;
        }
        System.out.println("\n--- Book Reservation Waiting List (FIFO order) ---");
        System.out.println(String.format("%-5s | %-10s | %-20s | %-12s",
                "Pos", "StudentID", "Student Name", "Book ID"));
        System.out.println("-----------------------------------------------------------");
        ReservationNode current = front;
        int position = 1;
        while (current != null) {
            System.out.println(String.format("%-5d | %-10d | %-20s | %-12d",
                    position++, current.studentId, current.studentName, current.requestedBookId));
            current = current.next;
        }
    }
}
public class LibraryManagementSystem {

    private static BookLinkedList catalogue = new BookLinkedList();
    private static HistoryStack historyStack = new HistoryStack(50);
    private static ReservationQueue reservationQueue = new ReservationQueue();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        preloadSampleData();
        boolean running = true;

        System.out.println("=================================================================");
        System.out.println("   WELCOME TO THE UVA WELLASSA UNIVERSITY LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=================================================================");

        while (running) {
            printMenu();
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1: addBookMenu(); break;
                case 2: deleteBookMenu(); break;
                case 3: searchBookMenu(); break;
                case 4: catalogue.displayAll(); break;
                case 5: catalogue.sortByTitle(); break;
                case 6: undoLastDeleteMenu(); break;
                case 7: historyStack.displayHistory(); break;
                case 8: addToReservationQueueMenu(); break;
                case 9: processReservationMenu(); break;
                case 10: reservationQueue.displayQueue(); break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using the Library Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please select an option between 0 and 10.");
            }
        }
        scanner.close();
    }
    private static void printMenu() {
        System.out.println("\n---------------------------- MAIN MENU ----------------------------");
        System.out.println(" [Linked List - Catalogue]");
        System.out.println("  1. Add a New Book");
        System.out.println("  2. Delete a Book by ID");
        System.out.println("  3. Search Book (by ID or Title)");
        System.out.println("  4. Display All Books");
        System.out.println("  5. Sort Catalogue by Title");
        System.out.println(" [Stack - History / Undo]");
        System.out.println("  6. Undo Last Delete");
        System.out.println("  7. View Deletion/Borrow History");
        System.out.println(" [Queue - Reservation Waiting List]");
        System.out.println("  8. Add Student to Reservation Waiting List");
        System.out.println("  9. Process Next Reservation (Serve Student)");
        System.out.println(" 10. Display Reservation Waiting List");
        System.out.println("  0. Exit");
        System.out.println("---------------------------------------------------------------------");
    }
    private static void addBookMenu() {
        System.out.println("\n-- Add a New Book --");
        int id = readIntInput("Enter Book ID: ");
        if (catalogue.idExists(id)) {
            System.out.println("Error: A book with ID " + id + " already exists. Use a unique ID.");
            return;
        }
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author Name: ");
        String author = scanner.nextLine().trim();
        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Error: Title and Author cannot be empty. Book not added.");
            return;
        }
        catalogue.addBook(new Book(id, title, author));
        System.out.println("Book \"" + title + "\" (ID: " + id + ") added successfully.");
    }
    private static void deleteBookMenu() {
        System.out.println("\n-- Delete a Book --");
        int id = readIntInput("Enter Book ID to delete: ");
        Book removed = catalogue.deleteBook(id);
        if (removed == null) {
            System.out.println("Error: No book found with ID " + id + ".");
        } else {
            historyStack.push(new DeletionRecord(removed, "DELETED"));
            System.out.println("Book \"" + removed.getTitle() + "\" (ID: " + id + ") deleted successfully.");
            System.out.println("(Tip: Use option 6 'Undo Last Delete' if this was a mistake.)");
        }
    }
    private static void searchBookMenu() {
        System.out.println("\n-- Search Book --");
        System.out.println("1. Search by Book ID");
        System.out.println("2. Search by Title");
        int subChoice = readIntInput("Enter your choice: ");
        if (subChoice == 1) {
            int id = readIntInput("Enter Book ID: ");
            Book found = catalogue.searchById(id);
            if (found != null) {
                System.out.println("\nBook Found:");
                System.out.println(found);
            } else {
                System.out.println("No book found with ID " + id + ".");
            }
        } else if (subChoice == 2) {
            System.out.print("Enter Title keyword: ");
            String keyword = scanner.nextLine().trim();
            catalogue.searchByTitle(keyword);
        } else {
            System.out.println("Invalid sub-choice.");
        }
    }
    private static void undoLastDeleteMenu() {
        System.out.println("\n-- Undo Last Delete --");
        DeletionRecord lastRecord = historyStack.pop();
        if (lastRecord == null) {
            System.out.println("Nothing to undo. History is empty.");
            return;
        }
        if (catalogue.idExists(lastRecord.book.getBookId())) {
            System.out.println("Cannot restore: A book with ID " + lastRecord.book.getBookId()
                    + " already exists (possibly re-added manually).");
            return;
        }
        catalogue.restoreBook(lastRecord.book);
        System.out.println("Undo successful! Restored book: \"" + lastRecord.book.getTitle()
                + "\" (ID: " + lastRecord.book.getBookId() + ")");
    }
    private static void addToReservationQueueMenu() {
        System.out.println("\n-- Add Student to Reservation Waiting List --");
        int bookId = readIntInput("Enter the Book ID the student wants to reserve: ");
        Book book = catalogue.searchById(bookId);
        if (book == null) {
            System.out.println("Error: No book exists with ID " + bookId + ". Cannot reserve.");
            return;
        }
        if (book.isAvailable()) {
            System.out.println("This book is currently AVAILABLE. The student can borrow it directly");
            System.out.println("without joining the waiting list.");
            return;
        }
        int studentId = readIntInput("Enter Student ID: ");
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Error: Student name cannot be empty.");
            return;
        }

        reservationQueue.enqueue(studentId, name, bookId);
        System.out.println("Student \"" + name + "\" added to the waiting list for Book ID " + bookId
                + " (Queue position: " + reservationQueue.getSize() + ").");
    }

    private static void processReservationMenu() {
        System.out.println("\n-- Process Next Reservation --");
        ReservationNode served = reservationQueue.dequeue();

        if (served == null) {
            System.out.println("The reservation waiting list is empty. No one to serve.");
            return;
        }

        Book book = catalogue.searchById(served.requestedBookId);
        System.out.println("Serving student: \"" + served.studentName + "\" (Student ID: "
                + served.studentId + ") for Book ID " + served.requestedBookId);

        if (book != null) {
            book.setAvailable(false); // now marked as borrowed by this student
            historyStack.push(new DeletionRecord(book, "BORROWED by " + served.studentName));
            System.out.println("Book \"" + book.getTitle() + "\" has been issued to " + served.studentName + ".");
        } else {
            System.out.println("Note: The reserved book no longer exists in the catalogue.");
        }
    }
    private static int readIntInput(String prompt) {
        int value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid whole number.");
            }
        }
    }
    private static void preloadSampleData() {
        catalogue.addBook(new Book(101, "Introduction to Algorithms", "Cormen et al."));
        catalogue.addBook(new Book(102, "Clean Code", "Robert C. Martin"));
        catalogue.addBook(new Book(103, "Effective Java", "Joshua Bloch"));
        catalogue.addBook(new Book(104, "Data Structures in Java", "Lafore"));
        Book borrowed = catalogue.searchById(102);
        if (borrowed != null) borrowed.setAvailable(false);
    }
}
