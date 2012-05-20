package scribe.simulator.snapshot;

import common.peer.PeerAddress;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class Snapshot {
	private static TreeMap<PeerAddress, PeerInfo> peers = new TreeMap<PeerAddress, PeerInfo>();
	private static HashMap<BigInteger, ArrayList<PeerAddress>> subscriptions = new HashMap<BigInteger, ArrayList<PeerAddress>>();
	private static ArrayList<BigInteger> publishedEvents = new ArrayList<BigInteger>();
	private static int counter = 0;
	private static String FILENAME = "scribe.out";
	private static DecimalFormat df = new DecimalFormat("#.###");

//-------------------------------------------------------------------
	public static void init(int numOfStripes) {
		FileIO.write("", FILENAME);
	}

//-------------------------------------------------------------------
	public static void addPeer(PeerAddress peer, ArrayList<BigInteger> topics) {
		peers.put(peer, new PeerInfo(topics));
		ArrayList<PeerAddress> nodes;
		
		for (BigInteger topic : topics) {
			nodes = null;
			nodes = subscriptions.get(topic);
			if (nodes == null)
				nodes = new ArrayList<PeerAddress>();
			
			nodes.add(peer);
			subscriptions.put(topic, nodes);
		}
	}

//-------------------------------------------------------------------
	public static void removePeer(PeerAddress address) {
		peers.remove(address);
	}

//-------------------------------------------------------------------
	public static void publishTopicEvent(BigInteger event) {
		publishedEvents.add(event);
	}

//-------------------------------------------------------------------
	public static void updateRecvTopicEvent(PeerAddress address, BigInteger event) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
		peerInfo.updateRecvTopicEvents(event);	
	}

//-------------------------------------------------------------------
	public static void updateSuccPred(PeerAddress address, PeerAddress succ, PeerAddress pred) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
		peerInfo.updateSuccPred(succ, pred);
	}

//-------------------------------------------------------------------
	public static void updateTManPartners(PeerAddress address, ArrayList<PeerAddress> partners) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
		peerInfo.updateTManPartners(partners);
	}
	
//-------------------------------------------------------------------
	public static void updateCyclonPartners(PeerAddress address, ArrayList<PeerAddress> partners) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
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
		str += verifyPublish();
		str += reportSpam();
		str += reportDetailes();
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
				if (peer.getSucc() == null || !peer.getSucc().equals(peersList[0]))
					count++;
			} else if (peer.getSucc() == null || !peer.getSucc().equals(peersList[i + 1]))
				count++;			
		}
		
		if (count == 0)
			str += "ring is correct :)\n";
		else
			str += count + " successor link(s) in ring are wrong :(\n";
		
		
		count = 0;
		for (int i = 0; i < peersList.length; i++) {
			PeerInfo peer = peers.get(peersList[i]);
			
			if (i == 0) {
				if (peer.getPred() == null || !peer.getPred().equals(peersList[peersList.length - 1]))
					count++;
			} else {
				if (peer.getPred() == null || !peer.getPred().equals(peersList[i - 1]))
					count++;
			}
		}
		
		if (count == 0)
			str += "reverse ring is correct :)\n";
		else
			str += count + " predecessor link(s) in ring are wrong :(\n";
		
		return str;
	}

//-------------------------------------------------------------------
	private static String verifyPublish() {
		PeerInfo peerInfo;
		int hit = 0;
		String str = new String("---\n");

		ArrayList<PeerAddress> nodes;
		for (BigInteger event : publishedEvents) {
			hit = 0;
			nodes = subscriptions.get(event);

			for (PeerAddress peer : nodes) {
				peerInfo = peers.get(peer);
			
				if (peerInfo.getReceivedTopicEvents().contains(event))
					hit++;
			}
			
			str += "topic event " + event + " --> hit ratio: " + df.format(((double)hit / (double)nodes.size()) * 100) + "%\n";		
		}
		
		return str;
	}

//-------------------------------------------------------------------
	private static String reportSpam() {
		PeerInfo peerInfo;
		int spam = 0;
		double spamRatio = 0;
		Integer count;
		TreeMap<Double, Integer> spamList = new TreeMap<Double, Integer>();
		String str = new String("---\n");
		

		ArrayList<BigInteger> receivedEvents;
		ArrayList<BigInteger> subscribes;
		for (PeerAddress peer : peers.keySet()) {
			spam = 0;
			peerInfo = peers.get(peer);
			
			subscribes = peerInfo.getTopics();
			receivedEvents = peerInfo.getReceivedTopicEvents();
			
			for (BigInteger event : receivedEvents) {
				if (!subscribes.contains(event))
					spam++;
			}

			if (receivedEvents.size() == 0)
				spamRatio = 0;
			else
				spamRatio = (double)spam / (double)receivedEvents.size();
			count = spamList.get(spamRatio);
			if (count == null)
				count = 1;
			else
				count++;
			
			spamList.put(spamRatio, count);
		}

		str += "spam%: # of nodes\n";
		for (Double s : spamList.keySet())
			str += df.format(s * 100) + "%" + ": " + spamList.get(s) + "\n";
		
		return str;
	}


}
