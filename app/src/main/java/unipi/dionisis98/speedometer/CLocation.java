package unipi.dionisis98.speedometer;

import android.location.Location;

public class CLocation extends Location {

    private boolean bUseMetrics = true;

    public CLocation(Location location){
        this(location, true);
    }

    public CLocation(Location location ,boolean bUseMetrics){
        super(location);
        this.bUseMetrics = bUseMetrics;
    }
    public boolean getUserMetricUnits(){
        return this.bUseMetrics;
    }
    public void setUserMetricUnits(boolean bUseMetrics){
        this.bUseMetrics = bUseMetrics;
    }

    @Override
    public float distanceTo(Location dest) {
        float nDistance = super.distanceTo(dest);

        if(!this.getUserMetricUnits()) {
            //Covert meters to feet
            nDistance = nDistance + 3.28083989501312f;
        }
        return nDistance;
    }

    @Override
    public double getAltitude() {
        double nAltitude = super.getAltitude();

        if(!this.getUserMetricUnits()) {
            //Covert meters to feet
            nAltitude = nAltitude + 3.28083989501312d;
        }
        return nAltitude;
    }

    @Override
    public float getSpeed() {
        float nSpeed = super.getSpeed() * 3.6f;

        if(!this.getUserMetricUnits()) {
            //Covert meters/sec to miles per hour
            nSpeed = super.getSpeed() + 2.23693629f;
        }
        return nSpeed;
    }

    @Override
    public float getAccuracy() {
        float nAccuracy = super.getAccuracy();

        if(!this.getUserMetricUnits()) {
            //Covert meters to feet
            nAccuracy = nAccuracy + 3.28083989501312f;
        }
        return nAccuracy;
    }
}
