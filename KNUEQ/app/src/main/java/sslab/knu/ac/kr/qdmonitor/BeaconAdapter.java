package sslab.knu.ac.kr.qdmonitor;
import java.util.Vector;

public class BeaconAdapter {
    private Vector<Beacon> beacons;

    public BeaconAdapter(Vector<Beacon> beacons) {
        this.beacons = beacons;
    }

    public int getCount() {
        return beacons.size();
    }

    public Object getItem(int position) {
        return beacons.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }
}
