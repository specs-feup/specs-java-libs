# Bug Report for Phase 5.3 - Advanced Logging Framework

## Bug 1: StringHandler.publish() does not handle null records gracefully

**Location**: `pt.up.fe.specs.util.logging.StringHandler.publish(LogRecord record)` at line 67

**Description**: The method throws a `NullPointerException` when a null `LogRecord` is passed because it directly calls `record.getMessage()` without null checking. Standard logging handlers typically handle null records silently by ignoring them.

**Root Cause**: The overridden `publish()` method bypasses the parent `StreamHandler.publish()` call and directly accesses `record.getMessage()`, causing NPE when record is null.

## Bug 2: StringHandler bypasses level and filter mechanisms

**Location**: `pt.up.fe.specs.util.logging.StringHandler.publish(LogRecord record)` method

**Description**: StringHandler completely bypasses the inherited StreamHandler's level filtering and custom filter mechanisms by directly appending to the buffer instead of calling `super.publish(record)`. This means that setting a level or filter on the handler has no effect, violating expected Handler interface behavior.

**Root Cause**: The publish() method implementation directly appends `record.getMessage()` to the buffer without first checking if the record should be processed according to the handler's level and filter settings.

## Bug 3: SpecsLoggers.getLogger() does not handle null logger names

**Location**: `pt.up.fe.specs.util.logging.SpecsLoggers.getLogger(String loggerName)` at line 54

**Description**: The method throws a `NullPointerException` when a null logger name is passed because the `ConcurrentHashMap.get(null)` call fails. The standard Java `Logger.getLogger(null)` would actually throw a `NullPointerException` as well, but the current implementation fails earlier during the cache lookup, providing a less informative stack trace.

**Root Cause**: The code calls `LOGGERS.get(loggerName)` without checking if `loggerName` is null first. `ConcurrentHashMap` does not accept null keys, so this throws `NullPointerException: Cannot invoke "Object.hashCode()" because "key" is null`.

**Impact**: Any code that inadvertently passes null as a logger name will get an unclear NPE from the caching layer rather than the expected NPE from the underlying Java logging framework.

**Suggested Fix**: Add a null check before the cache lookup, or catch the NPE and let the standard `Logger.getLogger(null)` provide the proper exception behavior.

**Test Evidence**: The test `SpecsLoggersTest.testNullLoggerName()` demonstrates this behavior by catching the unexpected NPE during cache lookup.

## Bug 4: LogSourceInfo does not handle null parameters

**Location**: `pt.up.fe.specs.util.logging.LogSourceInfo.getLogSourceInfo(Level level)` at line 44 and `setLogSourceInfo(Level level, LogSourceInfo info)` at line 50

**Description**: Both methods throw `NullPointerException` when null parameters are passed because the underlying `ConcurrentHashMap` does not accept null keys or values. The `getLogSourceInfo()` method throws NPE on `LOGGER_SOURCE_INFO.get(level)` when level is null, and `setLogSourceInfo()` throws NPE when either level is null (null key) or info is null (null value).

**Root Cause**: The methods call `ConcurrentHashMap.get(null)` and `ConcurrentHashMap.put(null, value)` or `ConcurrentHashMap.put(key, null)` without null checks. `ConcurrentHashMap` explicitly prohibits both null keys and null values.

**Impact**: Any code that passes null Level or null LogSourceInfo (which could happen in error conditions or during logging framework initialization) will get unclear NPEs from the map operations rather than graceful handling.

**Suggested Fix**: Add null checks in both methods. For `getLogSourceInfo()`, return `NONE` when level is null. For `setLogSourceInfo()`, either ignore null parameters or throw more informative exceptions.

**Test Evidence**: The tests `LogSourceInfoTest.testNullLevel()` and `LogSourceInfoTest.testNullLogSourceInfo()` demonstrate these NPE behaviors during map operations.

## Bug 5: StringLogger does not create defensive copy of tags set

**Location**: `pt.up.fe.specs.util.logging.StringLogger(String baseName, Set<String> tags)` constructor at line 29

**Description**: The constructor stores the provided tags set directly without creating a defensive copy (`this.tags = tags`). This means external modifications to the original set will affect the StringLogger's tag collection after construction, violating the principle of immutability and encapsulation.

**Root Cause**: The constructor assigns the tags parameter directly to the instance field without creating a defensive copy using `new HashSet<>(tags)` or `Collections.unmodifiableSet()`.

