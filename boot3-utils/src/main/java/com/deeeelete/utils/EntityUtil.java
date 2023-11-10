package com.deeeelete.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EntityUtil {
    private static final Logger log = LoggerFactory.getLogger(EntityUtil.class);

    public EntityUtil() {
    }

    public static <T> List<T> parentListToChildList(List parentList, Class<T> childClass) {
        if (ListUtil.isEmpty(parentList)) {
            return null;
        } else {
            return childClass == null ? null : JsonFormatUtil.stringToArrayList(JsonFormatUtil.objectToString(parentList), childClass);
        }
    }

    public static <T> T parentToChild(Object parent, Class<T> childClass) {
        if (parent == null) {
            return null;
        } else {
            return childClass == null ? null : JsonFormatUtil.stringToObject(JsonFormatUtil.objectToString(parent), childClass);
        }
    }

    public static <T> List<T> childListToParentList(List childList, Class<T> parentClass) {
        if (ListUtil.isEmpty(childList)) {
            return null;
        } else {
            return parentClass == null ? null : JsonFormatUtil.stringToArrayList(JsonFormatUtil.objectToString(childList), parentClass);
        }
    }

    public static <T> T childToParent(Object child, Class<T> parentClass) {
        if (child == null) {
            return null;
        } else {
            return parentClass == null ? null : JsonFormatUtil.stringToObject(JsonFormatUtil.objectToString(child), parentClass);
        }
    }
}
