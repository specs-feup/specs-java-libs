# Phase 6.6 Bug Report - Jobs Framework

This document records implementation bugs discovered during Phase 6.6 testing of the Jobs Framework (10 classes).

## Summary

During comprehensive unit testing of the Jobs Framework classes, the following implementation issues were identified:

## Job Class Interrupted Flag Propagation Bug

**Location**: `pt.up.fe.specs.util.jobs.Job.run()` method  
**Issue**: The Job class does not properly propagate the interrupted flag from JavaExecution when an exception occurs. When JavaExecution encounters an exception, it sets its internal interrupted flag to true and returns -1. However, the Job.run() method returns immediately when it sees a non-zero result code without checking if the execution was interrupted. The interrupted flag is only checked when the execution returns 0, meaning that JavaExecution exceptions are treated as regular failures rather than interruptions.  
**Impact**: This prevents proper handling of interrupted Java executions and makes it impossible to distinguish between genuine failures and interrupted executions when using JavaExecution with exceptions. This also affects JobUtils.runJobs() which relies on Job.isInterrupted() to decide whether to cancel remaining jobs.  
**Recommendation**: The Job.run() method should check for interruption regardless of the return code, or JavaExecution should use a different mechanism to signal interruption vs failure.

## SpecsIo Empty Extensions Behavior

**Location**: `pt.up.fe.specs.util.jobs.JobUtils.getSourcesFilesMode()` and underlying `SpecsIo.getFilesRecursive()`  
**Issue**: When an empty collection of extensions is passed to JobUtils.getSourcesFilesMode(), the method still returns FileSet objects containing files, suggesting that SpecsIo.getFilesRecursive() with empty extensions matches all files instead of no files.  
**Impact**: This counterintuitive behavior may lead to unexpected file collection when no extensions are specified.  
**Recommendation**: SpecsIo.getFilesRecursive() should return an empty list when given an empty extensions collection, or the behavior should be clearly documented.

## JobProgress Index Out of Bounds Errors

**Location**: `pt.up.fe.specs.util.jobs.JobProgress.nextMessage()` method  
**Issue**: The JobProgress class has multiple index out of bounds issues: 1) When initialized with an empty job list and nextMessage() is called, it throws IndexOutOfBoundsException trying to access jobs.get(counter-1). 2) When nextMessage() is called more times than there are jobs, it warns but continues execution, then throws ArrayIndexOutOfBoundsException when trying to access a job beyond the list bounds. The warning check correctly identifies the problem but doesn't prevent the subsequent crash.  
**Impact**: These exceptions crash the application in edge cases that could reasonably occur in real usage, making the JobProgress class unreliable for empty job lists or when called more times than expected.  
**Recommendation**: The nextMessage() method should return early after logging the warning when counter >= numJobs, and should handle empty job lists gracefully by checking bounds before accessing the jobs list.

## InputMode Null Parameter Handling Issues

**Location**: `pt.up.fe.specs.util.jobs.InputMode.getPrograms()` method and underlying JobUtils methods  
**Issue**: The InputMode.getPrograms() method has null parameter handling issues: 1) For folders mode, passing null folderLevel causes NullPointerException at line 47 when trying to call folderLevel.intValue(). 2) For any mode, passing null extensions causes NullPointerException in JobUtils methods when they try to create a HashSet from the null collection. The JobUtils.getSourcesFilesMode() method fails at line 120 when trying to get collection.size() on null extensions.  
**Impact**: These NullPointerExceptions crash the application when null parameters are passed, which is a reasonable edge case that could occur in real usage. This makes the InputMode enum unreliable for scenarios where parameters might be null.  
**Recommendation**: The InputMode.getPrograms() method should validate parameters before delegating to JobUtils methods, or JobUtils methods should handle null parameters gracefully with appropriate defaults or clear error messages.
