# Phase 6.1 Bug Report

## Bug Analysis for Phase 6.1 Implementation

During Phase 6.1 implementation of the Utilities Framework testing, several bugs were discovered in the CachedItems class behavior and locale-specific formatting differences.

### Bug 1: CachedItems Constructor Accepts Null Mapper
**Location**: `pt.up.fe.specs.util.utilities.CachedItems` constructor  
**Issue**: The constructor does not validate that the mapper function is non-null, which can lead to NullPointerException during get() operations.  
**Impact**: Tests expecting NPE on constructor fail because the exception is deferred until get() is called.  
**Recommendation**: Add null check in constructor: `Objects.requireNonNull(mapper, "Mapper function cannot be null")`.

### Bug 2: Locale-Specific Percentage Formatting
**Location**: `pt.up.fe.specs.util.utilities.CachedItems.getAnalytics()`  
**Issue**: The percentage formatting uses locale-specific decimal separators (comma vs period). In some locales, 33.33% becomes "33,33%" instead of expected "33.33%".  
**Impact**: Tests expecting specific percentage format fail on different system locales.  
**Recommendation**: Use Locale.US for consistent formatting or document the locale dependency.

### Bug 3: CachedItems Null Key Handling
**Location**: `pt.up.fe.specs.util.utilities.CachedItems.get()` with null keys  
**Issue**: When a null key is passed, the mapper function receives null and may throw NPE if not designed to handle null inputs.  
**Impact**: Tests expecting graceful null handling fail when mapper doesn't support null keys.  
**Recommendation**: Add null key validation or document that mappers must handle null keys.

### Bug 4: Thread Safety Test Race Condition  
**Location**: Thread safety test with concurrent access  
**Issue**: Due to race conditions in concurrent execution, the exact number of operations may vary slightly from expected values.  
**Impact**: Intermittent test failures due to timing variations in multi-threaded execution.  
**Recommendation**: Use more flexible assertions for concurrent tests or implement proper synchronization in test design.

These bugs reflect the need for better input validation, consistent locale handling, and more robust concurrent programming patterns in the utilities framework.

### Bug 5: AverageType Empty Collection Null Handling
**Location**: `pt.up.fe.specs.util.utilities.AverageType.calcAverage()` with empty collections  
**Issue**: When calculating arithmetic mean without zeros on empty collections, the method returns null but then tries to unbox it, causing NullPointerException.  
**Impact**: Tests with empty collections fail due to NPE instead of returning appropriate values like NaN.  
**Recommendation**: Add null checks before unboxing: `Double result = SpecsMath.arithmeticMeanWithoutZeros(values); return result != null ? result : Double.NaN;`

### Bug 6: AverageType Zero-Only Collection Handling
**Location**: `pt.up.fe.specs.util.utilities.AverageType.calcAverage()` with zero-only collections  
**Issue**: Collections containing only zeros return NaN for some average types instead of mathematically appropriate results.  
**Impact**: Tests expecting proper mathematical behavior fail when collections contain only zeros.  
**Recommendation**: Add special case handling for zero-only collections to return mathematically appropriate values.

### Bug 7: AverageType Incorrect Geometric Mean Implementation
**Location**: `pt.up.fe.specs.util.utilities.AverageType.calcAverage()` for GEOMETRIC type  
**Issue**: The geometric mean calculation produces incorrect results. For values [1, 2, 4], expected ~2.0 but got ~2.52.  
**Impact**: Mathematical calculations are incorrect, compromising the reliability of geometric mean computations.  
**Recommendation**: Review and fix the geometric mean calculation in the underlying math utility.

### Bug 8: AverageType Infinite Results for Large Datasets
**Location**: `pt.up.fe.specs.util.utilities.AverageType.calcAverage()` for HARMONIC type with large datasets  
**Issue**: Harmonic mean calculations on large datasets return Infinity instead of expected finite values.  
**Impact**: Numerical overflow makes harmonic mean calculations unreliable for large datasets.  
**Recommendation**: Implement more numerically stable harmonic mean calculation or add overflow protection.

