package com.company.app;

import com.typesafe.config.ConfigObject;
import com.google.common.base.Optional;

import java.util.*;

public class App
{
    public static final String OAUTH_PARAM_MAP = "oauthParams";

    /**
     * Given a List of Map<K, Object> return a Map<K, String>.
     * @param map Map<K, Object>
     * @return Map<K, String>
     * @throws NonStringValueException if at least one of the input map's values is not a String.
     */
    private static <K> Map<K, String> validate(final Map<K, Object> map) throws NonStringValueException {
        final List<Object> invalidValues = new ArrayList<Object>();
        final Map<K, String> result = new HashMap<K, String>();

        for(Map.Entry<K, Object> entry : map.entrySet()) {
            if(!(entry.getValue() instanceof String)) {
                invalidValues.add(entry.getValue());
            }
            else {
                result.put(entry.getKey(), (String) entry.getValue());
            }
        }

        if(!invalidValues.isEmpty()) {
            throw new NonStringValueException("Invalid Values: must be `String` type: " + invalidValues);
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

        return validate(map);
    }

    private static <K, V> Optional<V> find(final Map<K, V> map, final K key) {
        final V result = map.get(key);
        return Optional.fromNullable(result);
    }

}
