package aggregation.simulator.snapshot;

import java.util.ArrayList;
import java.util.HashMap;

import common.peer.PeerAddress;
import java.util.Map;

public class Snapshot {
    private static HashMap<PeerAddress, PeerInfo> peers = new HashMap<PeerAddress, PeerInfo>();
    private static int counter = 0;
    private static String FILENAME = "aggregation.out";

//-------------------------------------------------------------------
    public static void init(int numOfStripes) {
        FileIO.write("", FILENAME);
    }

//-------------------------------------------------------------------
    public static void addPeer(PeerAddress address) {
        peers.put(address, new PeerInfo());
    }

//-------------------------------------------------------------------
    public static void removePeer(PeerAddress address) {
        peers.remove(address);
    }

//-------------------------------------------------------------------
    public static void updateNum(PeerAddress address, double num) {
        PeerInfo peerInfo = peers.get(address);
        
        if (peerInfo == null) {
            return;
        }
        
        peerInfo.updateNum(num);
    }

//-------------------------------------------------------------------
    public static void updateCyclonPartners(PeerAddress address, ArrayList<PeerAddress> partners) {
        PeerInfo peerInfo = peers.get(address);
        
        if (peerInfo == null) {
            return;
        }
        
        peerInfo.updateCyclonPartners(partners);
    }

//-------------------------------------------------------------------
    public static void report() {
        String str = new String();
        str += "current time: " + counter++ + "\n";
        str += reportNetworkState();
        str += verifyAverageBandwidth();
        //str += reportDetailes();
        str += "###\n";
        
        System.out.println(str);
        FileIO.append(str, FILENAME);
    }

//-------------------------------------------------------------------
    private static String reportNetworkState() {
        String str = new String("---\n");
        int totalNumOfPeers = peers.size();
        str += "total number of peers: " + totalNumOfPeers + "\n";
        
        return str;
    }

//-------------------------------------------------------------------
    private static String reportDetailes() {
        PeerInfo peerInfo;
        String str = new String("---\n");
        
        for (PeerAddress peer : peers.keySet()) {
            peerInfo = peers.get(peer);
            
            str += "peer: " + peer;
            str += ", cyclon parters: " + peerInfo.getCyclonPartners();
            str += "\n";
        }
        
        return str;
    }

//-------------------------------------------------------------------
    private static String verifyAverageBandwidth() {
        int correct = 0;
        double average = 0.0;
        double averageSqr = 0.0;
        String str = new String("---\n");
        
        for (PeerInfo info : peers.values()) {
            average += info.getNum();
            averageSqr += Math.pow(info.getNum(), 2.0);
        }
        average /= peers.size();
        averageSqr /= peers.size();
        
        for (PeerAddress peer : peers.keySet()) {
            PeerInfo peerInfo = peers.get(peer);
            double estimated = peerInfo.getNum();
            str += peer + " --> estimated bandwidth: " + estimated + "\n";
            if (Math.abs(estimated - average) <= average * 0.02) {
                correct++;
            }
        }
        
        str += "estimated correctly: " + correct + "\n";
        str += "average: " + average + "\n";
        str += "standard deviation: " + Math.sqrt(averageSqr - average * average) + "\n";
        return str;
    }
}
