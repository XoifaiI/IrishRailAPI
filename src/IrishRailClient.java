import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class IrishRailClient {
  private static final String BASE_URL = "http://api.irishrail.ie/realtime/realtime.asmx";
  private static final int MIN_MINUTES = 5;
  private static final int MAX_MINUTES = 90;
  private HttpClient client;

  public IrishRailClient() {
    this.client = HttpClient.newHttpClient();
  }

  public List<Station> getAllStations() throws Exception {
    String url = BASE_URL + "/getAllStationsXML";
    String xmlResponse = makeRequest(url);
    if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
      throw new Exception("API returned empty response");
    }
    return XMLParser.parseStations(xmlResponse);
  }

  public List<Station> getStationsByType(String type) throws Exception {
    String validType = validateStationType(type);
    String url = BASE_URL + "/getAllStationsXML_WithStationType?StationType=" + validType;
    String xmlResponse = makeRequest(url);
    if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
      throw new Exception("API returned empty response");
    }
    return XMLParser.parseStations(xmlResponse);
  }

  public List<Train> getCurrentTrains() throws Exception {
    String url = BASE_URL + "/getCurrentTrainsXML";
    String xmlResponse = makeRequest(url);
    if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
      throw new Exception("API returned empty response");
    }
    return XMLParser.parseCurrentTrains(xmlResponse);
  }

  public List<Train> getCurrentTrainsByType(String type) throws Exception {
    String validType = validateStationType(type);
    String url = BASE_URL + "/getCurrentTrainsXML_WithTrainType?TrainType=" + validType;
    String xmlResponse = makeRequest(url);
    if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
      throw new Exception("API returned empty response");
    }
    return XMLParser.parseCurrentTrains(xmlResponse);
  }

  public List<StationData> getStationData(String stationName, int minutes) throws Exception {
    if (stationName == null || stationName.trim().isEmpty()) {
      throw new IllegalArgumentException("Station name cannot be empty");
    }
    int validMinutes = validateMinutes(minutes);
    String encodedName = URLEncoder.encode(stationName.trim(), StandardCharsets.UTF_8);
    String url = BASE_URL + "/getStationDataByNameXML?StationDesc="
        + encodedName + "&NumMins=" + validMinutes;
    String xmlResponse = makeRequest(url);
    if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
      throw new Exception("API returned empty response");
    }
    return XMLParser.parseStationData(xmlResponse);
  }

  public List<StationData> getStationDataByCode(String stationCode, int minutes)
      throws Exception {
    if (stationCode == null || stationCode.trim().isEmpty()) {
      throw new IllegalArgumentException("Station code cannot be empty");
    }
    int validMinutes = validateMinutes(minutes);
    String url = BASE_URL + "/getStationDataByCodeXML_WithNumMins?StationCode="
        + stationCode.trim() + "&NumMins=" + validMinutes;
    String xmlResponse = makeRequest(url);
    if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
      throw new Exception("API returned empty response");
    }
    return XMLParser.parseStationData(xmlResponse);
  }

  public List<Station> searchStations(String searchText) throws Exception {
    if (searchText == null || searchText.trim().isEmpty()) {
      throw new IllegalArgumentException("Search text cannot be empty");
    }
    String encodedText = URLEncoder.encode(searchText.trim(), StandardCharsets.UTF_8);
    String url = BASE_URL + "/getStationsFilterXML?StationText=" + encodedText;
    String xmlResponse = makeRequest(url);
    if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
      throw new Exception("API returned empty response");
    }
    return XMLParser.parseStationFilter(xmlResponse);
  }

  public List<TrainMovement> getTrainMovements(String trainId, String trainDate)
      throws Exception {
    if (trainId == null || trainId.trim().isEmpty()) {
      throw new IllegalArgumentException("Train ID cannot be empty");
    }
    if (trainDate == null || trainDate.trim().isEmpty()) {
      throw new IllegalArgumentException("Train date cannot be empty");
    }
    String encodedId = URLEncoder.encode(trainId.trim(), StandardCharsets.UTF_8);
    String encodedDate = URLEncoder.encode(trainDate.trim(), StandardCharsets.UTF_8);
    String url = BASE_URL + "/getTrainMovementsXML?TrainId="
        + encodedId + "&TrainDate=" + encodedDate;
    String xmlResponse = makeRequest(url);
    if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
      throw new Exception("API returned empty response");
    }
    return XMLParser.parseTrainMovements(xmlResponse);
  }

  private String makeRequest(String url) throws Exception {
    if (url == null || url.trim().isEmpty()) {
      throw new IllegalArgumentException("URL cannot be empty");
    }
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new Exception("API request failed with status code: " + response.statusCode());
    }

    return response.body();
  }

  private String validateStationType(String type) {
    if (type == null || type.trim().isEmpty()) {
      return "A";
    }
    String upperType = type.trim().toUpperCase();
    if (upperType.equals("A") || upperType.equals("D")
        || upperType.equals("M") || upperType.equals("S")) {
      return upperType;
    }
    return "A";
  }

  private int validateMinutes(int minutes) {
    if (minutes < MIN_MINUTES) {
      return MIN_MINUTES;
    }
    if (minutes > MAX_MINUTES) {
      return MAX_MINUTES;
    }
    return minutes;
  }
}