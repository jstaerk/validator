package de.kosit.validationtool.impl.input;

import lombok.extern.slf4j.Slf4j;

import de.kosit.validationtool.api.Input;

/**
 * Base class for all {@link Input Inputs}.
 * 
 * @author Andreas Penski
 */
@Slf4j
abstract class AbstractInput implements Input, HasHashCode {

    private byte[] hashCode;

    @Override
    public byte[] getHashCode() {
        if (this.hashCode == null) {
            throw new IllegalStateException("Hashcode is not computed yet");
        }
        return this.hashCode;
    }

    @Override
    public boolean isHashcodeComputed() {
        return this.hashCode != null;
    }

    @Override
    public void setHashCode(final byte[] digest) {
        this.hashCode = digest;
    }
}
