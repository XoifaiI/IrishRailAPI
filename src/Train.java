public class Train {
  private String trainCode;
  private String status;
  private double latitude;
  private double longitude;
  private String direction;
  private String publicMessage;

  public Train(String code, String status, double lat, double lon,
      String direction, String message) {
    this.trainCode = code != null ? code : "Unknown";
    this.status = status != null ? status : "N";
    this.latitude = lat;
    this.longitude = lon;
    this.direction = direction != null ? direction : "Unknown";
    this.publicMessage = message != null ? message : "No information available";
  }

  public String getTrainCode() {
    return trainCode;
  }

  public String getStatus() {
    return status;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public String getDirection() {
    return direction;
  }

  public String getPublicMessage() {
    return publicMessage;
  }

  @Override
  public String toString() {
    String statusText = status.equals("R") ? "Running" : "Not Running";
    String formattedMessage = formatMessage(publicMessage);
    return String.format("%s - %s (%s)\n  %s",
        trainCode, statusText, direction, formattedMessage);
  }

  private String formatMessage(String message) {
    if (message == null || message.isEmpty()) {
      return "No information available";
    }

    String formatted = message.replace("\\n", "\n  ");

    if (formatted.contains("mins late)")) {
      int startIdx = formatted.lastIndexOf("(");
      int endIdx = formatted.indexOf(" mins late)", startIdx);
      if (startIdx != -1 && endIdx != -1) {
        try {
          String minsStr = formatted.substring(startIdx + 1, endIdx).trim();
          int mins = Integer.parseInt(minsStr);
          if (mins <= 0) {
            formatted = formatted.substring(0, startIdx) + "(On time)"
                + formatted.substring(endIdx + 11);
          }
        } catch (NumberFormatException e) {
        }
      }
    }

    return formatted;
  }
}