### Bug 9: BuilderWithIndentation Null Tab String Acceptance
**Location**: `pt.up.fe.specs.util.utilities.BuilderWithIndentation` constructor  
**Issue**: The constructor accepts null tab strings without validation, which could lead to unexpected behavior.  
**Impact**: Tests expecting NPE on null tab string fail because validation is missing.  
**Recommendation**: Add null validation: `Objects.requireNonNull(tabString, "Tab string cannot be null");`

### Bug 10: BuilderWithIndentation Null String Addition Acceptance
**Location**: `pt.up.fe.specs.util.utilities.BuilderWithIndentation.add()` method  
**Issue**: The method accepts null strings without throwing exceptions, leading to unexpected null handling.  
**Impact**: Tests expecting NPE on null string addition fail because validation is missing.  
**Recommendation**: Either document null string behavior explicitly or add validation to reject null strings.

### Bug 11: BuilderWithIndentation Empty String Line Handling
**Location**: `pt.up.fe.specs.util.utilities.BuilderWithIndentation.addLines()` method  
**Issue**: Adding empty strings doesn't produce the expected indented newline. Expected "\t\n" but got empty string.  
**Impact**: Empty lines are not preserved with proper indentation, affecting formatting consistency.  
**Recommendation**: Ensure empty lines are preserved with proper indentation when adding multi-line strings.

### Bug 12: BuilderWithIndentation Tab Character Handling in Mixed Operations
**Location**: `pt.up.fe.specs.util.utilities.BuilderWithIndentation.add()` method with tab characters  
**Issue**: Tab characters in input strings are not handled consistently with the indentation system.  
**Impact**: Inconsistent formatting when input strings contain existing tab characters.  
**Recommendation**: Define clear behavior for how existing tab characters should interact with the indentation system.

### Bug 13: AverageType Non-Deterministic Behavior Across Test Runs
**Location**: `pt.up.fe.specs.util.utilities.AverageType` multiple methods  
**Issue**: The behavior of ARITHMETIC_MEAN_WITHOUT_ZEROS and GEOMETRIC_MEAN_WITHOUT_ZEROS with empty and zero-only collections is inconsistent between test runs, sometimes returning NaN, sometimes 0.0, and sometimes throwing NPE.  
**Impact**: Makes testing unreliable and suggests potential thread safety issues or environmental dependencies.  
**Recommendation**: Investigate the root cause of non-deterministic behavior and ensure consistent results across multiple test executions.

## 14. BufferedStringBuilder - NullPointerException on null object append (Line 77)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/BufferedStringBuilder.java`  
**Location:** Line 77 in append(Object) method  
**Issue**: Method does not handle null objects gracefully  
**Impact**: Throws NullPointerException when appending null objects  
**Code:**
```java
public BufferedStringBuilder append(Object object) {
    return append(object.toString()); // NPE if object is null
}
```

## 15. BufferedStringBuilder - NullPointerException with null file parameter (Line 110)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/BufferedStringBuilder.java`  
**Location:** Line 110 in save() method, triggered through constructor with null file  
**Issue**: Constructor accepts null file parameter but save() method assumes non-null builder  
**Impact**: Throws NullPointerException during close() when file parameter was null  
**Code:**
```java
// Constructor doesn't validate file parameter
public BufferedStringBuilder(File outputFile) {
    // ...initialization with potentially null file...
}

// save() method assumes builder is initialized
public void save() {
    SpecsIo.write(outputFile, builder.toString()); // NPE if builder is null
}
```

## 16. JarPath - RuntimeException on invalid system property path (Line 100)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/JarPath.java`  
**Location:** Line 100 in buildJarPathInternalTry() method, via SpecsIo.existingFolder()  
**Issue:** Method throws RuntimeException instead of handling invalid paths gracefully  
**Impact:** Application crashes when invalid jar path property is provided instead of falling back to auto-detection  
**Code:**
```java
// In buildJarPathInternalTry()
File jarFolder = SpecsIo.existingFolder(null, jarPath); // Throws RuntimeException if folder doesn't exist

if (jarFolder != null) {
    // This code is never reached when path is invalid
    try {
        return Optional.of(jarFolder.getCanonicalPath());
    } catch (IOException e) {
        return Optional.of(jarFolder.getAbsolutePath());
    }
}
```
**Expected:** Should catch the RuntimeException and continue with fallback mechanisms rather than crashing the application.

