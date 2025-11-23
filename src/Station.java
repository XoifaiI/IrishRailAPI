public class Station {
  private String stationDesc;
  private String stationCode;
  private String stationId;
  private double latitude;
  private double longitude;

  public Station(String desc, String code, String id, double lat, double lon) {
    this.stationDesc = desc != null ? desc : "";
    this.stationCode = code != null ? code : "";
    this.stationId = id != null ? id : "";
    this.latitude = lat;
    this.longitude = lon;
  }

  public String getStationDesc() {
    return stationDesc;
  }

  public String getStationCode() {
    return stationCode;
  }

  public String getStationId() {
    return stationId;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  @Override
  public String toString() {
    if (latitude == 0.0 && longitude == 0.0) {
      return String.format("%-30s [%s]", stationDesc, stationCode);
    }
    return String.format("%-30s [%s] (%.4f, %.4f)",
        stationDesc, stationCode, latitude, longitude);
  }
}