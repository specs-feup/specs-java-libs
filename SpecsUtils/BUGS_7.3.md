# Phase 7.3 Bug Report

## Bug Analysis for Phase 7.3 Implementation

During Phase 7.3 implementation of the Thread Stream Framework testing, several behavioral inconsistencies and potential bugs were discovered in the AObjectStream implementation.

### Bug 1: PeekNext() Returns Null Before Stream Initialization
**Location**: `pt.up.fe.specs.util.threadstream.AObjectStream.peekNext()`  
**Issue**: The peekNext() method returns nextT, but nextT is not initialized until the first next() call. This means peekNext() always returns null before the stream is used, even if items are available.  
**Impact**: Tests expecting peek functionality before consumption fail because peek returns null instead of the first available item.  
**Recommendation**: Consider initializing nextT lazily in peekNext() if not already initialized, or document that peek only works after the first next() call.

### Bug 2: Stream Closed State Timing Inconsistency  
**Location**: `pt.up.fe.specs.util.threadstream.AObjectStream.next()`  
**Issue**: The stream is marked as closed (isClosed = true) when nextT becomes null during a next() call, which happens immediately when poison is encountered in getNext(). This means isClosed() returns true before the poison is actually returned to the consumer.  
**Impact**: Tests expecting the stream to remain open until after poison consumption fail because the closed state is set prematurely.  
**Recommendation**: Consider deferring the closed state until after the null is returned to the consumer, or document the current behavior clearly.

### Bug 3: HasNext() Behavior Before Initialization
**Location**: `pt.up.fe.specs.util.threadstream.AObjectStream.hasNext()`  
**Issue**: The hasNext() method always returns true before initialization (when inited == false), regardless of whether items are actually available. This is optimistic but may be misleading.  
**Impact**: Tests may incorrectly assume items are available when hasNext() returns true before any consumption.  
**Recommendation**: Document that hasNext() is optimistic before first use, or consider lazy initialization in hasNext() similar to next().

### Bug 4: GenericObjectStream Close Method Not Implemented
**Location**: `pt.up.fe.specs.util.threadstream.GenericObjectStream.close()`  
**Issue**: The close() method contains only a TODO comment and doesn't implement any cleanup logic. This leaves resources potentially unclosed.  
**Impact**: Resource leaks may occur when streams are not properly closed, though the exact impact depends on usage patterns.  
**Recommendation**: Implement proper cleanup in the close() method or document that manual cleanup is not needed for this implementation.

These behaviors may be intentional design decisions for the threading framework, but they differ from typical Java stream patterns and should be clearly documented to avoid confusion during testing and usage.