## 17. LineStream - Last lines tracking includes null end-of-stream marker (Line 261)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/LineStream.java`  
**Location:** Line 261 in nextLineHelper() method  
**Issue:** Last lines tracking stores null values when stream ends, contaminating the buffer  
**Impact:** getLastLines() returns lists containing null values, making it unreliable for actual content tracking  
**Code:**
```java
// Store line, if active
if (lastLines != null) {
    lastLines.insertElement(line); // This stores null when line is null (end of stream)
}
```
**Expected:** Should not store null values in the last lines buffer, only actual line content.

## 18. ClassMapper - Null class parameter acceptance (Line 58)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/ClassMapper.java`  
**Location:** Line 58 in add() method  
**Issue:** Method accepts null class parameters without validation  
**Impact:** Null classes can be added to the mapper, potentially causing issues in mapping operations  
**Code:**
```java
public boolean add(Class<?> aClass) {
    // Everytime a class is added, invalidate cache
    emptyCache();
    
    return currentClasses.add(aClass); // LinkedHashSet accepts null
}
```
**Expected:** Should validate input and reject null class parameters with appropriate exception.

## 19. ClassMapper - Null mapping parameter acceptance (Line 64)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/ClassMapper.java`  
**Location:** Line 64 in map() method  
**Issue:** Method accepts null class parameters for mapping without validation  
**Impact:** Null classes can be mapped, returning empty results instead of appropriate error handling  
**Code:**
```java
public Optional<Class<?>> map(Class<?> aClass) {
    // Check if correct class has been calculated
    var mapping = cacheFound.get(aClass); // HashMap.get() accepts null keys
    // ... rest of method processes null aClass
}
```
**Expected:** Should validate input and reject null class parameters with appropriate exception.

## 20. ClassMapper - Limited interface hierarchy support (Line 105)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/ClassMapper.java`  
**Location:** Line 105 in calculateMapping() method  
**Issue:** Only checks direct interfaces, not interface inheritance hierarchy  
**Impact:** Classes implementing extended interfaces are not mapped to their super-interfaces  
**Code:**
```java
// Test interfaces
for (Class<?> interf : currentClass.getInterfaces()) {
    if (this.currentClasses.contains(interf)) {
        return interf;
    }
    // Missing: recursive check of interface hierarchy
}
```
**Expected:** Should recursively check interface inheritance hierarchy to find all assignable interfaces.

## 21. PersistenceFormat - Null file parameter acceptance in write() (Line 36)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/PersistenceFormat.java`  
**Location:** Line 36 in write() method  
**Issue:** Method accepts null file parameters without validation, delegating to SpecsIo which logs warnings but doesn't throw exceptions  
**Impact:** Null file parameters return false instead of providing clear error feedback through exceptions  
**Code:**
```java
public boolean write(File outputFile, Object anObject) {
    String contents = to(anObject);
    return SpecsIo.write(outputFile, contents); // Accepts null, logs warning, returns false
}
```
**Expected:** Should validate file parameter and throw appropriate exception for null inputs.

## 22. PersistenceFormat - Null file parameter acceptance in read() (Line 49)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/PersistenceFormat.java`  
**Location:** Line 49 in read() method  
**Issue:** Method accepts null file parameters without validation, delegating to SpecsIo which logs info but returns null content  
**Impact:** Null file parameters return null results instead of providing clear error feedback through exceptions  
**Code:**
```java
public <T> T read(File inputFile, Class<T> classOfObject) {
    String contents = SpecsIo.read(inputFile); // Accepts null, logs info, returns null
    return from(contents, classOfObject);
}
```
**Expected:** Should validate file parameter and throw appropriate exception for null inputs.

## 23. PersistenceFormat - Implicit null class parameter acceptance (Line 50)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/PersistenceFormat.java`  
**Location:** Line 50 in read() method  
**Issue:** Method passes null class parameters to abstract from() method without validation  
**Impact:** Null class parameters may be handled inconsistently by different implementations  
**Code:**
```java
public <T> T read(File inputFile, Class<T> classOfObject) {
    String contents = SpecsIo.read(inputFile);
    return from(contents, classOfObject); // classOfObject can be null
}
```
**Expected:** Should validate class parameter and throw appropriate exception for null inputs.

