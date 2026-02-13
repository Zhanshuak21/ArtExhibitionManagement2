ART EXHIBITION MANAGEMENT SYSTEM

This is an Art Exhibition Management System developed in Java.
The application simulates the management of an art exhibition where different types
of artworks (paintings and sculptures) created by artists are stored, displayed,
and managed in a gallery.

The project focuses on demonstrating Object-Oriented Programming (OOP) principles
through a real-world inspired example.

/

Key Features:
domain.Artwork Management:
- Create artworks (paintings and sculptures)
- Store artworks in a gallery
- Mark artworks as sold

domain.Artist Management:
- Create and assign artists to artworks
- Store artist information (name, birth year, nationality)

Insurance Cost Calculation:
- Different insurance cost calculation logic for paintings and sculptures

service.Gallery Management:
- Store artworks in a data pool
- Sort artworks by price
- Search for the most expensive artwork
- Filter sold artworks

/

Classes:
domain.Artwork (Abstract Class)
- Base class for all artworks
- Contains common fields and abstract method calculateInsuranceCost()

domain.Painting
- Inherits from domain.Artwork
- Has material and style attributes

domain.Sculpture
- Inherits from domain.Artwork
- Has medium and weight attributes

domain.Artist
- Represents an artist with personal information

service.Gallery
- Stores and manages a collection of artworks

Main
- Handles user input and demonstrates program functionality

/

Steps to Run:
1. Download all .java files:
    - Main.java
    - domain.Artwork.java
    - domain.Painting.java
    - domain.Sculpture.java
    - domain.Artist.java
    - service.Gallery.java
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
- Abstraction: domain.Artwork is an abstract class with an abstract method calculateInsuranceCost()
- Encapsulation: All class fields are private and accessed via getters and setters
- Inheritance: domain.Painting and domain.Sculpture extend domain.Artwork
- Polymorphism: Demonstrated via Method Overriding (e.g., calculateInsuranceCost()) and Method Overloading (e.g., two versions of addArtwork() in service.Gallery).

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
- service.Gallery manages multiple artworks
