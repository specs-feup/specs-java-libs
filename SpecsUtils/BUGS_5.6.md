# Bugs Found in Phase 5.6 - Class Mapping Framework

## Bug 1: ClassMap Null Value Handling Issue
**Location:** `ClassMap.java` line 135
**Severity:** High
**Description:** The ClassMap implementation uses `SpecsCheck.checkNotNull()` to verify that mapped values exist, but this incorrectly throws a NullPointerException when a class is explicitly mapped to a null value. This prevents legitimate use cases where null is a valid mapped value.

**Evidence:**
- When putting `null` as a value: `map.put(Integer.class, null)`
- Subsequent `get()` or `tryGet()` calls throw: `NullPointerException: Expected map to contain class java.lang.Integer`
- This occurs even though the class key exists in the map with an explicit null value

**Impact:** 
- Cannot store null values in ClassMap, limiting its usefulness
- Violates principle of least surprise (Map interface allows null values)
- Causes runtime crashes when null is a legitimate mapped value

**Recommendation:** 
- Modify the logic to distinguish between "key not found" and "key found with null value"
- Use `map.containsKey()` check before `SpecsCheck.checkNotNull()`
- Allow null values to be stored and retrieved correctly

## Bug 2: ClassMap Null Class Key Handling
**Location:** `ClassMap.java` 
**Severity:** Medium
**Description:** When a null class is passed to `get()` method, the implementation returns `NotImplementedException` instead of the expected `NullPointerException`, creating inconsistent error handling behavior.

**Evidence:**
- `map.get((Class<? extends Number>) null)` throws `NotImplementedException: Function not defined for class 'null'`
- Standard Java collections throw `NullPointerException` for null keys
- Inconsistent with Java collection contracts

**Impact:** 
- Unexpected exception type confuses error handling code
- Violates Java collection interface contracts
- Makes debugging more difficult

## Bug 3: ClassSet Null Handling Inconsistency
**Location:** `ClassSet.java`
**Severity:** Medium  
**Description:** ClassSet accepts null values in add() and contains() methods without throwing exceptions, which is inconsistent with standard Java collection behavior. The add() method returns true for null, and contains() returns false for null without validation.

**Evidence:**
- `classSet.add(null)` returns `true` instead of throwing NullPointerException
- `classSet.contains((Class<? extends T>) null)` returns `false` instead of throwing NullPointerException
- `classSet.contains((T) null)` returns `false` instead of throwing NullPointerException
- This behavior is inconsistent with Java Set interface contracts

**Impact:**
- Allows invalid state where null can be "added" to the set
- Inconsistent with Java collection interface expectations
- May cause confusion in client code expecting standard collection behavior

**Recommendation:**
- Add explicit null checks in add() and contains() methods
- Throw NullPointerException for null arguments
- Ensure consistency with standard Java collections

## Bug 4: ClassSet Interface Hierarchy Support Issues
**Location:** `ClassSet.java` via `ClassMapper.java`
**Severity:** Medium
**Description:** ClassSet does not properly handle interface hierarchies. When an interface is added to the set, subinterfaces and implementing classes are not recognized as contained elements, breaking polymorphic behavior.

**Evidence:**
- Adding `Collection.class` to set does NOT make `List.class` contained (returns false)
- Adding `Collection.class` to set does NOT make `ArrayList` instances contained (returns false)
- Interface inheritance chain lookup is not working correctly

**Expected vs Actual:**
- Test expects: `List.class` should be found when `Collection.class` is in set (true)
- Test actual: `List.class` is NOT found (false)
- This proves the interface hierarchy traversal is broken

**Impact:**
- Interface-based polymorphism not supported correctly
- Reduces utility for generic programming patterns
- Inconsistent behavior between class and interface hierarchies

**Recommendation:**
- Debug the `ClassMapper.calculateMapping()` method's interface handling
- The algorithm may not be properly checking `getInterfaces()` results
- Consider if interface hierarchy requires recursive traversal

## Bug 5: FunctionClassMap Null Default Function Return Handling
**Location:** `FunctionClassMap.java`, line 227
**Severity:** Medium
**Description:** FunctionClassMap incorrectly handles null returns from default functions, throwing NullPointerException instead of returning Optional.empty().

**Evidence:**
- Line 227: `return Optional.of(this.defaultFunction.apply(t));`
- Uses `Optional.of()` instead of `Optional.ofNullable()`
- When default function returns null, throws NPE instead of graceful handling

**Expected Behavior:**
- Default functions should be allowed to return null
- Should return `Optional.empty()` when default function returns null
- Should not throw exceptions for null returns from user-provided functions

**Impact:**
- Runtime crashes when default functions return null
- Inconsistent null handling compared to other parts of the API
- Forces users to handle null checking in their default functions

**Recommendation:**
- Change line 227 from `Optional.of()` to `Optional.ofNullable()`
- This will properly handle null returns from default functions
- Test thoroughly with null-returning default functions

## Bug 6: MultiFunction Fluent Interface Broken
**Location:** `MultiFunction.java`, setDefaultValue/setDefaultFunction methods
**Severity:** Low
**Description:** MultiFunction setter methods return new instances instead of the same instance, breaking fluent interface patterns.

**Evidence:**
- `setDefaultValue()` and `setDefaultFunction()` methods return new MultiFunction instances
- This breaks method chaining expectations
- Users expect `mf.setDefaultValue("x").setDefaultFunction(f)` to work on the same instance

**Expected Behavior:**
- Setter methods should modify the current instance and return `this`
- This enables fluent interface patterns and method chaining
- Consistent with builder pattern expectations

**Impact:**
- Breaks fluent interface usage patterns
- Unexpected behavior when chaining method calls
- API inconsistency with typical setter conventions

**Recommendation:**
- Modify setters to return `this` instead of creating new instances
- Or document that these methods create new instances (immutable pattern)

## Bug 7: MultiFunction Default Values Not Working
**Location:** `MultiFunction.java`, default value handling
**Severity:** Medium
**Description:** MultiFunction does not properly use default values or default functions when no mapping is found, throwing exceptions instead.

**Evidence:**
- Tests show that setting default values/functions doesn't prevent exceptions
- `NotImplementedException` is thrown even when defaults are set
- Default value/function mechanisms appear to be broken

**Expected Behavior:**
- When no mapping is found, should use default value if set
- When no mapping is found, should use default function if set
- Should only throw exception if no mapping AND no defaults are available

**Impact:**
- Default value/function feature is non-functional
- Users cannot provide fallback behavior
- API promises defaults but doesn't deliver

**Recommendation:**
- Debug the default value lookup mechanism
- Ensure defaults are checked before throwing exceptions
- Test default behavior thoroughly
