package com.deeeelete.system.service.impl;

import com.deeeelete.system.entity.SysHistoryLogs;
import com.deeeelete.system.service.ISysHistoryLogsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 日志线程
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ThreadPoolSysHandleServiceImpl implements Runnable {

    private SysHistoryLogs sysHistoryLogs;

    private ISysHistoryLogsService service;

    @Override
    public void run() {
        // 插入数据
        try {
            service.save(sysHistoryLogs);
        } catch (Exception e) {
            log.error("日志写入失败  " + e.getMessage());
        }

    }
}
