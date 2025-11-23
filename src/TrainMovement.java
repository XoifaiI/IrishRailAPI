public class TrainMovement {
  private String trainCode;
  private String trainDate;
  private String locationCode;
  private String locationFullName;
  private int locationOrder;
  private String locationType;
  private String trainOrigin;
  private String trainDestination;
  private String scheduledArrival;
  private String scheduledDeparture;
  private String arrival;
  private String departure;
  private int autoArrival;
  private int autoDepart;
  private String stopType;

  public TrainMovement(String trainCode, String trainDate, String locationCode,
      String locationFullName, int locationOrder, String locationType,
      String trainOrigin, String trainDestination, String scheduledArrival,
      String scheduledDeparture, String arrival, String departure,
      int autoArrival, int autoDepart, String stopType) {
    this.trainCode = trainCode != null ? trainCode : "";
    this.trainDate = trainDate != null ? trainDate : "";
    this.locationCode = locationCode != null ? locationCode : "";
    this.locationFullName = locationFullName != null ? locationFullName : "";
    this.locationOrder = locationOrder;
    this.locationType = locationType != null ? locationType : "";
    this.trainOrigin = trainOrigin != null ? trainOrigin : "";
    this.trainDestination = trainDestination != null ? trainDestination : "";
    this.scheduledArrival = scheduledArrival != null ? scheduledArrival : "";
    this.scheduledDeparture = scheduledDeparture != null ? scheduledDeparture : "";
    this.arrival = arrival != null ? arrival : "";
    this.departure = departure != null ? departure : "";
    this.autoArrival = autoArrival;
    this.autoDepart = autoDepart;
    this.stopType = stopType != null ? stopType : "";
  }

  public String getTrainCode() {
    return trainCode;
  }

  public String getTrainDate() {
    return trainDate;
  }

  public String getLocationCode() {
    return locationCode;
  }

  public String getLocationFullName() {
    return locationFullName;
  }

  public int getLocationOrder() {
    return locationOrder;
  }

  public String getLocationType() {
    return locationType;
  }

  public String getTrainOrigin() {
    return trainOrigin;
  }

  public String getTrainDestination() {
    return trainDestination;
  }

  public String getScheduledArrival() {
    return scheduledArrival;
  }

  public String getScheduledDeparture() {
    return scheduledDeparture;
  }

  public String getArrival() {
    return arrival;
  }

  public String getDeparture() {
    return departure;
  }

  public int getAutoArrival() {
    return autoArrival;
  }

  public int getAutoDepart() {
    return autoDepart;
  }

  public String getStopType() {
    return stopType;
  }

  public boolean isCurrent() {
    return stopType.equals("C");
  }

  public boolean isNext() {
    return stopType.equals("N");
  }

  public boolean isOrigin() {
    return locationType.equals("O");
  }

  public boolean isDestination() {
    return locationType.equals("D");
  }

  public boolean isStop() {
    return locationType.equals("S");
  }

  public boolean isTimingPoint() {
    return locationType.equals("T");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    String typeSymbol;
    if (isOrigin()) {
      typeSymbol = "[ORIGIN]";
    } else if (isDestination()) {
      typeSymbol = "[DESTINATION]";
    } else if (isTimingPoint()) {
      typeSymbol = "[TIMING]";
    } else {
      typeSymbol = "[STOP]";
    }

    String statusSymbol = "";
    if (isCurrent()) {
      statusSymbol = " << CURRENT";
    } else if (isNext()) {
      statusSymbol = " >> NEXT";
    }

    sb.append(String.format("%2d. %-30s %s%s",
        locationOrder, locationFullName, typeSymbol, statusSymbol));

    if (!scheduledArrival.isEmpty() && !scheduledArrival.equals("00:00:00")) {
      sb.append("\n    Scheduled Arrival:   " + scheduledArrival);
      if (!arrival.isEmpty() && !arrival.equals("00:00:00")) {
        sb.append("  |  Actual: " + arrival);
        if (!scheduledArrival.equals(arrival)) {
          sb.append(" (DELAYED)");
        }
      }
    }

    if (!scheduledDeparture.isEmpty() && !scheduledDeparture.equals("00:00:00")) {
      sb.append("\n    Scheduled Departure: " + scheduledDeparture);
      if (!departure.isEmpty() && !departure.equals("00:00:00")) {
        sb.append("  |  Actual: " + departure);
        if (!scheduledDeparture.equals(departure)) {
          sb.append(" (DELAYED)");
        }
      }
    }

    return sb.toString();
  }
}