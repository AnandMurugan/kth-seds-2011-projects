package tman.system.peer.tman;

import common.configuration.TManConfiguration;
import common.peer.PeerAddress;
import java.util.ArrayList;

import cyclon.system.peer.cyclon.CyclonPartnersPort;
import cyclon.system.peer.cyclon.CyclonPartnersRequest;
import cyclon.system.peer.cyclon.CyclonPartnersResponse;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timer;

import tman.simulator.snapshot.Snapshot;

public final class TMan extends ComponentDefinition {
	Negative<TManPartnersPort> tmanPartnersPort = negative(TManPartnersPort.class);
	Positive<CyclonPartnersPort> cyclonPartnersPort = positive(CyclonPartnersPort.class);
	Positive<Network> networkPort = positive(Network.class);
	Positive<Timer> timerPort = positive(Timer.class);

	private long period;
	private PeerAddress self;
	private PeerAddress succ;
	private PeerAddress pred;
	private ArrayList<PeerAddress> tmanPartners;
	private ArrayList<PeerAddress> cyclonPartners;
	private TManConfiguration tmanConfiguration;

//-------------------------------------------------------------------	
	public TMan() {
		tmanPartners = new ArrayList<PeerAddress>();
		cyclonPartners = new ArrayList<PeerAddress>();
		
		subscribe(handleInit, control);
		subscribe(handleRound, timerPort);
		subscribe(handleCyclonPartnersResponse, cyclonPartnersPort);
		subscribe(handleTManPartnersRequest, tmanPartnersPort);
	}

//-------------------------------------------------------------------	
	Handler<TManInit> handleInit = new Handler<TManInit>() {
		public void handle(TManInit init) {
			self = init.getSelf();
			tmanConfiguration = init.getConfiguration();
			period = tmanConfiguration.getPeriod();
			succ = self;
			pred = self;
			
			SchedulePeriodicTimeout rst = new SchedulePeriodicTimeout(period, period);
			rst.setTimeoutEvent(new TManSchedule(rst));
			trigger(rst, timerPort);

		}
	};

//-------------------------------------------------------------------	
	Handler<TManSchedule> handleRound = new Handler<TManSchedule>() {
		public void handle(TManSchedule event) {
			CyclonPartnersRequest response = new CyclonPartnersRequest();
			trigger(response, cyclonPartnersPort);
			
		}
	};
    
        
//-------------------------------------------------------------------	
	Handler<CyclonPartnersResponse> handleCyclonPartnersResponse = new Handler<CyclonPartnersResponse>() {
		public void handle(CyclonPartnersResponse event) {
			cyclonPartners = event.getPartners();
			System.out.println("tman: " + self + " --> " + cyclonPartners);

			Snapshot.updateTManPartners(self, tmanPartners);
			Snapshot.updateSuccPred(self, succ, pred);
                        
                        
		}
	};
	
//-------------------------------------------------------------------	
	Handler<TManPartnersRequest> handleTManPartnersRequest = new Handler<TManPartnersRequest>() {
		public void handle(TManPartnersRequest event) {
			TManPartnersResponse response = new TManPartnersResponse(tmanPartners);
			trigger(response, tmanPartnersPort);
		}
	};
}
