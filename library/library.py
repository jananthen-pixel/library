import os

# Books list ah file la save panna function
def save_books(books):
    with open("books.txt", "w") as f:
        for book, status in books.items():
            f.write(f"{book},{status}\n")

# File lendhu books ah load panna function
def load_books():
    books = {}
    if os.path.exists("books.txt"):
        with open("books.txt", "r") as f:
            for line in f:
                if "," in line:
                    book, status = line.strip().split(",")
                    books[book] = status
    else:
        # Default books for first time run
        books = {"Python Basics": "Available", "Data Structures": "Available"}
        save_books(books)
    return books

def main():
    books = load_books()
    
    while True:
        print("\n--- LIBRARY MANAGEMENT SYSTEM ---")
        print("1. View All Books")
        print("2. Add New Book")
        print("3. Borrow a Book")
        print("4. Return a Book")
        print("5. Exit")
        
        choice = input("Enter your choice (1-5): ")
        
        if choice == '1':
            print("\n--- Current Books In Library ---")
            for book, status in books.items():
                print(f"Book: {book} | Status: {status}")
                
        elif choice == '2':
            new_book = input("Enter new book title: ").strip()
            if new_book in books:
                print("Book already exists!")
            elif new_book == "":
                print("Invalid Title!")
            else:
                books[new_book] = "Available"
                save_books(books)
                print(f"'{new_book}' successfully added!")
                
        elif choice == '3':
            borrow_book = input("Enter book title to borrow: ").strip()
            if borrow_book in books and books[borrow_book] == "Available":
                books[borrow_book] = "Borrowed"
                save_books(books)
                print(f"You have borrowed '{borrow_book}'.")
            else:
                print("Sorry, book is not available or doesn't exist.")
                
        elif choice == '4':
            return_book = input("Enter book title to return: ").strip()
            if return_book in books and books[return_book] == "Borrowed":
                books[return_book] = "Available"
                save_books(books)
                print(f"Thank you for returning '{return_book}'.")
            else:
                print("This book was not borrowed or invalid title.")
                
        elif choice == '5':
            print("Thank you for using the Library System. Goodbye!")
            break
        else:
            print("Invalid choice! Try again.")

if __name__ == "__main__":
    main()