import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveTrainMap {
  private static final int MAP_WIDTH = 45;
  private static final int MAP_HEIGHT = 28;

  private static final double IRELAND_MIN_LAT = 51.4;
  private static final double IRELAND_MAX_LAT = 55.4;
  private static final double IRELAND_MIN_LON = -10.5;
  private static final double IRELAND_MAX_LON = -5.5;

  private static final String RESET = "\u001B[0m";
  private static final String MAGENTA = "\u001B[35m";
  private static final String CYAN = "\u001B[36m";
  private static final String BRIGHT_RED = "\u001B[91m";
  private static final String BRIGHT_GREEN = "\u001B[92m";
  private static final String BRIGHT_YELLOW = "\u001B[93m";
  private static final String BRIGHT_CYAN = "\u001B[96m";
  private static final String BRIGHT_MAGENTA = "\u001B[95m";
  private static final String WHITE = "\u001B[37m";
  private static final String GRAY = "\u001B[90m";

  private List<TrainPosition> trainPositions;
  private Map<String, CityInfo> cities;
  private List<Station> stations;

  private static class TrainPosition {
    String code;
    int mapX;
    int mapY;
    String color;
    String route;

    TrainPosition(String code, int x, int y, String color, String route) {
      this.code = code;
      this.mapX = x;
      this.mapY = y;
      this.color = color;
      this.route = route;
    }
  }

  private static class CityInfo {
    String name;
    double lat;
    double lon;
    String color;
    List<String> trainsAtCity;

    CityInfo(String name, double lat, double lon, String color) {
      this.name = name;
      this.lat = lat;
      this.lon = lon;
      this.color = color;
      this.trainsAtCity = new ArrayList<>();
    }
  }

  public LiveTrainMap() {
    trainPositions = new ArrayList<>();
    cities = new HashMap<>();
    stations = new ArrayList<>();
    initializeCities();
  }

  private void initializeCities() {
    cities.put("Dublin", new CityInfo("Dublin", 53.3498, -6.2603, BRIGHT_CYAN));
    cities.put("Cork", new CityInfo("Cork", 51.8985, -8.4756, BRIGHT_RED));
    cities.put("Galway", new CityInfo("Galway", 53.2707, -9.0568, BRIGHT_GREEN));
    cities.put("Limerick", new CityInfo("Limerick", 52.6638, -8.6267, BRIGHT_YELLOW));
    cities.put("Sligo", new CityInfo("Sligo", 54.2697, -8.4694, BRIGHT_MAGENTA));
  }

  public void setStations(List<Station> stationList) {
    this.stations = stationList != null ? stationList : new ArrayList<>();
  }

  public void plotTrains(List<Train> trains) {
    trainPositions.clear();
    for (CityInfo city : cities.values()) {
      city.trainsAtCity.clear();
    }

    if (trains == null) {
      return;
    }

    for (Train train : trains) {
      if (train == null || !train.getStatus().equals("R")) {
        continue;
      }

      double lat = train.getLatitude();
      double lon = train.getLongitude();

      if (lat == 0.0 && lon == 0.0) {
        continue;
      }

      String nearCity = findNearestCity(lat, lon);
      if (nearCity != null) {
        cities.get(nearCity).trainsAtCity.add(train.getTrainCode());
        continue;
      }

      int[] coords = latLonToXY(lat, lon);
      if (coords != null) {
        String color = getTrainColor(train);
        String route = extractRoute(train.getPublicMessage());
        trainPositions.add(new TrainPosition(
            train.getTrainCode(), coords[0], coords[1], color, route));
      }
    }
  }

  private String findNearestCity(double lat, double lon) {
    double threshold = 0.1;

    for (Map.Entry<String, CityInfo> entry : cities.entrySet()) {
      CityInfo city = entry.getValue();
      double distance = Math.sqrt(Math.pow(city.lat - lat, 2) + Math.pow(city.lon - lon, 2));
      if (distance < threshold) {
        return entry.getKey();
      }
    }
    return null;
  }

  private String getTrainColor(Train train) {
    String code = train.getTrainCode();
    if (code == null || code.isEmpty()) {
      return WHITE;
    }

    char firstChar = code.charAt(0);
    switch (firstChar) {
      case 'E':
        return BRIGHT_CYAN;
      case 'D':
        return BRIGHT_GREEN;
      case 'A':
        return BRIGHT_YELLOW;
      case 'P':
        return MAGENTA;
      case 'B':
        return CYAN;
      default:
        return WHITE;
    }
  }

  private String extractRoute(String message) {
    if (message == null || message.isEmpty()) {
      return "Unknown route";
    }

    String[] lines = message.split("\\\\n");
    if (lines.length >= 2) {
      String routeLine = lines[1];
      if (routeLine.contains(" - ")) {
        String[] parts = routeLine.split(" - ", 2);
        if (parts.length == 2) {
          return parts[1];
        }
      }
    }
    return "Unknown route";
  }

  private int[] latLonToXY(double lat, double lon) {
    if (lat < IRELAND_MIN_LAT || lat > IRELAND_MAX_LAT
        || lon < IRELAND_MIN_LON || lon > IRELAND_MAX_LON) {
      return null;
    }

    double latRange = IRELAND_MAX_LAT - IRELAND_MIN_LAT;
    double lonRange = IRELAND_MAX_LON - IRELAND_MIN_LON;

    int x = (int) ((lon - IRELAND_MIN_LON) / lonRange * (MAP_WIDTH - 1));
    int y = (int) ((IRELAND_MAX_LAT - lat) / latRange * (MAP_HEIGHT - 1));

    x = Math.max(0, Math.min(MAP_WIDTH - 1, x));
    y = Math.max(0, Math.min(MAP_HEIGHT - 1, y));

    return new int[]{x, y};
  }

  public void displaySplitView(boolean showStations) {
    System.out.println("\n" + BRIGHT_YELLOW + "=== LIVE TRAINS ===" + RESET);
    System.out.println();

    String[] mapLines = new String[MAP_HEIGHT + 4];
    mapLines[0] = "         N";
    mapLines[1] = "    W  +" + "-".repeat(MAP_WIDTH) + "+  E";

    char[][] mapGrid = new char[MAP_HEIGHT][MAP_WIDTH];
    String[][] colorGrid = new String[MAP_HEIGHT][MAP_WIDTH];

    for (int y = 0; y < MAP_HEIGHT; y++) {
      for (int x = 0; x < MAP_WIDTH; x++) {
        mapGrid[y][x] = ' ';
        colorGrid[y][x] = WHITE;
      }
    }

    if (showStations && stations != null) {
      for (Station station : stations) {
        if (station == null) {
          continue;
        }
        double lat = station.getLatitude();
        double lon = station.getLongitude();
        if (lat == 0.0 && lon == 0.0) {
          continue;
        }
        int[] coords = latLonToXY(lat, lon);
        if (coords != null) {
          if (mapGrid[coords[1]][coords[0]] == ' ') {
            mapGrid[coords[1]][coords[0]] = '.';
            colorGrid[coords[1]][coords[0]] = GRAY;
          }
        }
      }
    }

    for (int i = 0; i < trainPositions.size(); i++) {
      TrainPosition tp = trainPositions.get(i);
      char marker = (char) ('1' + (i % 9));
      if (mapGrid[tp.mapY][tp.mapX] != '*') {
        mapGrid[tp.mapY][tp.mapX] = marker;
        colorGrid[tp.mapY][tp.mapX] = tp.color;
      }
    }

    for (Map.Entry<String, CityInfo> entry : cities.entrySet()) {
      CityInfo city = entry.getValue();
      int[] coords = latLonToXY(city.lat, city.lon);
      if (coords != null) {
        mapGrid[coords[1]][coords[0]] = '*';
        colorGrid[coords[1]][coords[0]] = city.color;
      }
    }

    for (int y = 0; y < MAP_HEIGHT; y++) {
      StringBuilder line = new StringBuilder("       |");
      for (int x = 0; x < MAP_WIDTH; x++) {
        line.append(colorGrid[y][x]).append(mapGrid[y][x]).append(RESET);
      }
      line.append("|");
      mapLines[y + 2] = line.toString();
    }

    mapLines[MAP_HEIGHT + 2] = "       +" + "-".repeat(MAP_WIDTH) + "+";
    mapLines[MAP_HEIGHT + 3] = "         S";

    int trainIndex = 0;
    String header = BRIGHT_CYAN + "NUM | CODE | ROUTE" + RESET;

    for (int i = 0; i < mapLines.length; i++) {
      String leftSide = mapLines[i];
      String rightSide = "";

      if (i == 0) {
        rightSide = header;
      } else if (i == 1) {
        rightSide = GRAY + "----+------+---------------------------" + RESET;
      } else if (trainIndex < trainPositions.size()) {
        TrainPosition tp = trainPositions.get(trainIndex);
        char marker = (char) ('1' + (trainIndex % 9));
        String route = tp.route;
        if (route.length() > 25) {
          route = route.substring(0, 22) + "...";
        }
        rightSide = String.format("%s %c %s | %s%s%s | %s",
            tp.color, marker, RESET,
            tp.color, tp.code, RESET,
            route);
        trainIndex++;
      }

      System.out.println(leftSide + "  " + rightSide);
    }

    System.out.println();
    System.out.println(GRAY + "Cities: " + RESET
        + cities.get("Dublin").color + "*" + RESET + "=Dublin "
        + cities.get("Cork").color + "*" + RESET + "=Cork "
        + cities.get("Galway").color + "*" + RESET + "=Galway "
        + cities.get("Limerick").color + "*" + RESET + "=Limerick "
        + cities.get("Sligo").color + "*" + RESET + "=Sligo");

    for (Map.Entry<String, CityInfo> entry : cities.entrySet()) {
      CityInfo city = entry.getValue();
      if (!city.trainsAtCity.isEmpty()) {
        System.out.print(city.color + city.name + RESET + ": ");
        for (int i = 0; i < city.trainsAtCity.size(); i++) {
          System.out.print(city.trainsAtCity.get(i));
          if (i < city.trainsAtCity.size() - 1) {
            System.out.print(", ");
          }
        }
        System.out.println();
      }
    }

    System.out.println();
    System.out.println(GRAY + "Legend: " + BRIGHT_CYAN + "Cyan" + RESET + "=DART "
        + BRIGHT_YELLOW + "Yellow" + RESET + "=Intercity "
        + BRIGHT_GREEN + "Green" + RESET + "=Commuter "
        + (showStations ? GRAY + "." + RESET + "=Station " : ""));
    System.out.println(GRAY + "Total trains: " + (trainPositions.size() + getTotalCityTrains())
        + " (" + trainPositions.size() + " on map, " + getTotalCityTrains() + " at cities)"
        + RESET);

    if (trainPositions.size() > 9) {
      System.out.println(GRAY + "Note: Only first 9 trains numbered on map" + RESET);
    }
  }

  private int getTotalCityTrains() {
    int total = 0;
    for (CityInfo city : cities.values()) {
      total += city.trainsAtCity.size();
    }
    return total;
  }

  public void refresh(List<Train> trains) {
    plotTrains(trains);
  }
}