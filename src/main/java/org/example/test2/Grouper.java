package org.example.test2;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class Grouper {
    public Map<String, List<NamedObject>> groupByName(List<NamedObject> namedObjects) {
        return namedObjects.stream().collect(groupingBy(NamedObject::getName));
    }
}
