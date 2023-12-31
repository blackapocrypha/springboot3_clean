/*
 * Copyright 2018 NingWei (ningww1@126.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package com.deeeelete.excel.function;

import java.util.List;

/**
 * 分页查询
 *
 * @param <P>
 * @param <T>
 * @author NingWei
 */
public interface ExportFunction<P, T> {
    /**
     * 分页查询方法
     *
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<T> pageQuery(P param, int pageNum, int pageSize);

    /**
     * 集合内对象转换
     *
     * @param queryResult
     * @return
     */
    Object convert(T queryResult);

}
