import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

  public static List<Station> parseStations(String xmlData) {
    List<Station> stations = new ArrayList<>();

    if (xmlData == null || xmlData.trim().isEmpty()) {
      return stations;
    }

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

      NodeList stationNodes = doc.getElementsByTagName("objStation");

      for (int i = 0; i < stationNodes.getLength(); i++) {
        Node node = stationNodes.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }

        Element station = (Element) node;

        String desc = getTagValue("StationDesc", station);
        String code = getTagValue("StationCode", station);
        String id = getTagValue("StationId", station);
        String latStr = getTagValue("StationLatitude", station);
        String lonStr = getTagValue("StationLongitude", station);

        double lat = parseDouble(latStr, 0.0);
        double lon = parseDouble(lonStr, 0.0);

        stations.add(new Station(desc, code, id, lat, lon));
      }
    } catch (Exception e) {
      System.err.println("Error parsing stations: " + e.getMessage());
    }

    return stations;
  }

  public static List<Station> parseStationFilter(String xmlData) {
    List<Station> stations = new ArrayList<>();

    if (xmlData == null || xmlData.trim().isEmpty()) {
      return stations;
    }

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

      NodeList stationNodes = doc.getElementsByTagName("objStationFilter");

      for (int i = 0; i < stationNodes.getLength(); i++) {
        Node node = stationNodes.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }

        Element station = (Element) node;

        String desc = getTagValue("StationDesc", station);
        String code = getTagValue("StationCode", station);

        stations.add(new Station(desc, code, "", 0.0, 0.0));
      }
    } catch (Exception e) {
      System.err.println("Error parsing station filter: " + e.getMessage());
    }

    return stations;
  }

  public static List<Train> parseCurrentTrains(String xmlData) {
    List<Train> trains = new ArrayList<>();

    if (xmlData == null || xmlData.trim().isEmpty()) {
      return trains;
    }

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

      NodeList trainNodes = doc.getElementsByTagName("objTrainPositions");

      for (int i = 0; i < trainNodes.getLength(); i++) {
        Node node = trainNodes.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }

        Element train = (Element) node;

        String code = getTagValue("TrainCode", train);
        String status = getTagValue("TrainStatus", train);
        String latStr = getTagValue("TrainLatitude", train);
        String lonStr = getTagValue("TrainLongitude", train);
        String direction = getTagValue("Direction", train);
        String message = getTagValue("PublicMessage", train);

        double lat = parseDouble(latStr, 0.0);
        double lon = parseDouble(lonStr, 0.0);

        trains.add(new Train(code, status, lat, lon, direction, message));
      }
    } catch (Exception e) {
      System.err.println("Error parsing trains: " + e.getMessage());
    }

    return trains;
  }

  public static List<StationData> parseStationData(String xmlData) {
    List<StationData> stationData = new ArrayList<>();

    if (xmlData == null || xmlData.trim().isEmpty()) {
      return stationData;
    }

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

      NodeList dataNodes = doc.getElementsByTagName("objStationData");

      for (int i = 0; i < dataNodes.getLength(); i++) {
        Node node = dataNodes.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }

        Element data = (Element) node;

        String trainCode = getTagValue("Traincode", data);
        String origin = getTagValue("Origin", data);
        String destination = getTagValue("Destination", data);
        String status = getTagValue("Status", data);
        String dueInStr = getTagValue("Duein", data);
        String lateStr = getTagValue("Late", data);
        String expArrival = getTagValue("Exparrival", data);
        String expDepart = getTagValue("Expdepart", data);
        String direction = getTagValue("Direction", data);
        String trainType = getTagValue("Traintype", data);

        int dueIn = parseInt(dueInStr, 0);
        int late = parseInt(lateStr, 0);

        stationData.add(new StationData(trainCode, origin, destination,
            status, dueIn, late, expArrival, expDepart, direction, trainType));
      }
    } catch (Exception e) {
      System.err.println("Error parsing station data: " + e.getMessage());
    }

    return stationData;
  }

  private static String getTagValue(String tag, Element element) {
    if (tag == null || element == null) {
      return "";
    }

    try {
      NodeList nodeList = element.getElementsByTagName(tag);
      if (nodeList.getLength() > 0) {
        Node node = nodeList.item(0);
        if (node != null && node.getFirstChild() != null) {
          String value = node.getFirstChild().getNodeValue();
          return value != null ? value.trim() : "";
        }
      }
    } catch (Exception e) {
    }
    return "";
  }

  private static double parseDouble(String value, double defaultValue) {
    if (value == null || value.trim().isEmpty()) {
      return defaultValue;
    }
    try {
      return Double.parseDouble(value.trim());
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  private static int parseInt(String value, int defaultValue) {
    if (value == null || value.trim().isEmpty()) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public static List<TrainMovement> parseTrainMovements(String xmlData) {
    List<TrainMovement> movements = new ArrayList<>();

    if (xmlData == null || xmlData.trim().isEmpty()) {
      return movements;
    }

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

      NodeList movementNodes = doc.getElementsByTagName("objTrainMovements");

      for (int i = 0; i < movementNodes.getLength(); i++) {
        Node node = movementNodes.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }

        Element movement = (Element) node;

        String trainCode = getTagValue("TrainCode", movement);
        String trainDate = getTagValue("TrainDate", movement);
        String locationCode = getTagValue("LocationCode", movement);
        String locationFullName = getTagValue("LocationFullName", movement);
        String locationOrderStr = getTagValue("LocationOrder", movement);
        String locationType = getTagValue("LocationType", movement);
        String trainOrigin = getTagValue("TrainOrigin", movement);
        String trainDestination = getTagValue("TrainDestination", movement);
        String scheduledArrival = getTagValue("ScheduledArrival", movement);
        String scheduledDeparture = getTagValue("ScheduledDeparture", movement);
        String arrival = getTagValue("Arrival", movement);
        String departure = getTagValue("Departure", movement);
        String autoArrivalStr = getTagValue("AutoArrival", movement);
        String autoDepartStr = getTagValue("AutoDepart", movement);
        String stopType = getTagValue("StopType", movement);

        int locationOrder = parseInt(locationOrderStr, 0);
        int autoArrival = parseInt(autoArrivalStr, 0);
        int autoDepart = parseInt(autoDepartStr, 0);

        movements.add(new TrainMovement(trainCode, trainDate, locationCode,
            locationFullName, locationOrder, locationType, trainOrigin,
            trainDestination, scheduledArrival, scheduledDeparture, arrival,
            departure, autoArrival, autoDepart, stopType));
      }
    } catch (Exception e) {
      System.err.println("Error parsing train movements: " + e.getMessage());
    }

    return movements;
  }
}