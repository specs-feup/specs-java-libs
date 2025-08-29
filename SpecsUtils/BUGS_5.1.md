# TreeNode Framework - Bug Report (Phase 5.1)

## Bug #1: setChildren() doesn't handle null input gracefully

**Location:** `ATreeNode.java:105`

**Description:** 
The `setChildren(Collection<? extends K> children)` method throws a `NullPointerException` when passed a null collection, instead of treating it as an empty collection.

**Observed Behavior:**
```java
root.setChildren(null); // Throws NullPointerException
```

**Stack Trace:**
```
java.lang.NullPointerException: Cannot invoke "java.util.Collection.iterator()" because "children" is null
	at pt.up.fe.specs.util.treenode.ATreeNode.setChildren(ATreeNode.java:105)
```

**Expected Behavior:**
- Should handle null input gracefully by treating it as an empty collection
- Should clear existing children when null is passed

**Suggested Fix:**
Add null check at the beginning of the `setChildren` method:
```java
@Override
public void setChildren(Collection<? extends K> children) {
    // Remove previous children in this node
    int numChildren = getNumChildren();
    for (int i = 0; i < numChildren; i++) {
        this.removeChild(0);
    }

    // Add new children (handle null case)
    if (children != null) {
        for (K child : children) {
            addChild(child);
        }
    }
}
```

## Bug #2: getChildTry() doesn't handle invalid indices gracefully

**Location:** `TreeNode.java:470`

**Description:**
The `getChild(Class<T> nodeClass, int index)` method is supposed to return an `Optional.empty()` for invalid indices, but instead has inconsistent exception behavior that depends on the underlying `getChild()` implementation.

**Observed Behavior:**
- When node has children but invalid index: `IndexOutOfBoundsException` from ArrayList.get()
- When node has no children: `RuntimeException` from SpecsCheck.checkNotNull() after getChild() returns null

```java
root.getChildTry(TestTreeNode.class, -1); // Throws IndexOutOfBoundsException
root.getChildTry(TestTreeNode.class, 999); // Throws IndexOutOfBoundsException
child2.getChildTry(TestTreeNode.class, 0); // Throws RuntimeException "No child at index 0"
```

**Root Cause:**
The `getChild(int index)` method has inconsistent behavior:
- Returns `null` and logs warning when node has no children
- Delegates to `ArrayList.get(index)` when node has children (throws IndexOutOfBoundsException)

Then `getChildTry()` calls `SpecsCheck.checkNotNull()` on the result, which throws RuntimeException when getChild() returns null.

**Expected Behavior:**
- Should return `Optional.empty()` for any invalid index (negative or >= numChildren)
- Should only return Optional.empty() for type mismatches when index is valid

**Suggested Fix:**
Add bounds checking before calling `getChild()`:
```java
default <T extends K> Optional<T> getChildTry(Class<T> nodeClass, int index) {
    // Check bounds first
    if (index < 0 || index >= getNumChildren()) {
        return Optional.empty();
    }
    
    K childNode = getChild(index);

    SpecsCheck.checkNotNull(childNode, () -> "No child at index " + index + " of node '" + getClass()
            + "' (children: " + getNumChildren() + "):\n" + this);

    if (!nodeClass.isInstance(childNode)) {
        return Optional.empty();
    }

    return Optional.of(nodeClass.cast(childNode));
}
```

## Bug #3: Missing convenience methods for common operations

**Description:**
The TreeNode interface lacks some convenience methods that would be commonly needed:

1. **Missing `getChildTry(int index)` method** - Users need to provide a class parameter even when they just want to get a child by index safely
2. **Inconsistent exception handling** - Some methods throw exceptions while similar methods return Optional

**Suggested Enhancement:**
Add convenience method:
```java
default Optional<K> getChildTry(int index) {
    if (index < 0 || index >= getNumChildren()) {
        return Optional.empty();
    }
    return Optional.of(getChild(index));
}
```

## Bug #4: getChild() has inconsistent error handling behavior

**Location:** `TreeNode.java:79`

**Description:**
The `getChild(int index)` method has inconsistent behavior depending on whether the node has children:
- When node has no children: returns `null` and logs a warning
- When node has children but invalid index: throws `IndexOutOfBoundsException`

**Observed Behavior:**
```java
child2.getChild(0); // Returns null (child2 has no children)
root.getChild(-1); // Throws IndexOutOfBoundsException (root has children)
root.getChild(999); // Throws IndexOutOfBoundsException (root has children)
```

**Expected Behavior:**
Consistent error handling - either always throw exceptions for invalid indices or always return null.

**Impact:**
This inconsistency makes the API unpredictable and leads to the problems in `getChildTry()` method.

**Suggested Fix:**
Make the behavior consistent, preferably by always checking bounds first:
```java
default K getChild(int index) {
    if (index < 0 || index >= getNumChildren()) {
        SpecsLogs.warn("Tried to get child with index '" + index + "', but children size is " + getNumChildren());
        return null;
    }
    
    return getChildren().get(index);
}
```

