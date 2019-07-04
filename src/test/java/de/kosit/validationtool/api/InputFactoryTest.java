/*
 * Licensed to the Koordinierungsstelle für IT-Standards (KoSIT) under
 * one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  KoSIT licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.kosit.validationtool.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.kosit.validationtool.impl.ConversionServiceTest;

/**
 * Testet den Hashcode-Service.
 * 
 * @author Andreas Penski
 */
public class InputFactoryTest {

    private static final URL CONTENT = ConversionServiceTest.class.getResource("/valid/scenarios.xml");

    private static final URL OTHER_CONTENT = ConversionServiceTest.class.getResource("/valid/report.xml");

    public static final String SOME_VALUE = "some value";

    private static URL NOT_EXISTING;

    private static final int EOF = -1;

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    static {
        try {
            NOT_EXISTING = new URL("file://localhost/somefile.text");
        } catch (final MalformedURLException e) {
            // just ignore;
        }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDefaultDigestAlgorithm() {
        assertThat(new InputFactory().getAlgorithm()).isEqualTo(InputFactory.DEFAULT_ALGORITH);
        assertThat(new InputFactory("").getAlgorithm()).isEqualTo(InputFactory.DEFAULT_ALGORITH);
    }

    @Test
    public void testHashCodeGeneration() {
        final byte[] s1 = drain(InputFactory.read(CONTENT)).getHashCode();
        final byte[] s2 = drain(InputFactory.read(CONTENT)).getHashCode();
        final byte[] s3 = drain(InputFactory.read(OTHER_CONTENT)).getHashCode();
        assertThat(s1).isNotEmpty();
        assertThat(s1).isEqualTo(s2);
        assertThat(s3).isNotEmpty();
        assertThat(s1).isNotEqualTo(s3);
    }

    private static Input drain(final Input input) {
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        try ( final InputStream stream = input.openStream() ) {
            int n;
            while (EOF != (n = stream.read(buffer))) {
                // nothing
            }

        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        return input;

    }

    @Test
    public void testWrongAlgorithm() {
        this.expectedException.expect(IllegalArgumentException.class);
        new InputFactory("unknown");
    }

    @Test
    public void testNullInputURL() {
        this.expectedException.expect(IllegalArgumentException.class);
        InputFactory.read((URL) null);
    }

    @Test
    public void testInputByte() {
        final Input input = InputFactory.read(SOME_VALUE.getBytes(), SOME_VALUE);
        assertThat(input).isNotNull();
    }

    @Test
    public void testInputStream() {
        final Input input = InputFactory.read(new ByteArrayInputStream(SOME_VALUE.getBytes()), SOME_VALUE);
        assertThat(input).isNotNull();
    }

    @Test
    public void testNullStream() {
        this.expectedException.expect(IllegalArgumentException.class);
        final Input input = InputFactory.read((InputStream)null, SOME_VALUE);
    }

    @Test
    public void testInputFile() throws URISyntaxException {
        final Input input = InputFactory.read(new File(CONTENT.toURI()));
        assertThat(input).isNotNull();
    }

    @Test
    public void testInputPath() throws URISyntaxException {
        final Input input = InputFactory.read(Paths.get(CONTENT.toURI()));
        assertThat(input).isNotNull();
    }

    @Test
    public void testNullInput() {
        this.expectedException.expect(IllegalArgumentException.class);
        InputFactory.read((byte[]) null, SOME_VALUE);
    }

    @Test
    public void testNullInputName() {
        this.expectedException.expect(IllegalArgumentException.class);
        InputFactory.read(SOME_VALUE.getBytes(), null);
    }

    @Test
    public void testEmptyInputName() {
        this.expectedException.expect(IllegalArgumentException.class);
        InputFactory.read(SOME_VALUE.getBytes(), "");
    }

    @Test
    public void testUnexistingInput() {
        this.expectedException.expect(IllegalArgumentException.class);
        InputFactory.read(NOT_EXISTING);
    }


}
