package com.deeeelete.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deeeelete.excel.entity.ErrorEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deeeelete.enums.RequestEnum;
import com.deeeelete.excel.ExcelBoot;
import com.deeeelete.excel.function.ExportFunction;
import com.deeeelete.excel.function.ImportFunction;
import com.deeeelete.pojo.JWTUser;
import com.deeeelete.system.entity.AclRole;
import com.deeeelete.system.entity.AclUser;
import com.deeeelete.system.entity.AclUserRole;
import com.deeeelete.system.entity.dto.AclUserExportDTO;
import com.deeeelete.system.entity.dto.AclUserImportDTO;
import com.deeeelete.system.entity.enums.GenderEnum;
import com.deeeelete.system.entity.enums.MenuTypeEnum;
import com.deeeelete.system.entity.query.AclUserQuery;
import com.deeeelete.system.entity.dto.AclUserDTO;
import com.deeeelete.system.mapper.AclMenuRoleMapper;
import com.deeeelete.system.mapper.AclRoleMapper;
import com.deeeelete.system.mapper.AclUserMapper;
import com.deeeelete.system.mapper.AclUserRoleMapper;
import com.deeeelete.system.service.IAclUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deeeelete.system.util.JWTSecurityUtil;
import com.deeeelete.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户 服务实现类
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Service
public class AclUserServiceImpl extends ServiceImpl<AclUserMapper, AclUser> implements IAclUserService {

    @Autowired
    private JWTSecurityUtil jwtSecurityUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AclUserRoleMapper aclUserRoleMapper;

    @Autowired
    private AclRoleMapper aclRoleMapper;

    /**
     * 用户key
     */
    private static String redisStringKey = "user::";


    @Value("${myConfig.file.frontUploadUrl}")
    private String webUrl;

    @Autowired
    private AclMenuRoleMapper aclMenuRoleMapper;

    /**
     * 分页查询用户
     *
     * @return JsonResult
     */
    @Override
    public JsonResult selectAll(AclUserQuery query) {
        JsonResult jsonResult = new JsonResult();
        query.buildExample();
        // 创建page类并查询，本语句已经查询完毕
        Page<AclUser> page = query.buildPage(this);
        List<AclUserDTO> records = EntityUtil.parentListToChildList(page.getRecords(), AclUserDTO.class);
        if (ListUtil.isNotEmpty(records)) {
            List<Long> userIds = records.stream().map(m -> m.getId()).distinct().collect(Collectors.toList());
            List<AclUserRole> aclUserRoles = aclUserRoleMapper.selectList(new LambdaQueryWrapper<AclUserRole>().in(AclUserRole::getAcurUserId, userIds));
            Map<Long, List<AclUserRole>> userRoleMap = ListUtil.transformationByListToMapList(aclUserRoles, AclUserRole::getAcurUserId);
            Map<Long, AclRole> longAclRoleMap = new HashMap<>();
            if (ListUtil.isNotEmpty(aclUserRoles)) {
                List<Long> roleIds = aclUserRoles.stream().map(m -> m.getAcurRoleId()).distinct().collect(Collectors.toList());
                List<AclRole> aclRoles = aclRoleMapper.selectList(new LambdaQueryWrapper<AclRole>().in(AclRole::getRoleId, roleIds));
                longAclRoleMap = ListUtil.transformationByListToMapObject(aclRoles, AclRole::getRoleId);
            }
            for (AclUserDTO record : records) {
                record.setPassword("");
                List<AclUserRole> aclUserRoleList = userRoleMap.get(record.getId());
                List<AclRole> myRoles = new ArrayList<>();
                if (ListUtil.isNotEmpty(aclUserRoleList)) {
                    for (AclUserRole aclUserRole : aclUserRoleList) {
                        myRoles.add(longAclRoleMap.get(aclUserRole.getAcurRoleId()));
                    }
                }
                record.setMyRoles(myRoles);
            }


            jsonResult.buildTrue();
            jsonResult.setData(records);
            jsonResult.setTotalsize(page.getTotal());
        } else {
            jsonResult.buildFalse("无数据");
        }
        return jsonResult;
    }

