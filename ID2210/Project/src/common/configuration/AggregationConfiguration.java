package common.configuration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public final class AggregationConfiguration {
	private final long period;

//-------------------------------------------------------------------
	public AggregationConfiguration(long period) {
		super();
		this.period = period;
	}

//-------------------------------------------------------------------
	public long getPeriod() {
		return this.period;
	}
	
//-------------------------------------------------------------------
	public void store(String file) throws IOException {
		Properties p = new Properties();
		p.setProperty("period", "" + period);

		Writer writer = new FileWriter(file);
		p.store(writer, "se.sics.kompics.p2p.overlay.application");
	}

//-------------------------------------------------------------------
	public static AggregationConfiguration load(String file) throws IOException {
		Properties p = new Properties();
		Reader reader = new FileReader(file);
		p.load(reader);

		long period = Long.parseLong(p.getProperty("period"));

		return new AggregationConfiguration(period);
	}
}
