# Phase 7.4 Bug Report

## Bug Analysis for Phase 7.4 Implementation

During Phase 7.4 implementation of the String Splitter Framework testing, several behavioral quirks and potential design issues were discovered in the string splitting functionality.

### Bug 1: Leading Whitespace Behavior in StringSliceWithSplit.split()
**Location**: `pt.up.fe.specs.util.stringsplitter.StringSliceWithSplit.split()`  
**Issue**: When a string starts with whitespace characters (e.g., "  hello world"), the split() method immediately finds a separator at the beginning and returns an empty string as the first token, rather than skipping to the first non-whitespace content.  
**Impact**: This behavior makes it difficult to parse strings with leading whitespace, as the first split result is always empty. It requires additional logic to handle the empty results.  
**Recommendation**: Consider implementing a "skip leading separators" mode or documenting this behavior clearly for users who expect the first token to be "hello" rather than an empty string.

### Bug 2: Reverse Mode with Trailing Whitespace
**Location**: `pt.up.fe.specs.util.stringsplitter.StringSliceWithSplit.nextReverse()`  
**Issue**: Similar to the leading whitespace issue, when using reverse mode on strings with trailing whitespace (e.g., "hello world  "), the first split in reverse returns an empty string instead of the last meaningful token.  
**Impact**: Reverse parsing becomes inconsistent and difficult to use with strings that have trailing whitespace.  
**Recommendation**: Consider implementing consistent whitespace handling for both forward and reverse modes.

### Bug 3: Strict Mode Default Behavior Inconsistency
**Location**: `pt.up.fe.specs.util.stringsplitter.StringSplitterRules.doubleNumber()` and `floatNumber()`  
**Issue**: The StringSplitterRules for double and float parsing use `isStrict = false` when calling SpecsStrings parsing methods, while the default behavior in SpecsStrings is strict mode. This inconsistency can lead to unexpected parsing results.  
**Impact**: Numbers that would normally fail strict parsing (due to precision loss) are accepted in StringSplitterRules, potentially leading to data loss or unexpected behavior.  
**Recommendation**: Document the non-strict behavior clearly or consider making the strict mode configurable in the rules.

### Bug 4: No Built-in Whitespace Skipping Utilities
**Location**: General framework design  
**Issue**: The String Splitter Framework lacks built-in utilities to skip leading/trailing whitespace or empty tokens, requiring users to manually handle these common cases.  
**Impact**: Common parsing scenarios require additional boilerplate code to handle whitespace and empty tokens properly.  
**Recommendation**: Add utility methods like `skipEmpty()` or `skipWhitespace()` to make common parsing patterns easier.

These behavioral quirks reflect the low-level nature of the String Splitter Framework, where precise control over splitting behavior is prioritized over convenience. However, they may be unexpected for users coming from higher-level string parsing utilities.
