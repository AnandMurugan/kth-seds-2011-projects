package scribe.system.peer.scribe;

import common.peer.PeerAddress;
import java.math.BigInteger;
import java.util.ArrayList;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timer;

import scribe.simulator.snapshot.Snapshot;
import tman.system.peer.tman.TManPartnersPort;
import tman.system.peer.tman.TManPartnersRequest;
import tman.system.peer.tman.TManPartnersResponse;

public final class Scribe extends ComponentDefinition {
	Negative<ScribePort> scribePort = negative(ScribePort.class);
	Positive<TManPartnersPort> tmanPartnersPort = positive(TManPartnersPort.class);
	Positive<Network> networkPort = positive(Network.class);
	Positive<Timer> timerPort = positive(Timer.class);

	private long period;
	private PeerAddress self;
	private ArrayList<BigInteger> topics;
	private ArrayList<PeerAddress> tmanPartners;

//-------------------------------------------------------------------	
	public Scribe() {
		topics = new ArrayList<BigInteger>();
		tmanPartners = new ArrayList<PeerAddress>();
		
		subscribe(handleInit, control);
		subscribe(handleRequestTManPartners, timerPort);
		subscribe(handleRecvTopicEvent, networkPort);
		subscribe(handleRecvTManPartners, tmanPartnersPort);
		subscribe(handlePublish, scribePort);
	}

//-------------------------------------------------------------------	
	Handler<ScribeInit> handleInit = new Handler<ScribeInit>() {
		public void handle(ScribeInit init) {
			self = init.getSelf();
			topics = init.getTopics();
			period = init.getPeriod();
			
			SchedulePeriodicTimeout rst = new SchedulePeriodicTimeout(period, period);
			rst.setTimeoutEvent(new ScribeSchedule(rst));
			trigger(rst, timerPort);
		}
	};

//-------------------------------------------------------------------	
	Handler<PublishEvent> handlePublish = new Handler<PublishEvent>() {
		public void handle(PublishEvent event) {
			BigInteger topicId = event.getTopicId();
			Snapshot.publishTopicEvent(topicId);
		}
	};

//-------------------------------------------------------------------	
	Handler<TopicEvent> handleRecvTopicEvent = new Handler<TopicEvent>() {
		public void handle(TopicEvent event) {
			BigInteger topicId = event.getTopicId();
			Snapshot.updateRecvTopicEvent(self, topicId);
		}
	};

//-------------------------------------------------------------------	
	Handler<ScribeSchedule> handleRequestTManPartners = new Handler<ScribeSchedule>() {
		public void handle(ScribeSchedule event) {
			TManPartnersRequest request = new TManPartnersRequest();
			trigger(request, tmanPartnersPort);
		}
	};

//-------------------------------------------------------------------	
	Handler<TManPartnersResponse> handleRecvTManPartners = new Handler<TManPartnersResponse>() {
		public void handle(TManPartnersResponse event) {
			tmanPartners = event.getPartners();
		}
	};
}
