package pt.up.fe.specs.util.collections;

import pt.up.fe.specs.util.SpecsCollections;

import java.util.*;

/**
 * Interface that allows a class to support generic attributes.
 */
public interface Attributes {

    /**
     * @return the names of the attributes that are present in this object
     */
    Collection<String> getAttributes();

    /**
     * @return true if the object contains the given attribute
     */
    default boolean hasAttribute(String attribute) {
        return getAttributes().contains(attribute);
    }

    /**
     * @return the value of an attribute, or throws exception if attribute is not
     *          available.
     *          <p>
     *          To see all the attributes iterate the list provided by
     *          {@link Attributes#getAttributes()}
     */
    Object getObject(String attribute);

    /**
     * Sets the value of an attribute, or adds the attribute if not present.
     *
     * @return the previous value assigned to the given attribute, or null if value
     *          was assigned before
     */
    Object putObject(String attribute, Object value);

    /**
     * Convenience method which casts the attribute to the given class.
     *
     */
    default <T> T getObject(String attribute, Class<T> attributeClass) {
        return attributeClass.cast(getObject(attribute));
    }

    /**
     * Attempts to retrieve and convert the value of the corresponding attribute
     * into a list.
     * <p>
     * Currently, supports values which are arrays or a Collection.
     *
     */
    default List<Object> getObjectAsList(String attribute) {
        var value = getObject(attribute);

        if (value.getClass().isArray()) {
            return Arrays.asList((Object[]) value);
        }

        if (value instanceof Collection) {
            return new ArrayList<>((Collection<?>) value);
        }

        throw new RuntimeException("Could not convert object of class '" + value.getClass() + "' in a list");
    }

    /**
     * Convenience method which casts the elements of the list to the given class.
     *
     */
    default <T> List<T> getObjectAsList(String attribute, Class<T> elementClass) {
        return SpecsCollections.cast(getObjectAsList(attribute), elementClass);
    }

    /**
     * @return the value of the attribute wrapped around an Optional, or
     *         Optional.empty() if there is no value for the given attribute
     */
    default Optional<Object> getOptionalObject(String attribute) {
        if (!hasAttribute(attribute)) {
            return Optional.empty();
        }

        return Optional.ofNullable(getObject(attribute));
    }

}
