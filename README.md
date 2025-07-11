# Real-Time Event Ticketing System

## Overview

The Real-Time Event Ticketing System is a comprehensive simulation application that demonstrates advanced producer-consumer patterns, multi-threading, and synchronization concepts. The system simulates a real-world ticket booking scenario where multiple vendors can release tickets concurrently while multiple customers attempt to purchase them.

## Architecture

The project follows a full-stack architecture with clear separation of concerns:

- **Backend**: Spring Boot application with Java implementing the core business logic
- **Frontend**: Angular web application providing a modern user interface
- **Real-time Communication**: WebSocket integration for live updates

## Key Features

### Core Functionality
- **Multi-threaded Simulation**: Concurrent vendor and customer operations
- **Producer-Consumer Pattern**: Vendors produce tickets, customers consume them
- **Thread-Safe Operations**: Proper synchronization to prevent race conditions
- **Real-time Monitoring**: Live updates via WebSocket connections
- **Data Persistence**: JSON-based data storage with chunked file management

### Advanced Features
- **VIP Customer Priority**: Premium customers get higher thread priority
- **Dynamic Entity Management**: Add/remove vendors and customers at runtime
- **Pool Management**: Separate pools for tickets, purchases, and threads
- **RESTful API**: Complete HTTP API for system interaction
- **Responsive UI**: Modern Angular interface with real-time updates

## Technology Stack

### Backend
- **Java 23** - Core programming language
- **Spring Boot 3.4.0** - Application framework
- **Spring WebSocket** - Real-time communication
- **Jackson** - JSON processing
- **Lombok** - Code generation
- **Maven** - Build tool

### Frontend
- **Angular 19** - Frontend framework
- **TypeScript** - Programming language
- **RxJS** - Reactive programming
- **STOMP/SockJS** - WebSocket client
- **CSS3** - Styling

## Getting Started

### Prerequisites
- Java 23 or higher
- Node.js 18+ and npm
- Maven 3.9+
- Modern web browser

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/0-kodiya-0/OOP_CW.git
   cd real-time-ticketing-system
   ```

2. **Backend Setup**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```
   Backend will start on http://localhost:8080

3. **Frontend Setup**
   ```bash
   cd frontend
   npm install
   npm start
   ```
   Frontend will start on http://localhost:4200

### Configuration

The system uses a configuration-driven approach:

- **Initial Setup**: Configure total tickets, release rates, retrieval rates, and capacity
- **Runtime Management**: Add/remove entities through CLI or web interface
- **Persistence**: Configuration and simulation data saved to JSON files

## Usage

### Command Line Interface (CLI)
Run the backend and interact through console:
- Configure system parameters
- Add/remove customers and vendors
- Start/stop simulations
- Monitor live events
- View resource statistics

### Web Interface
Access the Angular frontend for:
- **Home**: Real-time system overview and statistics
- **Pool**: View ticket and purchase pool data
- **Simulation**: Manage customers and vendors

### API Endpoints

#### Simulation Control
- `GET /simulation/start` - Start simulation
- `GET /simulation/stop` - Stop simulation
- `GET /simulation/is-running` - Check simulation status

#### Entity Management
- `POST /simulation/customer/add` - Add customers
- `POST /simulation/vendor/add` - Add vendors
- `GET /simulation/customer/all` - List customers
- `GET /simulation/vendor/all` - List vendors

#### Pool Monitoring
- `GET /pool/ticket/all` - View all tickets
- `GET /pool/purchase/all` - View all purchases
- `GET /pool/ticket/quantity-not-full` - Available tickets

## Key Concepts

### Producer-Consumer Pattern
- **Producers (Vendors)**: Create and add tickets to the shared pool
- **Consumers (Customers)**: Retrieve and purchase tickets from the pool
- **Shared Resource**: Thread-safe ticket pool with capacity management

### Synchronization
- **ReentrantLocks**: Fine-grained locking for individual resources
- **Concurrent Collections**: Thread-safe data structures
- **Atomic Operations**: Preventing race conditions

### Thread Management
- **Custom Thread Pool**: Manages simulation entity threads
- **Priority Handling**: VIP customers get higher thread priority
- **Lifecycle Management**: Proper thread startup and shutdown

## Monitoring and Analytics

### Real-time Updates
- **WebSocket Streaming**: Live data updates every 2 seconds
- **Pool Statistics**: Thread, ticket, and purchase pool metrics
- **Simulation Status**: Active entities and system state

### Data Persistence
- **Chunked Storage**: Large datasets split into manageable files
- **JSON Format**: Human-readable data storage
- **Memory Management**: Automatic cleanup and archiving

## Troubleshooting

### Common Issues
1. **Port Conflicts**: Ensure ports 8080 and 4200 are available
2. **Memory Issues**: Adjust JVM heap size for large simulations
3. **WebSocket Errors**: Check CORS configuration and firewall settings

### Performance Tuning
- **Thread Pool Size**: Adjust based on system capacity
- **Chunk Size**: Modify for optimal file I/O performance
- **Update Intervals**: Balance real-time updates with performance

## Contributing

1. Fork the repository
2. Create a feature branch
3. Implement changes with proper testing
4. Submit a pull request with detailed description

## License

This project is developed for educational purposes as part of the Real-Time Event Ticketing System coursework.

## Academic Context

This project demonstrates:
- **Object-Oriented Programming**: Proper encapsulation and inheritance
- **Multi-threading**: Concurrent programming concepts
- **System Design**: Scalable architecture patterns
- **Modern Development**: Full-stack application development
- **Best Practices**: Code quality and documentation standards

The implementation showcases industry-relevant skills in concurrent programming, system design, and full-stack development, preparing students for professional software development environments.
