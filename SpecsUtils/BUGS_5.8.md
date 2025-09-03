# Bugs Found in Phase 5.8 - XML Framework

## Bug 1: XmlNodes.create() doesn't handle null nodes properly
**Location:** `XmlNodes.create()` method and `XmlNode.getParent()` default implementation  
**Issue:** When a DOM node has no parent (returns null), the `XmlNodes.create(null)` call throws a NullPointerException because the FunctionClassMap doesn't handle null keys properly.  
**Impact:** Makes it impossible to safely call `getParent()` on root nodes like documents. This violates the expected behavior where root nodes should return null for their parent.  
**Test Evidence:** Test for document parent causes NullPointerException instead of returning null.

## Bug 2: XmlNode.write() doesn't properly handle permission errors
**Location:** `XmlNode.write(File)` method  
**Issue:** When write operations fail due to permission errors, the method throws a RuntimeException wrapping the underlying IOException, but the test infrastructure expects it to handle errors gracefully.  
**Impact:** Write operations fail with uncaught exceptions instead of providing graceful error handling mechanisms.  
**Test Evidence:** Write to read-only directory throws RuntimeException instead of handling the error gracefully.

## Bug 3: XmlNode.setText(null) results in empty string instead of null
**Location:** `XmlNode.setText()` default implementation  
**Issue:** When setting text content to null, the underlying DOM implementation converts it to an empty string instead of maintaining the null value.  
**Impact:** Loss of distinction between null and empty text content, which may be important for some XML processing scenarios.  
**Test Evidence:** Setting text to null then getting it returns empty string instead of null.

## Bug 4: XmlNode interface default methods don't handle null getNode() 
**Location:** `XmlNode.getText()`, `XmlNode.getChildren()`, and other default methods
**Issue:** Default interface methods assume `getNode()` never returns null, causing NPE when implementations return null.
**Impact:** Makes it unsafe to create minimal test implementations or handle edge cases where node might be null.
**Test Evidence:** Test `AXmlNodeTest.testAbstractBasePattern()` demonstrates NPE when getNode() returns null in test implementation.

## Bug 5: XmlElement constructor doesn't validate null Element
**Location:** `XmlElement(Element)` constructor
**Issue:** Constructor accepts null Element without throwing exception, allowing creation of invalid XmlElement instances.
**Impact:** Creates XmlElement instances that will fail when any methods are called, making debugging difficult.
**Test Evidence:** `new XmlElement(null)` succeeds instead of throwing NullPointerException.

## Bug 6: XmlElement.getAttribute() doesn't handle null attribute names
**Location:** `XmlElement.getAttribute(String)` method
**Issue:** Passing null as attribute name throws NPE from underlying DOM implementation instead of returning empty string or handling gracefully.
**Impact:** Makes attribute access unsafe when attribute names might be null, inconsistent with documented behavior.
**Test Evidence:** `getAttribute(null)` throws NPE instead of returning empty string.

## Bug 7: XmlElement.setAttribute(null) converts to "null" string
**Location:** `XmlElement.setAttribute(String, String)` method  
**Issue:** Setting attribute value to null converts it to literal "null" string instead of removing attribute or handling null properly.
**Impact:** Loss of distinction between null values and "null" strings, inconsistent with expected null handling.
**Test Evidence:** Setting attribute to null results in "null" string value instead of empty string or removal.

## Bug 8: XML wrapper constructors don't validate null arguments
**Location:** `XmlDocument(Document)` and `XmlGenericNode(Node)` constructors
**Issue:** Constructors accept null arguments without validation, creating wrapper instances that will fail on any method call.
**Impact:** Defers error detection to method usage rather than construction time, making debugging more difficult.
**Test Evidence:** All XML wrapper constructors accept null without throwing exceptions.
