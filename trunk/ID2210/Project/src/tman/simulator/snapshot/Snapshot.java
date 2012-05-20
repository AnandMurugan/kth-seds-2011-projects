package tman.simulator.snapshot;

import common.peer.PeerAddress;
import java.util.ArrayList;
import java.util.TreeMap;

public class Snapshot {
    private static TreeMap<PeerAddress, PeerInfo> peers = new TreeMap<PeerAddress, PeerInfo>();
    private static int counter = 0;
    private static String FILENAME = "tman.out";

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
    public static void updateSuccPred(PeerAddress address, PeerAddress succ, PeerAddress pred) {
        PeerInfo peerInfo = peers.get(address);

        if (peerInfo == null) {
            return;
        }

        peerInfo.updateSuccPred(succ, pred);
    }

//-------------------------------------------------------------------
    public static void updateTManPartners(PeerAddress address, ArrayList<PeerAddress> partners) {
        PeerInfo peerInfo = peers.get(address);

        if (peerInfo == null) {
            return;
        }

        peerInfo.updateTManPartners(partners);
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
        PeerAddress[] peersList = new PeerAddress[peers.size()];
        peers.keySet().toArray(peersList);

        String str = new String();
        str += "current time: " + counter++ + "\n";
        str += reportNetworkState();
        str += "ring: " + verifyRing(peersList) + "\n";
        str += reportDetailes();
        str += "###\n";

        System.out.println(str);
        FileIO.append(str, FILENAME);
    }

//-------------------------------------------------------------------
    private static String reportNetworkState() {
        String str = new String("---\n");
        int totalNumOfPeers = peers.size() - 1;
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
            str += ", tman parters: " + peerInfo.getTManPartners();
            str += "\n";
        }

        return str;
    }

//-------------------------------------------------------------------
    private static String verifyRing(PeerAddress[] peersList) {
        int count = 0;
        String str = new String("---\n");

        for (int i = 0; i < peersList.length; i++) {
            PeerInfo peer = peers.get(peersList[i]);

            if (i == peersList.length - 1) {
                if (peer.getSucc() == null || !peer.getSucc().equals(peersList[0])) {
                    count++;
                }
            } else if (peer.getSucc() == null || !peer.getSucc().equals(peersList[i + 1])) {
                count++;
            }
        }

        if (count == 0) {
            str += "ring is correct :)\n";
        } else {
            str += count + " successor link(s) in ring are wrong :(\n";
        }

        count = 0;
        for (int i = 0; i < peersList.length; i++) {
            PeerInfo peer = peers.get(peersList[i]);

            if (i == 0) {
                if (peer.getPred() == null || !peer.getPred().equals(peersList[peersList.length - 1])) {
                    count++;
                }
            } else {
                if (peer.getPred() == null || !peer.getPred().equals(peersList[i - 1])) {
                    count++;
                }
            }
        }

        if (count == 0) {
            str += "reverse ring is correct :)\n";
        } else {
            str += count + " predecessor link(s) in ring are wrong :(\n";
        }

        return str;
    }
}
