package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for MultiOutputStream class.
 * 
 * Tests the OutputStream that delegates operations to multiple underlying
 * OutputStreams.
 * 
 * @author Generated Tests
 */
@SuppressWarnings("resource") // Resource leaks in tests are acceptable for simplicity
@DisplayName("MultiOutputStream Tests")
class MultiOutputStreamTest {

    private ByteArrayOutputStream stream1;
    private ByteArrayOutputStream stream2;
    private ByteArrayOutputStream stream3;
    private MultiOutputStream multiOutputStream;

    // Test stream that throws exceptions
    private static class FailingOutputStream extends OutputStream {
        private final String errorMessage;
        private boolean shouldFail;

        public FailingOutputStream(String errorMessage) {
            this.errorMessage = errorMessage;
            this.shouldFail = true;
        }

        public void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        @Override
        public void write(int b) throws IOException {
            if (shouldFail) {
                throw new IOException(errorMessage);
            }
        }

        @Override
        public void flush() throws IOException {
            if (shouldFail) {
                throw new IOException(errorMessage);
            }
        }

        @Override
        public void close() throws IOException {
            if (shouldFail) {
                throw new IOException(errorMessage);
            }
        }
    }

    @BeforeEach
    void setUp() {
        stream1 = new ByteArrayOutputStream();
        stream2 = new ByteArrayOutputStream();
        stream3 = new ByteArrayOutputStream();

        List<OutputStream> streams = Arrays.asList(stream1, stream2, stream3);
        multiOutputStream = new MultiOutputStream(streams);
    }

    private List<OutputStream> getOutputStreamsFromMulti(MultiOutputStream multi) throws Exception {
        Field streamsField = MultiOutputStream.class.getDeclaredField("outputStreams");
        streamsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<OutputStream> streams = (List<OutputStream>) streamsField.get(multi);
        return streams;
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create multi-stream with list of streams")
        void testConstructorWithStreams() throws Exception {
            // When
            List<OutputStream> testStreams = Arrays.asList(stream1, stream2);
            MultiOutputStream testMulti = new MultiOutputStream(testStreams);

            // Then
            assertThat(testMulti).isNotNull();
            assertThat(testMulti).isInstanceOf(OutputStream.class);

            List<OutputStream> storedStreams = getOutputStreamsFromMulti(testMulti);
            assertThat(storedStreams).isSameAs(testStreams);
        }

        @Test
        @DisplayName("Should create multi-stream with empty list")
        void testConstructorWithEmptyList() throws Exception {
            // When
            List<OutputStream> emptyStreams = new ArrayList<>();
            MultiOutputStream emptyMulti = new MultiOutputStream(emptyStreams);

            // Then
            assertThat(emptyMulti).isNotNull();
            List<OutputStream> storedStreams = getOutputStreamsFromMulti(emptyMulti);
            assertThat(storedStreams).isSameAs(emptyStreams);
        }

        @Test
        @DisplayName("Should create multi-stream with single stream")
        void testConstructorWithSingleStream() throws Exception {
            // When
            List<OutputStream> singleStream = Arrays.asList(stream1);
            MultiOutputStream singleMulti = new MultiOutputStream(singleStream);

            // Then
            assertThat(singleMulti).isNotNull();
            List<OutputStream> storedStreams = getOutputStreamsFromMulti(singleMulti);
            assertThat(storedStreams).hasSize(1);
            assertThat(storedStreams.get(0)).isSameAs(stream1);
        }

        @Test
        @DisplayName("Should create multi-stream with many streams")
        void testConstructorWithManyStreams() throws Exception {
            // Given
            List<OutputStream> manyStreams = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                manyStreams.add(new ByteArrayOutputStream());
            }

            // When
            MultiOutputStream manyMulti = new MultiOutputStream(manyStreams);

            // Then
            List<OutputStream> storedStreams = getOutputStreamsFromMulti(manyMulti);
            assertThat(storedStreams).hasSize(10);
            assertThat(storedStreams).isSameAs(manyStreams);
        }

