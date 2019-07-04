package de.kosit.validationtool.impl.input;

import java.io.InputStream;

/**
 * Internal interface used for lazy generation of the hashcode for document identification.
 * 
 * @see HashCodeHelper#wrap(HasHashCode, InputStream, String) for details
 * @author Andreas Penski
 */
interface HasHashCode {

    /**
     * Sets a hashcode
     * 
     * @param digest the digest
     */
    void setHashCode(byte[] digest);

    /**
     * Determines whether a hashcode has been computed yet
     * 
     * @return true when computed
     */
    boolean isHashcodeComputed();
}
