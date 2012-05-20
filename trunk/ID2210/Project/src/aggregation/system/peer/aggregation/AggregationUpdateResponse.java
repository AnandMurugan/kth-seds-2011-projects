/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation.system.peer.aggregation;

import common.peer.PeerAddress;
import common.peer.PeerMessage;

/**
 *
 * @author Igor
 */
public class AggregationUpdateResponse extends PeerMessage {
    private double num;

    public AggregationUpdateResponse(double num, PeerAddress source, PeerAddress destination) {
        super(source, destination);
        this.num = num;
    }

    public double getNum() {
        return num;
    }
}
