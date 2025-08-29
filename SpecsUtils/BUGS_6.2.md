# Phase 6.2 Bug Report

## Bug Analysis for Phase 6.2 Implementation (Events Framework)

During Phase 6.2 implementation of the Events Framework testing, two potential behavioral issues were identified - one in the ActionsMap class and another in the EventRegisterTest implementation.

### Bug 1: ActionsMap Null Action Handling Inconsistency
**Location**: `pt.up.fe.specs.util.events.ActionsMap.performAction()` method  
**Issue**: The ActionsMap allows null actions to be registered via `putAction()` but then treats them as "not found" during execution rather than as explicitly registered null actions. When a null action is registered and an event is performed for that EventId, the system logs a warning saying "Could not find an action for event" even though an action (null) was explicitly registered.  
**Impact**: This creates confusion between "no action registered" and "null action registered" scenarios, making debugging more difficult and potentially masking configuration errors.  
**Current Behavior**: `putAction(eventId, null)` followed by `performAction(event)` logs "Could not find an action" and returns silently.  
**Expected Behavior**: Either (1) prevent null actions from being registered, or (2) distinguish between null actions and missing actions with different warning messages, or (3) throw a more specific exception for null actions.  
**Recommendation**: Add validation in `putAction()` to reject null actions with: `Objects.requireNonNull(action, "EventAction cannot be null")`, or modify the warning message to distinguish between missing and null actions.

This issue reflects the need for clearer contract definition regarding null action handling in the events framework, ensuring consistent behavior between registration and execution phases.

### Bug 2: EventRegisterTest Implementation Error
**Location**: `EventRegisterTest.TestEventRegister` test helper class  
**Issue**: The test helper class `TestEventRegister` was incorrectly implementing methods (`registerListener`, `hasListeners`, `getListeners`) with `@Override` annotations that don't exist in the `EventRegister` interface. The `EventRegister` interface only defines two methods: `registerReceiver(EventReceiver)` and `unregisterReceiver(EventReceiver)`.  
**Impact**: All tests using the `TestEventRegister` class were failing with compilation errors because the class was trying to override non-existent interface methods.  
**Root Cause**: The test was written based on an incorrect assumption about the `EventRegister` interface contract, attempting to implement additional methods that are not part of the actual interface.  
**Resolution**: Removed the `@Override` annotations from the non-interface methods (`registerListener`, `hasListeners`, `getListeners`) making them test-specific helper methods, and updated the `getListeners()` method to return an immutable collection using `Collections.unmodifiableCollection()` to satisfy test expectations.  
**Lesson Learned**: Always verify interface contracts before implementing test helpers, and ensure test implementations match the actual interface being tested rather than assumed behavior.

This bug highlights the importance of understanding the actual interface contracts when writing comprehensive test suites, and the need to distinguish between interface-defined behavior and test-specific helper functionality.
