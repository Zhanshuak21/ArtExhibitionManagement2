ART EXHIBITION MANAGEMENT SYSTEM

This is an Art Exhibition Management System developed in Java.
The application simulates the management of an art exhibition where different types
of artworks (paintings and sculptures) created by artists are stored, displayed,
and managed in a gallery.

The project focuses on demonstrating Object-Oriented Programming (OOP) principles
through a real-world inspired example.

/

Key Features:
Artwork Management:
- Create artworks (paintings and sculptures)
- Store artworks in a gallery
- Mark artworks as sold

Artist Management:
- Create and assign artists to artworks
- Store artist information (name, birth year, nationality)

Insurance Cost Calculation:
- Different insurance cost calculation logic for paintings and sculptures

Gallery Management:
- Store artworks in a data pool
- Sort artworks by price
- Search for the most expensive artwork
- Filter sold artworks

/

Classes:
Artwork (Abstract Class)
- Base class for all artworks
- Contains common fields and abstract method calculateInsuranceCost()

Painting
- Inherits from Artwork
- Has material and style attributes

Sculpture
- Inherits from Artwork
- Has medium and weight attributes

Artist
- Represents an artist with personal information

Gallery
- Stores and manages a collection of artworks

Main
- Handles user input and demonstrates program functionality

/

Steps to Run:
1. Download all .java files:
    - Main.java
    - Artwork.java
    - Painting.java
    - Sculpture.java
    - Artist.java
    - Gallery.java
2. Open IntelliJ IDEA.
3. Create a new Java project.
4. Add all files to the src folder.
5. Run Main.java.
6. Follow the console instructions to enter artist and artwork data.

/

Program Execution Flow:
- The program starts by creating an artist using user input.
- A painting is created based on user-provided data.
- Additional artworks are created for demonstration.
- All artworks are added to a gallery.
- The program demonstrates:
    - Polymorphism using printInfo()
    - Sorting artworks by price
    - Searching for the most expensive artwork
    - Filtering sold artworks
    - Usage of toString() and equals() methods

/

Assignment Requirements Completed:

OOP Principles:
- Abstraction: Artwork is an abstract class with an abstract method calculateInsuranceCost()
- Encapsulation: All class fields are private and accessed via getters and setters
- Inheritance: Painting and Sculpture extend Artwork
- Polymorphism: Demonstrated via Method Overriding (e.g., calculateInsuranceCost()) and Method Overloading (e.g., two versions of addArtwork() in Gallery).

Data Handling:
- Data pool implemented using ArrayList
- Sorting implemented using Comparator
- Searching implemented using loops
- Filtering implemented using loops

Required Project Structure:
- Multiple classes with attributes, constructors, and methods
- Object creation in the Main class
- Console output for demonstration
- Object comparison (finding most expensive artwork)

Java Basics Used:
- Variables of different types (int, double, boolean, String)
- If/else statements
- For loops
- Arrays and collections
- Scanner for user input
- System.out.println() for output

/

Real-World Logic:
- Artworks can be sold and tracked
- Different artwork types have different insurance calculation logic
- Artists are linked to artworks
- Gallery manages multiple artworks
