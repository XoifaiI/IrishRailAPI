# Irish Rail API Client

A Java application for tracking Irish Rail trains in real-time using the Irish Rail API (thats almost older than me).

## Features

- **Search Stations** - Find stations by name
- **Station Trains** - View upcoming trains at any station
- **Live Train Map** - Visual map of all active trains
- **Track Journey** - Follow a specific train's journey
- **DART Support** - Filter for DART trains and stations
- **Real-time Data** - Live updates from Irish Rail API

## Requirements

- Java 21 LTS or higher
- Internet connection for API access

## Compilation

```bash
javac src/*.java
```

## Running

```bash
java -cp src Main
```

## Usage
The code provides an menu with the following options:
1. **Search for stations** - Find stations by partial name match
2. **Get trains at a station** - View upcoming trains (with time window)
3. **Get all current trains** - List all trains currently running
4. **Get DART trains only** - Filter for DART services
5. **Get all DART stations** - List all DART stations
6. **Track train journey** - Follow a specific train's route
7. **Live train map** - Visual representation of train positions
8. **Exit** - Close the application

## API Source

This application uses the [Irish Rail Real Time API](http://api.irishrail.ie/realtime/).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