### Bug 8: IdGenerator Counter Starting Value
**Location**: `pt.up.fe.specs.util.utilities.IdGenerator.next()`  
**Issue**: The IdGenerator uses AccumulatorMap.add() which returns the count AFTER incrementing. This means generated IDs start with suffix "1" instead of "0" which might be unexpected for users expecting 0-based indexing.  
**Example**: 
```java
IdGenerator generator = new IdGenerator();
generator.next("var"); // Returns "var1" not "var0"
generator.next("var"); // Returns "var2" not "var1"  
```
**Impact**: Low - The functionality works correctly, just with 1-based instead of 0-based indexing for generated IDs.  
**Recommendation**: Consider if this is the intended behavior. If 0-based indexing is desired, IdGenerator could subtract 1 from the AccumulatorMap result, or document that IDs start from 1.

## Bug 9: ScheduledLinesBuilder toString() uses incorrect maxLevel calculation

**Location:** `pt.up.fe.specs.util.utilities.ScheduledLinesBuilder.toString()`

**Issue:** The `toString()` method calculates maxLevel as `this.scheduledLines.size() - 1`, but this is incorrect when the map doesn't have consecutive keys starting from 0. For example, if the map contains keys {0, 2}, the size is 2, so maxLevel becomes 1, but it should be 2 to include all elements.

**Expected behavior:** maxLevel should be the maximum key in the map, not size - 1.

**Current behavior:** 
- Empty map: maxLevel = -1, toString returns empty string
- Map with keys {0, 2}: maxLevel = 1, only shows levels 0 and 1, missing level 2

**Suggested fix:** Use `Collections.max(scheduledLines.keySet())` when map is not empty, or handle empty map case separately.

## Bug 10: StringList encoding/decoding not symmetric due to split() behavior

**Location:** `pt.up.fe.specs.util.utilities.StringList.decode()`

**Issue:** The `decode()` method uses `String.split()` which removes trailing empty strings by default. This causes asymmetric encoding/decoding behavior where trailing empty strings are lost.

**Examples:**
- Encoding `["", "a", "", "b", ""]` produces `";a;;b;"`
- Decoding `";a;;b;"` produces `["", "a", "", "b"]` (trailing empty string lost)
- Single semicolon `";"` becomes `[]` instead of `["", ""]`

**Impact:** Round-trip encoding/decoding is not guaranteed to preserve the original data when trailing empty strings are present.

**Suggested fix:** Use `split(pattern, -1)` to preserve trailing empty strings.

## 27. HeapBar - NullPointerException in close() without run() (Line 106)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/heapwindow/HeapBar.java`  
**Location:** Line 106 in close() method  
**Issue:** Calling close() before run() causes NullPointerException because timer is null  
**Impact:** Incorrect usage order causes application crash  
**Code:**
```java
public void close() {
    java.awt.EventQueue.invokeLater(() -> {
        HeapBar.this.timer.cancel(); // timer is null if run() never called
        setVisible(false);
    });
}
```
**Expected:** Should check if timer is null before attempting to cancel it.

## 28. HeapBar - No protection against multiple close() calls (Line 106)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/heapwindow/HeapBar.java`  
**Location:** Line 106 in close() method  
**Issue:** Multiple calls to close() cause NullPointerException after first call  
**Impact:** Defensive programming issue - repeated close calls should be safe  
**Code:**
```java
public void close() {
    java.awt.EventQueue.invokeLater(() -> {
        HeapBar.this.timer.cancel(); // timer becomes null after cancel
        setVisible(false);
    });
}
```
**Expected:** Should set timer to null after cancel and check for null before canceling.

## 29. MemProgressBarUpdater - No null progress bar validation (Line 25)

**File:** `SpecsUtils/src/pt/up/fe/specs/util/utilities/heapwindow/MemProgressBarUpdater.java`  
**Location:** Line 25 in constructor  
**Issue:** Constructor accepts null JProgressBar without validation  
**Impact:** Causes NullPointerException when attempting to update null progress bar  
**Code:**
```java
public MemProgressBarUpdater(JProgressBar jProgressBar) {
    this.jProgressBar = jProgressBar;
    this.jProgressBar.setStringPainted(true); // NPE if jProgressBar is null
}
```
**Expected:** Should validate input parameter and throw IllegalArgumentException for null progress bar.