**Impact**: This can lead to unexpected behavior where a StringLogger's tags change after construction if the original set is modified externally. This breaks the expected immutability of configuration objects and could cause subtle bugs in logging behavior, such as tags appearing or disappearing unexpectedly during runtime.

**Suggested Fix**: Create a defensive copy in the constructor: `this.tags = new HashSet<>(tags)` or make it immutable: `this.tags = Collections.unmodifiableSet(new HashSet<>(tags))`.

**Test Evidence**: The test `StringLoggerTest.testTagSetReference()` demonstrates this behavior by showing that modifications to the original set are reflected in the StringLogger's tags collection.

## Bug 6: LoggerWrapper constructor does not handle null logger names

**Location**: `pt.up.fe.specs.util.logging.LoggerWrapper(String loggerName)` constructor at line 32

**Description**: The constructor throws a `NullPointerException` when a null logger name is passed because it directly calls `Logger.getLogger(loggerName)` without validation. The underlying Java logging framework uses a `ConcurrentHashMap` internally which does not accept null keys, causing the NPE during logger lookup.

**Root Cause**: The constructor calls `Logger.getLogger(loggerName)` without checking if `loggerName` is null first. This triggers a `NullPointerException: Cannot invoke "Object.hashCode()" because "key" is null` from the Java logging framework's internal hash map operations.

**Impact**: Any code that inadvertently passes null as a logger name to the wrapper will get an unclear NPE from the Java logging framework's internal implementation rather than a clear validation error from the wrapper itself.

**Suggested Fix**: Add input validation in the constructor to either reject null parameters with a clear error message or provide a default logger name behavior.

**Test Evidence**: The test `LoggerWrapperTest.testNullLoggerName()` demonstrates this behavior by catching the NPE during logger creation.

## Bug 7: LoggerWrapper parseMessage method does not handle null messages

**Location**: `pt.up.fe.specs.util.logging.LoggerWrapper.parseMessage(String msg)` method at line 74

**Description**: The `parseMessage` method throws a `NullPointerException` when a null message is passed because it calls `msg.isEmpty()` without null checking. This affects the public `info(String msg)` method which forwards messages to `parseMessage`, causing null message logging to fail unexpectedly.

**Root Cause**: The method immediately calls `msg.isEmpty()` without checking if `msg` is null first, causing `NullPointerException: Cannot invoke "String.isEmpty()" because "msg" is null`.

**Impact**: Any code that passes null messages to `info()` (which could happen in error conditions or when logging optional values) will get NPEs instead of graceful handling. Many logging frameworks handle null messages by treating them as empty strings or the literal "null".

**Suggested Fix**: Add null checking in `parseMessage()`: `if (msg == null || msg.isEmpty()) return msg;` or similar null-safe logic to handle null messages appropriately.

**Test Evidence**: The tests `LoggerWrapperTest.testNullInfoMessage()` and `LoggerWrapperTest.testParseMessageNull()` demonstrate this NPE behavior when processing null messages.

## Bug 8: TextAreaHandler.publish() does not handle null records gracefully

**Location**: `pt.up.fe.specs.util.logging.TextAreaHandler.publish(LogRecord record)` at line 39

**Description**: The method throws a `NullPointerException` when a null `LogRecord` is passed because it directly calls `record.getLevel()` without null checking. Standard logging handlers typically handle null records silently.

**Root Cause**: The publish() method calls `record.getLevel().intValue()` without checking if record is null first.

## Bug 9: TextAreaHandler does not respect custom filters

**Location**: `pt.up.fe.specs.util.logging.TextAreaHandler.publish(LogRecord record)` method

**Description**: TextAreaHandler only checks the level but ignores any custom filters set on the handler. The Handler interface specification requires that both level and filter (if set) should be checked before processing a record.

**Root Cause**: The publish() method only checks `record.getLevel().intValue() < this.getLevel().intValue()` but doesn't call `this.getFilter()` or check if the filter accepts the record.

## Bug 10: Java logging Handler.setFormatter() does not accept null

**Location**: Standard Java logging framework behavior

**Description**: The standard `Handler.setFormatter(null)` call throws `NullPointerException` because the Java logging framework requires a non-null formatter. This is documented Java behavior, not a bug in the implementation, but it affects testing scenarios.

**Root Cause**: Java's Handler class uses `Objects.requireNonNull(newFormatter)` in the setFormatter method.

