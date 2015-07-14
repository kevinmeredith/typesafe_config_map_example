package com.company.app;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigTest {

    @Test
         public void testValidMap() {
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("non-prod", "foo");
        expected.put("prod", "bar");

        try {
            final Map<String, String> actual = getValidMap();
            assert( expected.equals(actual));
        } catch(NonStringValueException ex){
            assert(false);
        }
    }

    @Test
    public void testInvalidMapWithIntAndBoolValues() {
        try {
            getInvalidMap();
        } catch(NonStringValueException ex){
            assert(ex.getMessage().equals("Invalid Values: must be `java.lang.String` type: [1234123, true]"));
        }
    }

    @Test
    public void testWithEmptyMap() {
        try {
            assert(getEmptyMap().isEmpty());
        } catch(NonStringValueException ex){
            assert(false);
        }
    }

    private Map<String, String> getValidMap() throws NonStringValueException {
        final Config conf = ConfigFactory.load();
        final List<? extends ConfigObject> objects = conf.getObjectList(App.OAUTH_PARAM_MAP);
        return App.getMap(objects);
    }

    // Object consists of Int's rather than String
    private Map<String, String> getInvalidMap() throws NonStringValueException {
        final Config conf = ConfigFactory.load();
        final List<? extends ConfigObject> objects = conf.getObjectList("invalid_map_ints");
        return App.getMap(objects);
    }

    private Map<String, String> getEmptyMap() throws NonStringValueException {
        final Config conf = ConfigFactory.load();
        final List<? extends ConfigObject> objects = conf.getObjectList("empty_map");
        return App.getMap(objects);
    }

}