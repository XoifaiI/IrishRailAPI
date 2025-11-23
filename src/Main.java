import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    IrishRailClient api = new IrishRailClient();
    Scanner input = new Scanner(System.in);

    printHeader();

    while (true) {
      printMenu();
      System.out.print("Choose option: ");

      String choiceStr = input.nextLine().trim();

      if (!choiceStr.matches("\\d+")) {
        System.out.println("\nError: Please enter a number between 1 and 8");
        continue;
      }

      int choice = Integer.parseInt(choiceStr);

      try {
        switch (choice) {
          case 1 -> searchStations(api, input);
          case 2 -> getStationTrains(api, input);
          case 3 -> getCurrentTrains(api);
          case 4 -> getDartTrains(api);
          case 5 -> getDartStations(api);
          case 6 -> trackTrainJourney(api, input);
          case 7 -> showLiveTrainMap(api, input);
          case 8 -> {
            System.out.println("\nGoodbye!");
            input.close();
            return;
          }
          default -> System.out.println("\nError: Invalid option. Please choose 1-8");
        }
      } catch (IllegalArgumentException e) {
        System.out.println("\nInput Error: " + e.getMessage());
      } catch (Exception e) {
        System.out.println("\nAPI Error: " + e.getMessage());
      }
    }
  }

  private static void printHeader() {
    System.out.println("=====================================");
    System.out.println("  Irish Rail Real-Time Tracker");
    System.out.println("=====================================\n");
  }

  private static void printMenu() {
    System.out.println("\n--- MENU ---");
    System.out.println("1. Search for stations");
    System.out.println("2. Get trains at a station");
    System.out.println("3. Get all current trains");
    System.out.println("4. Get DART trains only");
    System.out.println("5. Get all DART stations");
    System.out.println("6. Track train journey");
    System.out.println("7. Live train map");
    System.out.println("8. Exit");
    System.out.println();
  }

  private static void searchStations(IrishRailClient api, Scanner input) throws Exception {
    System.out.print("\nEnter station name (or part of it): ");
    String search = input.nextLine().trim();

    if (search.isEmpty()) {
      System.out.println("Error: Please enter a search term");
      return;
    }

    System.out.println("\nSearching for stations containing '" + search + "'...");
    List<Station> stations = api.searchStations(search);

    System.out.println("\n--- SEARCH RESULTS ---");
    if (stations == null || stations.isEmpty()) {
      System.out.println("No stations found.");
      System.out.println("Suggestions: Try 'bray', 'dublin', 'cork', 'galway'");
    } else {
      System.out.println("Found " + stations.size() + " station(s):\n");
      for (Station station : stations) {
        if (station != null) {
          System.out.println("  " + station);
        }
      }
    }
    System.out.println();
  }

  private static void getStationTrains(IrishRailClient api, Scanner input) throws Exception {
    System.out.print("\nEnter station name (e.g. Bray, Dublin Connolly): ");
    String station = input.nextLine().trim();

    if (station.isEmpty()) {
      System.out.println("Error: Please enter a station name");
      return;
    }

    System.out.print("Show trains for next how many minutes (5-90): ");
    String minsStr = input.nextLine().trim();

    if (!minsStr.matches("\\d+")) {
      System.out.println("Error: Please enter a valid number");
      return;
    }

    int mins = Integer.parseInt(minsStr);

    if (mins < 5 || mins > 90) {
      System.out.println("Warning: Minutes adjusted to valid range (5-90)");
      mins = Math.max(5, Math.min(90, mins));
    }

    System.out.println("\nFetching trains at " + station + "...");
    List<StationData> trains = api.getStationData(station, mins);

    System.out.println("\n--- TRAINS AT " + station.toUpperCase() + " ---");
    if (trains == null || trains.isEmpty()) {
      System.out.println("No trains found in the next " + mins + " minutes.");
      System.out.println("Note: Station name might be incorrect. Try searching first.");
    } else {
      System.out.println("Found " + trains.size() + " train(s):\n");
      for (StationData train : trains) {
        if (train != null) {
          System.out.println("  " + train);
        }
      }
    }
    System.out.println();
  }

  private static void getCurrentTrains(IrishRailClient api) throws Exception {
    System.out.println("\nFetching current trains...");
    List<Train> trains = api.getCurrentTrains();

    System.out.println("\n--- CURRENT TRAINS ---");
    if (trains == null || trains.isEmpty()) {
      System.out.println("No trains currently running.");
    } else {
      System.out.println("Total running trains: " + trains.size() + "\n");
      int count = 0;
      for (Train train : trains) {
        if (train != null) {
          count++;
          System.out.println("[" + count + "] " + train);
          if (count < trains.size()) {
            System.out.println();
          }
        }
      }
    }
  }

  private static void getDartTrains(IrishRailClient api) throws Exception {
    System.out.println("\nFetching DART trains...");
    List<Train> trains = api.getCurrentTrainsByType("D");

    System.out.println("\n--- DART TRAINS ---");
    if (trains == null || trains.isEmpty()) {
      System.out.println("No DART trains currently running.");
    } else {
      System.out.println("Total DART trains running: " + trains.size() + "\n");
      int count = 0;
      for (Train train : trains) {
        if (train != null) {
          count++;
          System.out.println("[" + count + "] " + train);
          if (count < trains.size()) {
            System.out.println();
          }
        }
      }
    }
  }

  private static void getDartStations(IrishRailClient api) throws Exception {
    System.out.println("\nFetching DART stations...");
    List<Station> stations = api.getStationsByType("D");

    System.out.println("\n--- DART STATIONS ---");
    if (stations == null || stations.isEmpty()) {
      System.out.println("No DART stations found.");
    } else {
      System.out.println("Total DART stations: " + stations.size() + "\n");
      for (Station station : stations) {
        if (station != null) {
          System.out.println("  " + station);
        }
      }
    }
    System.out.println();
  }

  private static void trackTrainJourney(IrishRailClient api, Scanner input) throws Exception {
    System.out.print("\nEnter train code (e.g. E109, A802): ");
    String trainId = input.nextLine().trim();

    if (trainId.isEmpty()) {
      System.out.println("Error: Please enter a train code");
      return;
    }

    System.out.print("Enter train date (e.g. '02 Nov 2025', 'today'): ");
    String trainDate = input.nextLine().trim();

    if (trainDate.isEmpty()) {
      System.out.println("Error: Please enter a date");
      return;
    }

    if (trainDate.equalsIgnoreCase("today")) {
      trainDate = "02 Nov 2025";
    }

    System.out.println("\nFetching journey details for train " + trainId + "...");
    List<TrainMovement> movements = api.getTrainMovements(trainId, trainDate);

    if (movements == null || movements.isEmpty()) {
      System.out.println("\n--- TRAIN JOURNEY ---");
      System.out.println("No journey data found for train " + trainId + " on " + trainDate);
      System.out.println("Note: Train code may be incorrect or date format wrong.");
      System.out.println("Tip: Get train codes from 'Current Trains' menu option first.");
    } else {
      TrainMovement first = movements.get(0);
      System.out.println("\n=== TRAIN JOURNEY: " + trainId + " ===");
      System.out.println("Route: " + first.getTrainOrigin() + " to "
          + first.getTrainDestination());
      System.out.println("Date: " + first.getTrainDate());
      System.out.println("Total Stops: " + movements.size());
      System.out.println();

      for (TrainMovement movement : movements) {
        if (movement != null) {
          System.out.println(movement);
          System.out.println();
        }
      }

      System.out.println("Legend:");
      System.out.println("  [ORIGIN] - Starting station");
      System.out.println("  [STOP] - Regular stop");
      System.out.println("  [TIMING] - Timing point (non-stopping)");
      System.out.println("  [DESTINATION] - Final station");
      System.out.println("  << CURRENT - Train is currently here");
      System.out.println("  >> NEXT - Next scheduled stop");
    }
    System.out.println();
  }

  private static void showLiveTrainMap(IrishRailClient api, Scanner input) throws Exception {
    System.out.println("\n--- LIVE TRAIN MAP ---");
    System.out.println("Options:");
    System.out.println("1. All trains");
    System.out.println("2. DART trains only");
    System.out.println("3. All trains + stations");
    System.out.print("\nChoose option (1-3): ");

    String optionStr = input.nextLine().trim();
    int option = 1;

    if (optionStr.matches("\\d+")) {
      option = Integer.parseInt(optionStr);
    }

    System.out.println("\nFetching train positions...");

    List<Train> trains;
    boolean showStations = (option == 3);

    if (option == 2) {
      trains = api.getCurrentTrainsByType("D");
    } else {
      trains = api.getCurrentTrains();
    }

    LiveTrainMap mapDisplay = new LiveTrainMap();

    if (showStations) {
      System.out.println("Loading stations...");
      List<Station> stations = api.getAllStations();
      mapDisplay.setStations(stations);
    }

    mapDisplay.refresh(trains);
    mapDisplay.displaySplitView(showStations);

    System.out.println("\nPress Enter to refresh or type 'back' to return to menu...");
    String response = input.nextLine().trim();

    if (!response.equalsIgnoreCase("back")) {
      showLiveTrainMap(api, input);
    }
  }
}