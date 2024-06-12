# QuadTree
This project implements a QuadTree data structure in Java. The QuadTree structure is particularly useful in image compression and representation. It also inccludes a helpful visualizer.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- JUnit 4.13 or higher for running tests

### Installation

1. Clone the repository.
2. Navigate to the project directory:
   ```bash
   cd quadtree-compression
   ```
3. Compile the project either through following command or through your favorite IDE:
   ```bash
   javac -cp .:junit-4.13.jar:hamcrest-core-1.3.jar *.java
   ```
4. Run the tests:
   ```bash
   java -cp .:junit-4.13.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore QuadTreeNodeImplTest
   ```

## Features

- **QuadTreeNode Interface**: Defines the essential operations for a QuadTree node.
- **QuadTreeNode Implementation**: Provides a concrete implementation of the QuadTreeNode interface, supporting both leaf nodes and internal nodes.
- **Comprehensive Testing**: Includes a suite of unit tests to validate the functionality and robustness of the QuadTree implementation.

### Testing

The project includes a comprehensive suite of unit tests to ensure the correctness and robustness of the QuadTree implementation. The tests cover a variety of scenarios, including:

- Different quadrant configurations
- Uniform color arrays
- Unique pixel arrays
- Edge cases such as empty arrays and non-power-of-two dimensions
