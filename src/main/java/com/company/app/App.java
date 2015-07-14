package com.company.app;

import com.typesafe.config.ConfigObject;
import com.google.common.base.Optional;

import java.util.*;

public class App
{
    public static final String OAUTH_PARAM_MAP = "oauthParams";

    /**
     * NonStringValueException if at least one of the input map's values is not a String.
     * @param coerce Value to attempt to cast the unbounded wildcard, i.e. map's value.
     * @param map Map<K, V>
     * @return Map<K, V>
     * @throws NonStringValueException
     */
    private static <K, V> Map<K, V> validate(Class<V> coerce, final Map<K, ?> map)
            throws NonStringValueException {
        final List<Object> invalidValues = new ArrayList<Object>();
        final Map<K, V> result = new HashMap<K, V>();

        for(final Map.Entry<K, ?> entry : map.entrySet()) {
            if(coerce.isInstance(entry.getValue())) {
                result.put(entry.getKey(), coerce.cast(entry.getValue()));
            } else {
                invalidValues.add(entry.getValue());
            }
        }

        if(!invalidValues.isEmpty()) {
            throw new NonStringValueException("Invalid Values: must be `" + coerce.getName() +  "` type: " + invalidValues);
        }

        return result;
    }

    /**
     * Given a List of ConfigObjects, i.e. key-value pairs, return a Map<String, String>.
     * @param list ConfigObject's consisting of key-value pairs
     * @return Map of key-value pairs
     * @throws NonStringValueException if at least one of its values is *not* of String type.
     */
    public static Map<String, String> getMap(final List<? extends ConfigObject> list) throws NonStringValueException{
        final Map<String, Object> map = new HashMap<String, Object>();

        for(final ConfigObject o : list) {
            final Map<String, Object> res = o.unwrapped();
            map.putAll(res);
        }

        return validate(String.class, map);
    }

    private static <K, V> Optional<V> find(final Map<K, V> map, final K key) {
        final V result = map.get(key);
        return Optional.fromNullable(result);
    }

}
