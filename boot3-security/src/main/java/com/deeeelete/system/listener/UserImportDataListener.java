package com.deeeelete.system.listener;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.system.entity.AclUser;
import com.deeeelete.system.entity.dto.AclUserExportDTO;
import com.deeeelete.system.entity.dto.AclUserImportDTO;
import com.deeeelete.system.entity.enums.GenderEnum;
import com.deeeelete.system.service.IAclUserService;
import com.deeeelete.utils.ListUtil;
import com.deeeelete.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Slf4j
public class UserImportDataListener implements ReadListener<AclUserImportDTO>{

    /**
     * 每隔100条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存的数据
     */
    private List<AclUser> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private List<AclUserExportDTO> errorList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 持久化服务
     */
    private IAclUserService saveDataService;

    /**
     * redis
     */
    private RedisTemplate redisTemplate;

    /**
     * 错误Key
     */
    private String errorKey;


    public UserImportDataListener(IAclUserService saveDataService, RedisTemplate redisTemplate, String errorKey) {
        this.saveDataService = saveDataService;
        this.redisTemplate = redisTemplate;
        this.errorKey = errorKey;
    }

    @Override
    public void invoke(AclUserImportDTO demoData, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(demoData));

        AclUser targetData = new AclUser();
        StringBuilder stringBuffer = new StringBuilder();
        // 对错误的数据单独进行摘出
        // 名称
        if (StringUtil.isEmpty(demoData.getAccount())) {
            stringBuffer.append(" "  +"账号不能为空！");
        } else {
            if (saveDataService.count(new LambdaQueryWrapper<AclUser>().eq(AclUser::getAccount, demoData.getAccount())) > 0) {
                stringBuffer.append(" "  + "账号已存在！");
            }
        }


        // 密码
        if (StringUtil.isEmpty(demoData.getPassword())) {
            stringBuffer.append(" "  + "密码不能为空！");
        }
        if (StringUtil.isNotEmpty(demoData.getPassword()) && demoData.getPassword().length() < 6) {
            stringBuffer.append(" "  +  "密码不能低于6位！");
        }

        // 中文名称
        if (StringUtil.isEmpty(demoData.getRealName())) {
            stringBuffer.append(" "  +  "真实姓名不能为空！");
        }

        // 追加
        if (StringUtil.isNotEmpty(stringBuffer.toString())) {
            AclUserExportDTO userExportDTO = new AclUserExportDTO();
            BeanUtils.copyProperties(demoData,userExportDTO);
            userExportDTO.setTip(stringBuffer.toString());
            errorList.add(userExportDTO);
        } else {
            BeanUtils.copyProperties(demoData,targetData);
            cachedDataList.add(targetData);
        }

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
        if (errorList.size() >= BATCH_COUNT) {
            saveError();
            errorList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        saveError();
        log.info("所有数据解析完成！");
    }


    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        saveDataService.saveBatch(cachedDataList);
        log.info("存储数据库成功！");
    }

    /**
     * 加上存储缓存
     */
    private void saveError(){
        if(ListUtil.isNotEmpty(errorList)){
            log.info("{}条错误数据，开始打入缓存！", errorList.size());
            redisTemplate.opsForList().leftPushAll(errorKey, errorList);
        }

    }
}
