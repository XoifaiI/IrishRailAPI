public class StationData {
  private String trainCode;
  private String origin;
  private String destination;
  private String status;
  private int dueIn;
  private int minutesLate;
  private String expectedArrival;
  private String expectedDeparture;
  private String direction;
  private String trainType;

  public StationData(String trainCode, String origin, String destination,
      String status, int dueIn, int late, String expArr,
      String expDep, String direction, String trainType) {
    this.trainCode = trainCode != null ? trainCode : "Unknown";
    this.origin = origin != null ? origin : "Unknown";
    this.destination = destination != null ? destination : "Unknown";
    this.status = status != null ? status : "No status";
    this.dueIn = Math.max(0, dueIn);
    this.minutesLate = Math.max(0, late);
    this.expectedArrival = expArr != null ? expArr : "";
    this.expectedDeparture = expDep != null ? expDep : "";
    this.direction = direction != null ? direction : "";
    this.trainType = trainType != null ? trainType : "Unknown";
  }

  public String getTrainCode() {
    return trainCode;
  }

  public String getOrigin() {
    return origin;
  }

  public String getDestination() {
    return destination;
  }

  public String getStatus() {
    return status;
  }

  public int getDueIn() {
    return dueIn;
  }

  public int getMinutesLate() {
    return minutesLate;
  }

  public String getExpectedArrival() {
    return expectedArrival;
  }

  public String getExpectedDeparture() {
    return expectedDeparture;
  }

  public String getDirection() {
    return direction;
  }

  public String getTrainType() {
    return trainType;
  }

  @Override
  public String toString() {
    String lateText;
    if (minutesLate <= 0) {
      lateText = " (On time)";
    } else {
      lateText = " (" + minutesLate + " min late)";
    }
    return String.format("%s | %s to %s | Due: %d min%s | %s | %s",
        trainType, origin, destination, dueIn, lateText, direction, status);
  }
}