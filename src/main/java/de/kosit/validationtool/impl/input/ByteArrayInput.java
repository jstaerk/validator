package de.kosit.validationtool.impl.input;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Classical in-memory {@link de.kosit.validationtool.api.Input}. It is not memory efficient to read the whole file into
 * memory prio validating. Consider using the {@link ResourceInput}.
 * 
 * @author Andreas Penski
 */
@Getter
@AllArgsConstructor()
public class ByteArrayInput extends AbstractInput {

    private final byte[] content;

    private final String name;

    private final String digestAlgorithm;

    public int getLength() {
        return this.content != null ? this.content.length : 0;
    }

    @Override
    public InputStream openStream() {
        return new ByteArrayInputStream(this.content);
    }
}
