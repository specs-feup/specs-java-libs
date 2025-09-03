# Coverage Testing Bug Report #1

## Additional Bugs Discovered During Phase 1

### Bug 2: DelaySlotBranchCorrector Logic Issues
**Status**: IDENTIFIED (Not Fixed) ❌

**Description**: The `DelaySlotBranchCorrector` class has logic issues causing test failures in complex jump scenarios.

**Location**: `SpecsUtils/src/pt/up/fe/specs/util/asm/processor/DelaySlotBranchCorrector.java`

**Failing Tests**:
1. `DelaySlotBranchCorrectorTest.java:409` - "Should handle alternating jump patterns"
2. `DelaySlotBranchCorrectorTest.java:288` - "Should handle consecutive jumps with delay slots"

**Root Cause**: The corrector's `isJumpPoint()` method returns false when it should return true for complex patterns involving alternating and consecutive jumps with delay slots.

**Impact**: Assembly instruction processing may not correctly identify jump points, affecting branch prediction and execution flow.

### Bug 3: JumpDetector Branch Logic Issue
**Status**: IDENTIFIED (Not Fixed) ❌

**Description**: JumpDetector has issues with conditional branch detection.

**Failing Test**: `JumpDetectorTest.java:410` - "Should detect not taken conditional branch"

**Root Cause**: Logic for detecting "not taken" conditional branches appears incorrect.

**Impact**: Could affect branch prediction accuracy in ASM processing workflows.

---