        @Test
        @DisplayName("Should store reference to original list")
        void testConstructorStoresReference() throws Exception {
            // Given
            List<OutputStream> modifiableList = new ArrayList<>(Arrays.asList(stream1, stream2));
            MultiOutputStream testMulti = new MultiOutputStream(modifiableList);

            // When
            modifiableList.add(stream3);

            // Then
            List<OutputStream> storedStreams = getOutputStreamsFromMulti(testMulti);
            assertThat(storedStreams).hasSize(3); // Should reflect the modification
            assertThat(storedStreams).contains(stream3);
        }
    }

    @Nested
    @DisplayName("Write Method Tests")
    class WriteMethodTests {

        @Test
        @DisplayName("Should write to all streams")
        void testWriteToAllStreams() throws IOException {
            // Given
            int byteValue = 65; // ASCII 'A'

            // When
            multiOutputStream.write(byteValue);

            // Then
            assertThat(stream1.toByteArray()).containsExactly((byte) byteValue);
            assertThat(stream2.toByteArray()).containsExactly((byte) byteValue);
            assertThat(stream3.toByteArray()).containsExactly((byte) byteValue);
        }

        @Test
        @DisplayName("Should write multiple bytes to all streams")
        void testWriteMultipleBytes() throws IOException {
            // Given
            int[] bytes = { 65, 66, 67 }; // ASCII 'A', 'B', 'C'

            // When
            for (int b : bytes) {
                multiOutputStream.write(b);
            }

            // Then
            byte[] expected = { 65, 66, 67 };
            assertThat(stream1.toByteArray()).containsExactly(expected);
            assertThat(stream2.toByteArray()).containsExactly(expected);
            assertThat(stream3.toByteArray()).containsExactly(expected);
        }

        @Test
        @DisplayName("Should write to empty stream list without error")
        void testWriteToEmptyList() throws IOException {
            // Given
            MultiOutputStream emptyMulti = new MultiOutputStream(new ArrayList<>());

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                emptyMulti.write(65);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should write to single stream")
        void testWriteToSingleStream() throws IOException {
            // Given
            MultiOutputStream singleMulti = new MultiOutputStream(Arrays.asList(stream1));
            int byteValue = 72; // ASCII 'H'

            // When
            singleMulti.write(byteValue);

            // Then
            assertThat(stream1.toByteArray()).containsExactly((byte) byteValue);
            assertThat(stream2.toByteArray()).isEmpty(); // Should not be written to
            assertThat(stream3.toByteArray()).isEmpty(); // Should not be written to
        }

        @Test
        @DisplayName("Should handle write failure in one stream")
        void testWriteFailureInOneStream() {
            // Given
            FailingOutputStream failingStream = new FailingOutputStream("Write failed");
            List<OutputStream> mixedStreams = Arrays.asList(stream1, failingStream, stream2);
            MultiOutputStream mixedMulti = new MultiOutputStream(mixedStreams);

            // When/Then
            assertThatThrownBy(() -> {
                mixedMulti.write(65);
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams")
                    .hasMessageContaining("Write failed");

            // Verify successful streams still got the write
            assertThat(stream1.toByteArray()).containsExactly((byte) 65);
            assertThat(stream2.toByteArray()).containsExactly((byte) 65);
        }

        @Test
        @DisplayName("Should handle write failure in multiple streams")
        void testWriteFailureInMultipleStreams() {
            // Given
            FailingOutputStream failingStream1 = new FailingOutputStream("Stream1 failed");
            FailingOutputStream failingStream2 = new FailingOutputStream("Stream2 failed");
            List<OutputStream> failingStreams = Arrays.asList(failingStream1, stream1, failingStream2);
            MultiOutputStream failingMulti = new MultiOutputStream(failingStreams);

            // When/Then
            assertThatThrownBy(() -> {
                failingMulti.write(65);
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams")
                    .hasMessageContaining("Stream1 failed")
                    .hasMessageContaining("Stream2 failed");

            // Verify successful stream still got the write
            assertThat(stream1.toByteArray()).containsExactly((byte) 65);
        }

        @Test
        @DisplayName("Should continue writing to other streams after failure")
        void testContinueWritingAfterFailure() {
            // Given
            FailingOutputStream failingStream = new FailingOutputStream("Middle stream failed");
            List<OutputStream> mixedStreams = Arrays.asList(stream1, failingStream, stream2, stream3);
            MultiOutputStream mixedMulti = new MultiOutputStream(mixedStreams);

            // When
            assertThatThrownBy(() -> {
                mixedMulti.write(88);
            }).isInstanceOf(IOException.class);

            // Then - All non-failing streams should have received the write
            assertThat(stream1.toByteArray()).containsExactly((byte) 88);
            assertThat(stream2.toByteArray()).containsExactly((byte) 88);
            assertThat(stream3.toByteArray()).containsExactly((byte) 88);
        }
    }

    @Nested
    @DisplayName("Flush Method Tests")
    class FlushMethodTests {

        @Test
        @DisplayName("Should flush all streams")
        void testFlushAllStreams() throws IOException {
            // Given
            multiOutputStream.write(65);

            // When
            multiOutputStream.flush();

            // Then - Should complete without error (ByteArrayOutputStream flush is no-op)
            assertThat(stream1.toByteArray()).containsExactly((byte) 65);
            assertThat(stream2.toByteArray()).containsExactly((byte) 65);
            assertThat(stream3.toByteArray()).containsExactly((byte) 65);
        }

        @Test
        @DisplayName("Should flush empty stream list without error")
        void testFlushEmptyList() throws IOException {
            // Given
            MultiOutputStream emptyMulti = new MultiOutputStream(new ArrayList<>());

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                emptyMulti.flush();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle flush failure in one stream")
        void testFlushFailureInOneStream() {
            // Given
            FailingOutputStream failingStream = new FailingOutputStream("Flush failed");
            List<OutputStream> mixedStreams = Arrays.asList(stream1, failingStream, stream2);
            MultiOutputStream mixedMulti = new MultiOutputStream(mixedStreams);

            // When/Then
            assertThatThrownBy(() -> {
                mixedMulti.flush();
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams")
                    .hasMessageContaining("Flush failed");
        }

        @Test
        @DisplayName("Should handle flush failure in multiple streams")
        void testFlushFailureInMultipleStreams() {
            // Given
            FailingOutputStream failingStream1 = new FailingOutputStream("Flush1 failed");
            FailingOutputStream failingStream2 = new FailingOutputStream("Flush2 failed");
            List<OutputStream> failingStreams = Arrays.asList(failingStream1, stream1, failingStream2);
            MultiOutputStream failingMulti = new MultiOutputStream(failingStreams);

            // When/Then
            assertThatThrownBy(() -> {
                failingMulti.flush();
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams")
                    .hasMessageContaining("Flush1 failed")
                    .hasMessageContaining("Flush2 failed");
        }

        @Test
        @DisplayName("Should continue flushing other streams after failure")
        void testContinueFlushingAfterFailure() throws IOException {
            // Given - Create streams where we can verify flush was called
            TestFlushStream successStream1 = new TestFlushStream();
            TestFlushStream successStream2 = new TestFlushStream();
            FailingOutputStream failingStream = new FailingOutputStream("Flush failed");

            List<OutputStream> mixedStreams = Arrays.asList(successStream1, failingStream, successStream2);
            MultiOutputStream mixedMulti = new MultiOutputStream(mixedStreams);

            // When
            assertThatThrownBy(() -> {
                mixedMulti.flush();
            }).isInstanceOf(IOException.class);

            // Then - All non-failing streams should have been flushed
            assertThat(successStream1.wasFlushCalled()).isTrue();
            assertThat(successStream2.wasFlushCalled()).isTrue();
        }

        // Helper class to test flush behavior
        private static class TestFlushStream extends OutputStream {
            private boolean flushCalled = false;

            @Override
            public void write(int b) throws IOException {
                // No-op
            }

            @Override
            public void flush() throws IOException {
                flushCalled = true;
            }

            public boolean wasFlushCalled() {
                return flushCalled;
            }
        }
    }

    @Nested
    @DisplayName("Close Method Tests")
    class CloseMethodTests {

        @Test
        @DisplayName("Should close all streams")
        void testCloseAllStreams() throws IOException {
            // When
            multiOutputStream.close();

            // Then - Should complete without error (ByteArrayOutputStream close is no-op)
            // Verify by attempting to use closed multi-stream
            assertThatCode(() -> {
                multiOutputStream.write(65);
            }).doesNotThrowAnyException(); // ByteArrayOutputStream doesn't enforce close
        }

        @Test
        @DisplayName("Should close empty stream list without error")
        void testCloseEmptyList() throws IOException {
            // Given
            MultiOutputStream emptyMulti = new MultiOutputStream(new ArrayList<>());

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                emptyMulti.close();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle close failure in one stream")
        void testCloseFailureInOneStream() {
            // Given
            FailingOutputStream failingStream = new FailingOutputStream("Close failed");
            List<OutputStream> mixedStreams = Arrays.asList(stream1, failingStream, stream2);
            MultiOutputStream mixedMulti = new MultiOutputStream(mixedStreams);

            // When/Then
            assertThatThrownBy(() -> {
                mixedMulti.close();
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams")
                    .hasMessageContaining("Close failed");
        }

        @Test
        @DisplayName("Should handle close failure in multiple streams")
        void testCloseFailureInMultipleStreams() {
            // Given
            FailingOutputStream failingStream1 = new FailingOutputStream("Close1 failed");
            FailingOutputStream failingStream2 = new FailingOutputStream("Close2 failed");
            List<OutputStream> failingStreams = Arrays.asList(failingStream1, stream1, failingStream2);
            MultiOutputStream failingMulti = new MultiOutputStream(failingStreams);

            // When/Then
            assertThatThrownBy(() -> {
                failingMulti.close();
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams")
                    .hasMessageContaining("Close1 failed")
                    .hasMessageContaining("Close2 failed");
        }

        @Test
        @DisplayName("Should continue closing other streams after failure")
        void testContinueClosingAfterFailure() throws IOException {
            // Given - Create streams where we can verify close was called
            TestCloseStream successStream1 = new TestCloseStream();
            TestCloseStream successStream2 = new TestCloseStream();
            FailingOutputStream failingStream = new FailingOutputStream("Close failed");

            List<OutputStream> mixedStreams = Arrays.asList(successStream1, failingStream, successStream2);
            MultiOutputStream mixedMulti = new MultiOutputStream(mixedStreams);

            // When
            assertThatThrownBy(() -> {
                mixedMulti.close();
            }).isInstanceOf(IOException.class);

            // Then - All non-failing streams should have been closed
            assertThat(successStream1.wasCloseCalled()).isTrue();
            assertThat(successStream2.wasCloseCalled()).isTrue();
        }

        // Helper class to test close behavior
        private static class TestCloseStream extends OutputStream {
            private boolean closeCalled = false;

            @Override
            public void write(int b) throws IOException {
                // No-op
            }

            @Override
            public void close() throws IOException {
                closeCalled = true;
            }

            public boolean wasCloseCalled() {
                return closeCalled;
            }
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Should format exception messages correctly")
        void testExceptionMessageFormatting() {
            // Given
            FailingOutputStream failingStream1 = new FailingOutputStream("Error message 1");
            FailingOutputStream failingStream2 = new FailingOutputStream("Error message 2");
            List<OutputStream> failingStreams = Arrays.asList(failingStream1, failingStream2);
            MultiOutputStream failingMulti = new MultiOutputStream(failingStreams);

            // When/Then
            assertThatThrownBy(() -> {
                failingMulti.write(65);
            }).isInstanceOf(IOException.class)
                    .hasMessage(
                            "An exception occurred in one or more streams:\n - Error message 1\n - Error message 2");
        }

        @Test
        @DisplayName("Should handle null exception messages")
        void testNullExceptionMessages() {
            // Given
            FailingOutputStream failingStream = new FailingOutputStream(null);
            List<OutputStream> failingStreams = Arrays.asList(failingStream);
            MultiOutputStream failingMulti = new MultiOutputStream(failingStreams);

            // When/Then
            assertThatThrownBy(() -> {
                failingMulti.write(65);
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams")
                    .hasMessageContaining("null");
        }

        @Test
        @DisplayName("Should handle empty exception messages")
        void testEmptyExceptionMessages() {
            // Given
            FailingOutputStream failingStream = new FailingOutputStream("");
            List<OutputStream> failingStreams = Arrays.asList(failingStream);
            MultiOutputStream failingMulti = new MultiOutputStream(failingStreams);

            // When/Then
            assertThatThrownBy(() -> {
                failingMulti.write(65);
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams");
        }

        @Test
        @DisplayName("Should collect all exceptions before throwing")
        void testCollectAllExceptions() {
            // Given
            FailingOutputStream fail1 = new FailingOutputStream("Exception 1");
            FailingOutputStream fail2 = new FailingOutputStream("Exception 2");
            FailingOutputStream fail3 = new FailingOutputStream("Exception 3");
            List<OutputStream> allFailing = Arrays.asList(fail1, fail2, fail3);
            MultiOutputStream allFailingMulti = new MultiOutputStream(allFailing);

            // When/Then
            assertThatThrownBy(() -> {
                allFailingMulti.write(65);
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("Exception 1")
                    .hasMessageContaining("Exception 2")
                    .hasMessageContaining("Exception 3");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with different types of streams")
        void testDifferentStreamTypes() throws IOException {
            // Given
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            LoggingOutputStream loggingStream = new LoggingOutputStream(
                    java.util.logging.Logger.getLogger("test"),
                    java.util.logging.Level.INFO);

            List<OutputStream> mixedTypes = Arrays.asList(byteStream, loggingStream, stream1);
            MultiOutputStream mixedMulti = new MultiOutputStream(mixedTypes);

            // When
            mixedMulti.write(72); // ASCII 'H'
            mixedMulti.flush();
            mixedMulti.close();

            // Then
            assertThat(byteStream.toByteArray()).containsExactly((byte) 72);
            assertThat(stream1.toByteArray()).containsExactly((byte) 72);
        }

        @Test
        @DisplayName("Should handle complex write patterns")
        void testComplexWritePatterns() throws IOException {
            // Given
            String message = "Hello World!";
            byte[] messageBytes = message.getBytes();

            // When
            for (byte b : messageBytes) {
                multiOutputStream.write(b);
            }
            multiOutputStream.flush();

            // Then
            assertThat(stream1.toByteArray()).containsExactly(messageBytes);
            assertThat(stream2.toByteArray()).containsExactly(messageBytes);
            assertThat(stream3.toByteArray()).containsExactly(messageBytes);

            assertThat(new String(stream1.toByteArray())).isEqualTo(message);
            assertThat(new String(stream2.toByteArray())).isEqualTo(message);
            assertThat(new String(stream3.toByteArray())).isEqualTo(message);
        }

        @Test
        @DisplayName("Should work with nested MultiOutputStreams")
        void testNestedMultiOutputStreams() throws IOException {
            // Given
            ByteArrayOutputStream nestedStream1 = new ByteArrayOutputStream();
            ByteArrayOutputStream nestedStream2 = new ByteArrayOutputStream();
            MultiOutputStream nestedMulti = new MultiOutputStream(Arrays.asList(nestedStream1, nestedStream2));

            List<OutputStream> outerStreams = Arrays.asList(stream1, nestedMulti, stream2);
            MultiOutputStream outerMulti = new MultiOutputStream(outerStreams);

            // When
            outerMulti.write(65); // ASCII 'A'
            outerMulti.flush();
            outerMulti.close();

            // Then
            assertThat(stream1.toByteArray()).containsExactly((byte) 65);
            assertThat(stream2.toByteArray()).containsExactly((byte) 65);
            assertThat(nestedStream1.toByteArray()).containsExactly((byte) 65);
            assertThat(nestedStream2.toByteArray()).containsExactly((byte) 65);
        }

        @Test
        @DisplayName("Should handle large amounts of data")
        void testLargeDataHandling() throws IOException {
            // Given
            byte[] largeData = new byte[10000];
            for (int i = 0; i < largeData.length; i++) {
                largeData[i] = (byte) (i % 256);
            }

            // When
            for (byte b : largeData) {
                multiOutputStream.write(b);
            }
            multiOutputStream.flush();

            // Then
            assertThat(stream1.toByteArray()).containsExactly(largeData);
            assertThat(stream2.toByteArray()).containsExactly(largeData);
            assertThat(stream3.toByteArray()).containsExactly(largeData);
        }

        @Test
        @DisplayName("Should work with streams that have state")
        void testStatefulStreams() throws IOException {
            // Given - Create streams that track how many times they're called
            CountingOutputStream counter1 = new CountingOutputStream();
            CountingOutputStream counter2 = new CountingOutputStream();
            List<OutputStream> countingStreams = Arrays.asList(counter1, counter2);
            MultiOutputStream countingMulti = new MultiOutputStream(countingStreams);

            // When
            countingMulti.write(65);
            countingMulti.write(66);
            countingMulti.write(67);
            countingMulti.flush();
            countingMulti.close();

            // Then
            assertThat(counter1.getWriteCount()).isEqualTo(3);
            assertThat(counter1.getFlushCount()).isEqualTo(1);
            assertThat(counter1.getCloseCount()).isEqualTo(1);

            assertThat(counter2.getWriteCount()).isEqualTo(3);
            assertThat(counter2.getFlushCount()).isEqualTo(1);
            assertThat(counter2.getCloseCount()).isEqualTo(1);
        }

        // Helper class to count method calls
        private static class CountingOutputStream extends OutputStream {
            private int writeCount = 0;
            private int flushCount = 0;
            private int closeCount = 0;

            @Override
            public void write(int b) throws IOException {
                writeCount++;
            }

            @Override
            public void flush() throws IOException {
                flushCount++;
            }

            @Override
            public void close() throws IOException {
                closeCount++;
            }

            public int getWriteCount() {
                return writeCount;
            }

            public int getFlushCount() {
                return flushCount;
            }

            public int getCloseCount() {
                return closeCount;
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle stream list modifications during operation")
        void testStreamListModifications() throws IOException {
            // Given
            List<OutputStream> modifiableList = new ArrayList<>(Arrays.asList(stream1, stream2));
            MultiOutputStream modifiableMulti = new MultiOutputStream(modifiableList);

            // When
            modifiableMulti.write(65);

            // Modify the list after creation
            modifiableList.add(stream3);

            modifiableMulti.write(66);

            // Then
            assertThat(stream1.toByteArray()).containsExactly((byte) 65, (byte) 66);
            assertThat(stream2.toByteArray()).containsExactly((byte) 65, (byte) 66);
            assertThat(stream3.toByteArray()).containsExactly((byte) 66); // Only second write
        }

        @Test
        @DisplayName("Should handle partial failures gracefully")
        void testPartialFailures() {
            // Given
            FailingOutputStream partialFailer = new FailingOutputStream("Partial failure");
            List<OutputStream> partialList = Arrays.asList(stream1, partialFailer, stream2);
            MultiOutputStream partialMulti = new MultiOutputStream(partialList);

            // When - First operation fails
            assertThatThrownBy(() -> {
                partialMulti.write(65);
            }).isInstanceOf(IOException.class);

            // Then - Fix the failing stream and try again
            partialFailer.setShouldFail(false);

            assertThatCode(() -> {
                partialMulti.write(66);
            }).doesNotThrowAnyException();

            // Verify all streams got the second write
            assertThat(stream1.toByteArray()).containsExactly((byte) 65, (byte) 66);
            assertThat(stream2.toByteArray()).containsExactly((byte) 65, (byte) 66);
        }

        @Test
        @DisplayName("Should handle null streams in list")
        void testNullStreamsInList() {
            // Given
            List<OutputStream> listWithNull = Arrays.asList(stream1, null, stream2);
            MultiOutputStream nullMulti = new MultiOutputStream(listWithNull);

            // When/Then - Should throw NullPointerException
            assertThatThrownBy(() -> {
                nullMulti.write(65);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle extreme exception message formatting")
        void testExtremeExceptionFormatting() {
            // Given
            String longMessage = "Very long error message ".repeat(100);
            FailingOutputStream longFailer = new FailingOutputStream(longMessage);
            List<OutputStream> longFailList = Arrays.asList(longFailer);
            MultiOutputStream longFailMulti = new MultiOutputStream(longFailList);

            // When/Then
            assertThatThrownBy(() -> {
                longFailMulti.write(65);
            }).isInstanceOf(IOException.class)
                    .hasMessageContaining("An exception occurred in one or more streams")
                    .hasMessageContaining(longMessage);
        }

        @Test
        @DisplayName("Should handle rapid sequential operations")
        void testRapidSequentialOperations() throws IOException {
            // When/Then - Should handle rapid operations without issues
            assertThatCode(() -> {
                for (int i = 0; i < 1000; i++) {
                    multiOutputStream.write(i % 256);
                    if (i % 100 == 0) {
                        multiOutputStream.flush();
                    }
                }
                multiOutputStream.close();
            }).doesNotThrowAnyException();

            // Verify all streams received all writes
            assertThat(stream1.size()).isEqualTo(1000);
            assertThat(stream2.size()).isEqualTo(1000);
            assertThat(stream3.size()).isEqualTo(1000);
        }
    }
}