    /**
     * 根据主键删除用户
     *
     * @param id 主键
     * @return JsonResult
     */
    @Override
    public JsonResult deleteByKey(Long id) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(id)) {
            jsonResult.buildFalse("id为空");
            return jsonResult;
        }
        if (id.equals(1L)) {
            jsonResult.buildFalse("禁止删除默认用户");
            return jsonResult;
        }

        if (removeById(id)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("删除失败");
        }
        return jsonResult;
    }

    /**
     * 根据主键修改用户
     *
     * @param record 修改信息
     * @return JsonResult
     */
    @Override
    public JsonResult updateByKey(AclUser record) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(record.getId())) {
            jsonResult.buildFalse("id为空");
            return jsonResult;
        }
        AclUser oldUser = getById(record.getId());
        if (StringUtil.isEmpty(oldUser)) {
            jsonResult.buildFalse("用户不存在");
            return jsonResult;
        }
        if (StringUtil.isEmpty(record.getPassword())) {
            record.setPassword(null);
        }
        if (!oldUser.getAccount().equals(record.getAccount()) && StringUtil.isNotEmpty(record.getAccount())) {
            jsonResult.buildFalse("账号禁止修改");
            return jsonResult;
        }

        if (StringUtil.isEmpty(record.getAccount())) {
            record.setAccount(null);
        }
        if (StringUtil.isEmpty(record.getPhone())) {
            record.setPhone(null);
        }
        if (StringUtil.isEmpty(record.getGender())) {
            record.setGender(null);
        }
        if (StringUtil.isEmpty(record.getUserImage())) {
            record.setUserImage(null);
        }
        if (StringUtil.isEmpty(record.getNickName())) {
            record.setNickName(null);
        }
        if (StringUtil.isEmpty(record.getRealName())) {
            record.setRealName(null);
        }

        if (StringUtil.isNotEmpty(record.getPassword())) {
            record.setPassword(MD5Util.getSaltMD5(record.getPassword(), oldUser.getSalt()));
        }

        if (updateById(record)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("更新失败");
        }
        return jsonResult;
    }

    /**
     * 插入用户
     *
     * @param record 插入信息
     * @return JsonResult
     */
    @Override
    public JsonResult add(AclUser record) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(record.getAccount())) {
            jsonResult.buildFalse("账号不能为空");
            return jsonResult;
        }
        if (count(new LambdaQueryWrapper<AclUser>().eq(AclUser::getAccount, record.getAccount())) > 0) {
            jsonResult.buildFalse("账号不能为重复");
            return jsonResult;
        }

        if (StringUtil.isEmpty(record.getPassword())) {
            jsonResult.buildFalse("密码不能为空");
            return jsonResult;
        }

        if (StringUtil.isEmpty(record.getUserImage())) {
            record.setUserImage("/default.png");
        }
        record.setSalt(MD5Util.getSalt());
        record.setPassword(MD5Util.getSaltMD5(record.getPassword(), record.getSalt()));

        if (save(record)) {
            jsonResult.buildTrue();
        } else {
            jsonResult.buildFalse("插入失败");
        }
        return jsonResult;
    }

    /**
     * 用户登录
     *
     * @param dto 用户数据
     * @return JsonResult
     */
    @Override
    public JsonResult userLogin(AclUserDTO dto) {
        // 验证码
        JsonResult checkCaptcha = checkCaptcha(dto.getCode(), dto.getUuid());
        if (!checkCaptcha.isSuccess()) {
            return checkCaptcha;
        }
        AclUser user = new AclUser();
        BeanUtils.copyProperties(dto, user);
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(user.getAccount())) {
            jsonResult.buildFalse("账号不能为空");
            return jsonResult;
        }
        if (StringUtil.isEmpty(user.getPassword())) {
            jsonResult.buildFalse("密码不能为空");
            return jsonResult;
        }

        List<AclUser> aclUsers = list(new LambdaQueryWrapper<AclUser>().eq(AclUser::getAccount, user.getAccount()));
        if (ListUtil.isEmpty(aclUsers)) {
            jsonResult.buildFalse("账号不存在");
            return jsonResult;
        }
        AclUser aclUser = aclUsers.get(0);
        if (!aclUser.getPassword().equals(MD5Util.getSaltMD5(user.getPassword(), aclUser.getSalt()))) {
            jsonResult.buildFalse("密码错误");
            return jsonResult;
        }

        // 是否重复获取
        if (redisTemplate.hasKey(redisStringKey + aclUser.getId())) {
            jsonResult.buildTrue();
            jsonResult.setData(redisTemplate.opsForValue().get(redisStringKey + aclUser.getId()));
            return jsonResult;
        }


        // 封装进token
        JWTUser jwtUser = new JWTUser();
        jwtUser.setId(aclUser.getId());
        jwtUser.setUserAccount(aclUser.getAccount());
        jwtUser.setUserRealname(aclUser.getRealName());
        jwtUser.setUserSalt(aclUser.getSalt());
        jwtUser.setUserGender(aclUser.getGender());
        jwtUser.setImg(aclUser.getUserImage());
        jwtUser.setRoutes(aclMenuRoleMapper.selectAllUserMenuCodeByUserId(aclUser.getId(), MenuTypeEnum.MENU.getCode()));
        jwtUser.setButtons(aclMenuRoleMapper.selectAllUserMenuCodeByUserId(aclUser.getId(), MenuTypeEnum.BUTTON.getCode()));
        String realToken = jwtSecurityUtil.jwtEncode(jwtUser);
        // 登录保持24小时
        redisTemplate.opsForValue().set(redisStringKey + jwtUser.getId(), realToken, 24L, TimeUnit.HOURS);

        jsonResult.buildTrue();
        jsonResult.setData(realToken);
        return jsonResult;
    }

    /**
     * 批量删除
     *
     * @param ids 多个id
     * @return JsonResult
     */
    @Override
    @Transactional
    public JsonResult deleteBatch(String ids) {
        JsonResult jsonResult = new JsonResult();
        if (StringUtil.isEmpty(ids)) {
            jsonResult.buildFalse("不能为空");
            return jsonResult;
        }

        List<Long> idList = null;
        try {
            String[] manyIds = ids.split(",");
            idList = Arrays.asList(manyIds).stream().map(m -> Long.parseLong(m)).collect(Collectors.toList());
        } catch (Exception e) {
            jsonResult.buildFalse("id错误");
            return jsonResult;
        }

        // 默认无法删除默认角色
        idList.remove(1L);
        if (removeByIds(idList)) {
            jsonResult.buildTrue();
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            jsonResult.buildFalse(RequestEnum.REQUEST_ERROR_DATABASE_DELETE_NO_KEY);
        }

        return jsonResult;
    }


    /**
     * 校验验证码
     *
     * @param code   验证码
     * @param uuid   uuid
     * @return JsonResult
     */
    public JsonResult checkCaptcha(String code, String uuid) {
        JsonResult jsonResult = new JsonResult();

        if (StringUtil.isEmpty(code)) {
            jsonResult.buildFalse("验证码不能为空");
            return jsonResult;
        }
        if (StringUtil.isEmpty(uuid)) {
            jsonResult.buildFalse("验证码已失效");
            return jsonResult;
        }
        String verifyKey = "captcha_codes:" + uuid;
        String captcha = (String) redisTemplate.opsForValue().get(verifyKey);
        if (StringUtil.isEmpty(captcha)) {
            jsonResult.buildFalse("验证码已失效");
            return jsonResult;
        }
        redisTemplate.delete(verifyKey);

        if (!code.equalsIgnoreCase(captcha)) {
            jsonResult.buildFalse("验证码错误");
            return jsonResult;
        }
        jsonResult.buildTrue();
        return jsonResult;
    }


    /**
     * 导入用户
     *
     * @param file 文件
     * @return JsonResult
     */
    @Override
    @Transactional
    public JsonResult excelImport(MultipartFile file) {
        JsonResult jsonResult = new JsonResult();

        String webappPath = webUrl;
        String fileDir = "import" + File.separator + DateUtil.format(new Date(), "yyyy-MM-dd");
        String fileName = "userImport" + System.currentTimeMillis() + ".xlsx";

        List<AclUserExportDTO> notInsertList = new ArrayList<>();
        List<AclUser> insertList = new ArrayList<>();
        ArrayList<String> errorList = new ArrayList<>();

        File file1 = new File(webappPath + File.separator + fileDir);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        String filePath = webappPath + File.separator + fileDir + File.separator + fileName;
        File fileExcel = null;
        try {
            fileExcel = FileUtil.multipartFileToFile(file, filePath);
        } catch (IOException e) {
            jsonResult.buildFalse("文件创建失败，请重试！");
            return jsonResult;
        }
        try {

            ExcelBoot.ImportBuilder(new FileInputStream(fileExcel), AclUserImportDTO.class).importExcel(new ImportFunction<AclUserImportDTO>() {
                @Override
                public void onProcess(int sheetIndex, int rowIndex, AclUserImportDTO infoImportDTO) {
                    StringBuffer stringBuffer = new StringBuffer();

                    AclUser aclUser = new AclUser();
                    BeanUtils.copyProperties(infoImportDTO, aclUser);

                    // 名称
                    if (StringUtil.isEmpty(infoImportDTO.getAccount())) {
                        stringBuffer.append("第" + rowIndex + "行,账号不能为空！");
                    } else {
                        if (count(new LambdaQueryWrapper<AclUser>().eq(AclUser::getAccount, infoImportDTO.getAccount())) > 0) {
                            stringBuffer.append("第" + rowIndex + "行,账号已存在！");
                        }
                    }


                    // 密码
                    if (StringUtil.isEmpty(infoImportDTO.getPassword())) {
                        stringBuffer.append("第" + rowIndex + "行,密码不能为空！");
                    }
                    if (StringUtil.isNotEmpty(infoImportDTO.getPassword()) && infoImportDTO.getPassword().length() < 6) {
                        stringBuffer.append("第" + rowIndex + "行,密码不能低于6位！");
                    }

                    // 中文名称
                    if (StringUtil.isEmpty(infoImportDTO.getRealName())) {
                        stringBuffer.append("第" + rowIndex + "行,真实姓名不能为空！");
                    }

                    // 性别
                    if (StringUtil.isNotEmpty(infoImportDTO.getGender())) {
                        aclUser.setGender(GenderEnum.getEnumByMeaning(infoImportDTO.getGender()).getCode());
                    }

                    // 如果错误信息不为空说明有错误，追加到错误信息列表
                    if (StringUtil.isNotEmpty(stringBuffer.toString())) {
                        AclUserExportDTO userExportDTO = new AclUserExportDTO();
                        BeanUtils.copyProperties(infoImportDTO, userExportDTO);
                        userExportDTO.setTip(stringBuffer.toString());
                        userExportDTO.setOldRow(rowIndex);
                        notInsertList.add(userExportDTO);
                        errorList.add(stringBuffer.toString());
                    } else {
                        aclUser.setUserImage("/default.png");
                        // 追加到插入列表
                        insertList.add(aclUser);
                    }
                }

                @Override
                public void onError(ErrorEntity errorEntity) {
                    AclUserExportDTO importDTO = new AclUserExportDTO();
                    importDTO.setTip("第" + errorEntity.getCellIndex() + "列导入失败，" + errorEntity.getErrorMessage() + "。解析错误，无法复制信息");
                    importDTO.setOldRow(errorEntity.getRowIndex());
                    notInsertList.add(importDTO);
                }
            });
        } catch (FileNotFoundException e) {
            jsonResult.buildFalse("Excel导入失败，请重试！");
            e.printStackTrace();
        }

        // 插入
        for (AclUser aclUser : insertList) {
            // 加密
            String salt = MD5Util.getSalt();
            aclUser.setSalt(salt);
            aclUser.setPassword(MD5Util.getSaltMD5(aclUser.getPassword(), salt));
        }
        saveBatch(insertList);

        if (ListUtil.isNotEmpty(notInsertList)) {
            // 导出
            jsonResult.buildFalse("导入失败");
            jsonResult.setData(exportNoInsertEntity(notInsertList));
            return jsonResult;
        }

        jsonResult.buildTrue();
        return jsonResult;
    }

    /**
     * 导出插入不成功的数据
     *
     * @param notInsertList 未插入列表
     * @return String
     */
    public String exportNoInsertEntity(List<AclUserExportDTO> notInsertList) {
        if (ListUtil.isNotEmpty(notInsertList)) {

            String outputpath = "excel" + File.separator + DateUtil.format(new Date(), "yyyy-MM-dd");
            String mypath = outputpath + File.separator + System.currentTimeMillis() + "_AclUserExport.xlsx";
            String fileName = webUrl + mypath;
            File file1 = new File(webUrl + outputpath);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(new File(fileName));
                ExcelBoot.ExportBuilder(fileOutputStream,
                                "Sheet1", AclUserExportDTO.class)
                        .exportStream(null, new ExportFunction<QueryWrapper, AclUserExportDTO>() {
                            @Override
                            public List<AclUserExportDTO> pageQuery(QueryWrapper queryWrapper, int i, int i1) {
                                return notInsertList;
                            }

                            @Override
                            public Object convert(AclUserExportDTO t) {
                                return t;
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String fileDownLoadPath = File.separator + mypath;
            return fileDownLoadPath.replaceAll(Pattern.quote("\\"), "/");
        }
        return null;
    }

}
