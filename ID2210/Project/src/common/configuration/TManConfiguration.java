package common.configuration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
import java.util.Properties;

public final class TManConfiguration {
    private final long period;
    private final BigInteger identifierSpaceSize;

//-------------------------------------------------------------------
    public TManConfiguration(long period, BigInteger identifierSpaceSize) {
        super();
        this.period = period;
        this.identifierSpaceSize = identifierSpaceSize;
    }

//-------------------------------------------------------------------
    public long getPeriod() {
        return this.period;
    }

    public BigInteger getIdentifierSpaceSize() {
        return identifierSpaceSize;
    }
//-------------------------------------------------------------------

    public void store(String file) throws IOException {
        Properties p = new Properties();
        p.setProperty("period", "" + period);
        p.setProperty("identifierSpaceSize", "" + identifierSpaceSize);

        Writer writer = new FileWriter(file);
        p.store(writer, "se.sics.kompics.p2p.overlay.application");
    }

//-------------------------------------------------------------------
    public static TManConfiguration load(String file) throws IOException {
        Properties p = new Properties();
        Reader reader = new FileReader(file);
        p.load(reader);

        long period = Long.parseLong(p.getProperty("period"));
        BigInteger n = new BigInteger(p.getProperty("identifierSpaceSize"));

        return new TManConfiguration(period, n);
    }
}
