/**
 * Copyright 2009-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.jsonunit;

import net.javacrumbs.jsonunit.core.internal.Diff;

import java.math.BigDecimal;

import static net.javacrumbs.jsonunit.core.internal.Diff.create;

/**
 * Assertions for comparing JSON. The comparison ignores white-spaces and order of nodes.
 * <p/>
 * All the methods accept Objects as parameters. The supported types are:
 * <ol>
 * <li>Jackson JsonNode</li>
 * <li>Numbers, booleans and any other type parseable by Jackson's ObjectMapper.convertValue</li>
 * <li>String is parsed as JSON. For expected values the string is quoted if it contains obviously invalid JSON.</li>
 * <li>{@link java.io.Reader} similarly to String</li>
 * <li>null as null Node</li>
 * </ol>
 *
 * @author Lukas Krecan
 */
public class JsonAssert {
    private static final String FULL_JSON = "fullJson";
    private static final String ACTUAL = "actual";
    private static String ignorePlaceholder = "${json-unit.ignore}";
    private static BigDecimal numericComparisonTolerance = null;

    private JsonAssert() {
        //nothing
    }

    /**
     * Compares to JSON documents. Throws {@link AssertionError} if they are different.
     *
     * @param expected
     * @param actual
     */
    public static void assertJsonEquals(Object expected, Object actual) {
        assertJsonPartEquals(expected, actual, "");
    }

    /**
     * Compares part of the JSON. Path has this format "root.array[0].value".
     *
     * @param expected
     * @param fullJson
     * @param path
     */
    public static void assertJsonPartEquals(Object expected, Object fullJson, String path) {
        Diff diff = create(expected, fullJson, FULL_JSON, path, ignorePlaceholder, numericComparisonTolerance);
        if (!diff.similar()) {
            doFail(diff.toString());
        }
    }

    /**
     * Compares structures of two JSON documents.
     * Throws {@link AssertionError} if they are different.
     *
     * @param expected
     * @param actual
     */
    public static void assertJsonStructureEquals(Object expected, Object actual) {
        Diff diff = create(expected, actual, ACTUAL, "", ignorePlaceholder, numericComparisonTolerance);
        if (!diff.similarStructure()) {
            doFail(diff.structureDifferences());
        }
    }

    /**
     * Compares structure of part of the JSON. Path has this format "root.array[0].value".
     *
     * @param expected
     * @param fullJson
     * @param path
     */
    public static void assertJsonPartStructureEquals(Object expected, Object fullJson, String path) {
        Diff diff = create(expected, fullJson, FULL_JSON, path, ignorePlaceholder, numericComparisonTolerance);
        if (!diff.similarStructure()) {
            doFail(diff.structureDifferences());
        }
    }

    /**
     * Fails a test with the given message.
     */
    private static void doFail(String diffMessage) {
        throw new AssertionError(diffMessage);
    }

    /**
     * Set's string that will be ignored in comparison. Default value is "${json-unit.ignore}"
     *
     * @param ignorePlaceholder
     */
    public static void setIgnorePlaceholder(String ignorePlaceholder) {
        JsonAssert.ignorePlaceholder = ignorePlaceholder;
    }

    public static String getIgnorePlaceholder() {
        return ignorePlaceholder;
    }

    /**
     * Sets the tolerance for floating number comparison. If set to null, requires exact match of the values.
     * For example, if set to 0.01, ignores all differences lower than 0.01, so 1 and 0.9999 are considered equal.
     *
     * @param numericComparisonTolerance
     */
    public static void setTolerance(BigDecimal numericComparisonTolerance) {
        JsonAssert.numericComparisonTolerance = numericComparisonTolerance;
    }

    /**
     * Sets the tolerance for floating number comparison. If set to null, requires exact match of the values.
     * For example, if set to 0.01, ignores all differences lower than 0.01, so 1 and 0.9999 are considered equal.
     *
     * @param numberComparisonTolerance
     */
    public static void setTolerance(double numberComparisonTolerance) {
        JsonAssert.numericComparisonTolerance = BigDecimal.valueOf(numberComparisonTolerance);
    }

    public static BigDecimal getTolerance() {
        return numericComparisonTolerance;
    }
}
