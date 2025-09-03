# Phase 6.4 Bug Report - Swing Framework

This document records implementation bugs discovered during Phase 6.4 testing of the Swing Framework (4 classes).

## Summary

During comprehensive unit testing of the Swing Framework classes, the following implementation issues were identified:

# MapModel Implementation Issues

## Bug 1: MapModel creates internal copy of map instead of referencing original
**Location**: MapModel constructor, line ~52  
**Issue**: The constructor creates a new HashMap copy of the provided map using `SpecsFactory.newHashMap(map)` instead of directly referencing the original map. This breaks the expected behavior where changes to the underlying map should be reflected in the table model, and changes through the model should update the original map.  
**Impact**: Tests expecting bidirectional synchronization between the model and original map fail. The model operates on its internal copy while the original map remains unchanged, violating the typical table model contract where the model should reflect the actual data source.

## Bug 2: Row-wise value updates are not implemented
**Location**: MapModel.updateValue() method, lines ~195-197  
**Issue**: When using row-wise layout (rowWise=true), attempting to update values throws "UnsupportedOperationException: Not yet implemented" for both key updates (row 0) and value updates (row 1).  
**Impact**: Row-wise models are effectively read-only, preventing any data modifications through the table interface.

## Bug 3: MapModel doesn't handle out-of-bounds access consistently
**Location**: MapModel.getValueAt() method  
**Issue**: The implementation doesn't properly validate row/column indices before accessing internal data structures. Out-of-bounds access may result in unexpected exceptions from underlying collections rather than consistent IndexOutOfBoundsException handling.  
**Impact**: Inconsistent exception behavior when accessing invalid table coordinates, making error handling unpredictable for client code.

## Bug 4: Key update operations throw wrong exception type
**Location**: MapModel.setValueAt() method, line ~338 in test execution  
**Issue**: When attempting to update a key (column 0 in column-wise mode), the implementation first checks type compatibility and throws RuntimeException for type mismatches before checking if the operation is supported. This means trying to update a key with the wrong type throws RuntimeException instead of UnsupportedOperationException.  
**Impact**: Exception hierarchy doesn't follow expected patterns - type errors are caught before operation support is validated, making error handling inconsistent.
