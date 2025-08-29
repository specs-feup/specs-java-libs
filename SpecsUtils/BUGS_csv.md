# CSV Utility Classes Bug Report

## Bug 1: Incorrect Range Calculation in CsvWriter

**Class**: `pt.up.fe.specs.util.csv.CsvWriter`
**Method**: `getDataEndColumn()`
**Issue**: The method calculates the end column for Excel formulas incorrectly, causing formula ranges to be too narrow.

**Description**: When adding fields like AVERAGE to a CsvWriter, the formula range is calculated incorrectly. For a header with columns "data1", "data2", the expected range should be B2:C2 (columns B to C), but the actual output is B2:B2 (only column B).

**Root Cause**: The `getDataEndColumn()` method uses `header.size()` directly without accounting for the fact that column indexing starts from 1 and the dataOffset. The startColumn correctly calculates as `1 + dataOffset` (= 2, which is column B), but endColumn should be `header.size() + dataOffset - 1` to include all data columns.

**Example**:
- Header: ["data1", "data2"] (size = 2)
- dataOffset = 1
- Expected: startColumn = B (index 2), endColumn = C (index 3)
- Actual: startColumn = B (index 2), endColumn = B (index 2)
- Result: =AVERAGE(B2:B2) instead of =AVERAGE(B2:C2)

**Impact**: All formula calculations will be incorrect when there are multiple data columns, affecting statistical calculations like averages and standard deviations.

## Bug 2: ArrayIndexOutOfBoundsException with Empty Headers

**Class**: `pt.up.fe.specs.util.csv.CsvWriter`
**Method**: `buildHeader()`
**Issue**: The method throws an ArrayIndexOutOfBoundsException when trying to build CSV content with an empty header.

**Description**: When a CsvWriter is created with no header arguments (using the default constructor), the `buildHeader()` method attempts to access the first element of an empty list, causing an ArrayIndexOutOfBoundsException.

**Root Cause**: The constructor `CsvWriter()` calls `Arrays.asList()` which creates an empty list, but `buildHeader()` assumes at least one header element exists when it tries to access `this.header.get(0)`.

**Example**:
```java
CsvWriter writer = new CsvWriter(); // Creates empty header list
writer.buildCsv(); // Throws ArrayIndexOutOfBoundsException
```

**Impact**: The class cannot handle the case of no header columns, even though the constructor allows it.