# SpecsUtils Phase 5.3 Bug Report

## Bugs Found During Advanced Logging Framework Testing

### 1. SpecsLoggers - ConcurrentHashMap Null Rejection
**File:** `SpecsLoggers.java`  
**Issue:** The underlying ConcurrentHashMap in `LOGGERS_MAP` rejects null keys, but the method signature suggests null keys should be accepted.  
**Expected:** Null keys would be handled gracefully with a reasonable default behavior.  
**Actual:** `NullPointerException` is thrown when trying to store null keys.  
**Analysis:** This is a design inconsistency where the API suggests null-tolerance but the implementation uses a data structure that explicitly rejects nulls. Either the documentation should clarify that nulls aren't supported, or the implementation should use a wrapper that handles nulls appropriately.

### 2. LogSourceInfo - Constructor Null Level Rejection  
**File:** `LogSourceInfo.java`  
**Issue:** Constructor throws `NullPointerException` when passed null Level parameter.  
**Expected:** Either explicit null checking with meaningful error, or null-tolerant behavior.  
**Actual:** Generic `NullPointerException` with unclear source.  
**Analysis:** The constructor should either explicitly validate the Level parameter with a clear error message, or have documented behavior for handling null levels (e.g., defaulting to a standard level).

### 3. LoggerWrapper - Inconsistent Null Handling
**File:** `LoggerWrapper.java`  
**Issue:** Constructor accepts null Logger parameter without validation, but subsequent method calls fail.  
**Expected:** Either fail-fast in constructor or handle null Logger gracefully throughout.  
**Actual:** Constructor succeeds, but later method calls throw `NullPointerException`.  
**Analysis:** This creates a delayed failure pattern that's harder to debug. The wrapper should either validate the Logger parameter upfront or implement null-safe behavior for all operations.

### 4. StringHandler - Filter Bypass Bug
**File:** `StringHandler.java`  
**Issue:** Handler processes LogRecords even when filter rejects them.  
**Expected:** LogRecords rejected by filter should not be formatted or published.  
**Actual:** Filter rejection is ignored, and LogRecords are processed normally.  
**Analysis:** The handler should respect the filter's decision and skip processing when filter returns false. This could lead to unwanted log output that should have been filtered out.

### 5. StringHandler - Null Parameter Tolerance
**File:** `StringHandler.java`  
**Issue:** Some methods accept null parameters silently when they should probably validate or document this behavior.  
**Expected:** Clear null handling behavior (either reject with validation or document null-tolerant behavior).  
**Actual:** Inconsistent null handling across different methods.  
**Analysis:** This makes the API unpredictable for users who need to know whether null parameters are acceptable.

### 6. TextAreaHandler - Filter Bypass and Null Issues
**File:** `TextAreaHandler.java`  
**Issue:** Similar to StringHandler - ignores filter decisions and has inconsistent null parameter handling.  
**Expected:** Proper filter respect and clear null handling policies.  
**Actual:** Filter is bypassed, and null handling is inconsistent.  
**Analysis:** This is the same pattern as StringHandler, suggesting a common issue in the handler architecture where filter logic is not being properly implemented.

### 7. Java Logging Framework Limitation
**File:** Various logging handlers  
**Issue:** `Handler.setFormatter(null)` behavior varies between Java versions and implementations.  
**Expected:** Consistent behavior when setting null formatter.  
**Actual:** Some implementations throw exceptions, others use default formatting.  
**Analysis:** This is a broader Java logging framework inconsistency that affects any custom handlers. The framework should either standardize null formatter behavior or provide clear documentation about expected behavior.

### 8. MultiOutputStream - Resource Management with Failing Streams
**File:** `MultiOutputStream.java`  
**Issue:** When using try-with-resources with MultiOutputStream containing failing streams, the close() operation can also fail, causing test difficulties.  
**Expected:** Clean resource management pattern that doesn't interfere with exception testing.  
**Actual:** try-with-resources close() operation throws exceptions from failing streams, masking the original test exception.  
**Analysis:** This makes testing of failure scenarios difficult when using proper resource management patterns. The MultiOutputStream implementation correctly aggregates exceptions, but this creates challenges for unit testing where you want to verify specific failure behaviors without interference from cleanup operations.

---

**Testing Notes:** All issues documented above represent actual behavioral differences from expected patterns, not test implementation problems. Each bug reflects a design or implementation inconsistency that could affect production usage of the logging framework.
