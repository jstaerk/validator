package de.kosit.validationtool.impl.input;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper for generating the hashcode for document identification.
 * 
 * @author Andreas Penski
 */
public class HashCodeHelper {

    /**
     * Helper class, which generates the hashcode while reading the stream e.g. for parsing the document. This allows
     * generating the hashcode without an aditional reading step.
     */
    private static class DigestingInputStream extends FilterInputStream {

        private final MessageDigest digest;

        private final HasHashCode reference;

        DigestingInputStream(final HasHashCode input, final InputStream in, final MessageDigest digest) {
            super(new DigestInputStream(in, digest));
            this.digest = digest;
            this.reference = input;
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.reference.setHashCode(this.digest.digest());
        }

    }

    private HashCodeHelper() {
        // hide
    }

    public static MessageDigest createDigest(final String algorithm) {
        try {
            final MessageDigest digest;
            digest = MessageDigest.getInstance(algorithm);
            return digest;
        } catch (final NoSuchAlgorithmException e) {
            // should not happen
            throw new IllegalArgumentException(String.format("Specified method %s is not available", algorithm), e);
        }
    }

    public static InputStream wrap(final HasHashCode input, final InputStream strea, final String digestAlgorithm) {
        return new DigestingInputStream(input, strea, createDigest(digestAlgorithm));
    }
}
