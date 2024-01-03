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


    /**
     * 将字符串数组转为Long List
     * @param split 字符串数组
     * @return List<Long>
     */
    public static List<Long> parseToLongList(String[] split){
        List<Long> classIdList = Arrays.stream(split)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return classIdList;
    }

    /**
     * 将字符串数组转为Integer List
     * @param split 字符串数组
     * @return List<Long>
     */
    public static List<Integer> parseToIntegerList(String[] split){
        List<Integer> classIdList = Arrays.stream(split)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return classIdList;
    }

    /**
     * 将List变成以逗号分割的字符串
     * @param list 数组列表
     * @return String
     */
    public static String makeListToString(List<?> list){
        return list.stream().map(Object::toString).collect(Collectors.joining(","));
    }



}
