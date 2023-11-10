package com.deeeelete.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtil {

    /**
     * 判断是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List list){
        if(list==null || list.size() <= 0){
            return true;
        }
        return false;
    }

    /**
     * 判断是否非空
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List list){
        if(list!=null && list.size() > 0){
            return true;
        }
        return false;
    }


    public static <R> List<R> splitStr(String str, String splitPattern, Function<String, R> mapper) {
        String[] strings = str.split(splitPattern);
        Stream<String> stream = Arrays.stream(strings);
        return (List)stream.map(mapper).collect(Collectors.toList());
    }

    public static <A, R> List<R> transformationByListToList(List<A> list, Function<A, R> mapper) {
        return (List)list.stream().map(mapper).collect(Collectors.toList());
    }

    public static <R, A> Map<R, A> transformationByListToMapObject(List<A> list, Function<A, R> mapper) {
        return (Map)list.stream().collect(Collectors.toMap(mapper, (a) -> {
            return a;
        }));
    }

    public static <R, A> Map<R, List<A>> transformationByListToMapList(List<A> list, Function<A, R> mapper) {
        return (Map)list.stream().collect(Collectors.groupingBy(mapper));
    }


}
