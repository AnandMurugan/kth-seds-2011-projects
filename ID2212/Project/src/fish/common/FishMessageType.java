/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.common;

/**
 *
 * @author Igor
 */
public enum FishMessageType {
    /**
     * Request to download file
     */
    PEER_DOWNLOAD,
    /**
     * OK message
     */
    PEER_OK,
    /**
     * ERROR message
     */
    PEER_ERROR,
    /**
     * Request to get all shared files
     */
    PEER_FIND_ALL,
    /**
     * Request to get all shared file by filename mask
     */
    PEER_FIND,
    /**
     * Request to server to share files
     */
    CLIENT_SHARE,
    /**
     * Request to server to unshare files
     */
    CLIENT_UNSHARE,
    /**
     * Request to get all shared files
     */
    CLIENT_FIND_ALL,
    /**
     * Request to get all shared file by filename mask
     */
    CLIENT_FIND,
    /**
     * ALIVE response
     */
    CLIENT_ALIVE,
    /**
     * OK message
     */
    SERVER_OK,
    /**
     * ERROR message
     */
    SERVER_ERROR,
    /**
     * Request to client if it is alive
     */
    SERVER_ALIVE,
    /**
     * Request to connect to the peer
     */
    PEER_CONNECT,
    /**
     * Request to send peer's neighbour list
     */
    PEER_NEIGHBOURS,
    /**
     * Request to send peer's address for communicating
     */
    PEER_ADDRESS,
    /**
     * Response to the peer that some files were found
     */
    PEER_FOUND_RESPONSE
}