## Bug #5: NodeInsertUtils.replace() API confusion with child preservation

**Location:** `NodeInsertUtils.java:70-102`

**Description:**
The `replace()` method's behavior regarding child preservation is confusing and inconsistent with user expectations. The `move` parameter does not control whether children are preserved, but rather whether the replacement token is detached from its current parent.

**API Confusion:**
```java
// User expectation: move=true preserves children, move=false does not
NodeInsertUtils.replace(oldToken, newToken, true);  // Assumed to preserve children
NodeInsertUtils.replace(oldToken, newToken, false); // Assumed to not preserve children
```

**Actual Behavior:**
- `move` parameter controls whether `newToken` is detached from its current parent
- Children preservation is **never** done by `replace()` method
- To preserve children, users must use `set()` method instead

**Working Solution:**
```java
// To preserve children from oldToken to newToken:
NodeInsertUtils.set(newToken, oldToken.getChildren());
NodeInsertUtils.replace(oldToken, newToken, true);

// Or combine in one call:
NodeInsertUtils.set(newToken, oldToken.getChildren());
NodeInsertUtils.replace(oldToken, newToken, false); // move parameter irrelevant for child preservation
```

**Root Cause:**
The method signature `replace(TreeNode<?> baseToken, TreeNode<?> newToken, boolean move)` suggests that `move` controls the replacement behavior, but it actually only controls detachment of `newToken`.

**Impact:**
- Users expect child preservation based on parameter name
- No clear indication in method signature that children are never preserved
- Requires additional API call (`set()`) for common use case of replacing while preserving children

**Suggested Fix:**
1. **Improve documentation** to clearly state that `replace()` never preserves children
2. **Add overloaded method** with clearer semantics:
```java
public static void replacePreservingChildren(TreeNode<?> baseToken, TreeNode<?> newToken, boolean move) {
    set(newToken, baseToken.getChildren());
    replace(baseToken, newToken, move);
}
```
3. **Rename parameter** from `move` to `detachReplacement` for clarity

## Bug #7: ATreeNode addChild/setChildren behavior with nodes that have parents is confusing

**Location:** `ATreeNode.java:addChild()`, `ATreeNode.java:setChildren()`, `TreeNodeUtils.java:sanitizeNode()`

**Description:**
The ATreeNode framework's behavior when adding children that already have parents is counterintuitive and potentially problematic. Instead of detaching nodes from their existing parents and moving them to the new parent, the framework creates copies of the nodes, leaving the original nodes with their original parents unchanged.

**Observed Behavior:**
```java
TestTreeNode root = new TestTreeNode("root");
TestTreeNode otherParent = new TestTreeNode("otherParent");
TestTreeNode child = new TestTreeNode("child");

otherParent.addChild(child); // child.parent = otherParent
root.addChild(child); // Creates a COPY of child, original child stays with otherParent

assertThat(child.getParent()).isSameAs(otherParent); // Still true!
assertThat(root.getChild(0)).isNotSameAs(child); // Different object entirely
```

**Root Cause:**
The `sanitizeNode()` method in TreeNodeUtils creates a deep copy of any node that has a parent, rather than detaching it from its current parent. This means the original tree structure remains unchanged when nodes are "moved" between parents.

**Impact:**
- Memory leaks: Original nodes remain in old trees even when conceptually "moved"
- Unexpected behavior: Adding a child doesn't actually move the child as users would expect
- Inconsistent tree operations: Some operations work on references, others create copies
- Testing complications: Tests expecting node movement behavior fail unexpectedly

**Suggested Fix:**
Consider adding a configuration option or separate methods for "move" vs "copy" semantics, or change the default behavior to actually detach nodes from their current parents before adding them to new parents.

## Bug #8: ATreeNode detach() method is overly strict with error handling

**Location:** `ATreeNode.java:detach()`

**Description:**
The `detach()` method throws a RuntimeException when called on a node that doesn't have a parent, making it unsafe to call in scenarios where the parent state is uncertain. This forces users to check `hasParent()` before every `detach()` call.

**Observed Behavior:**
```java
TestTreeNode orphan = new TestTreeNode("orphan"); // No parent
orphan.detach(); // Throws RuntimeException: "Does not have a parent"
```

**Expected Behavior:**
Many tree frameworks make `detach()` a safe operation that simply does nothing if the node is already detached. This follows the principle of idempotent operations.

**Impact:**
- Forces defensive programming with hasParent() checks
- Makes generic tree manipulation code more complex
- Inconsistent with user expectations from other tree frameworks

**Suggested Fix:**
Make `detach()` idempotent by returning early if the node has no parent:
```java
@Override
public void detach() {
    // Safe detach - do nothing if already detached
    if (!hasParent()) {
        return;
    }
    // ... rest of current implementation
}
```
