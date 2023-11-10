地址：https://gitee.com/programmeres/easy-poi?_from=gitee_search#pomxml



pom

```xml
<dependency>
	<groupId>io.github.magic-core</groupId>
	<artifactId>excel-boot</artifactId>
	<version>2.0</version>
</dependency>
```



导入的逻辑是将正确的数据进行导入，将错误的数据导出到一个excel中返回



导入模板

```java
@Data
public class ProductClassImportDTO {

    /**
     * 分类名称
     */
    @ImportField
    @ExportField(columnName = "分类名称")
    private String prclName;


    /**
     * 编码
     */
    @ImportField
    @ExportField(columnName = "分类编码")
    private String prclCode;


    /**
     * 中文名称
     */
    @ImportField
    @ExportField(columnName = "中文名称")
    private String prcaNameCh;


    /**
     * 英文名称
     */
    @ImportField
    @ExportField(columnName = "英文名称")
    private String prcaNameEn;


    /**
     * 值
     */
    @ImportField
    @ExportField(columnName = "值")
    private String prcaValue;


    /**
     * 排序
     */
    @ImportField
    @ExportField(columnName = "排序(1-100)")
    private Integer prcaSort;

    /**
     * 是否打印 默认Y
     */
    @ImportField
    @ExportField(columnName = "是否打印 （是/否）")
    private String prcaPrint;


    @ExportField(columnName = "原始行数")
    private Integer oldRow;

    @ExportField(columnName = "错误原因")
    private String tip;

}
```



导入

```java
   @Override
    public JsonResult importProductClass(MultipartFile file, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();

        String webappPath = request.getSession().getServletContext().getRealPath("/");
        String fileDir = "import" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        String fileName = "productClassImport" + System.currentTimeMillis() + ".xlsx";

        List<ProductClassImportDTO> notInsertList = new ArrayList<>();
        List<ProductClass> insertList = new ArrayList<>();
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
            jsonResult.setSuccess(false);
            jsonResult.setFailReasonShow("文件创建失败，请重试！");
            return jsonResult;
        }
        try {

            ExcelBoot.ImportBuilder(new FileInputStream(fileExcel), ProductClassImportDTO.class).importExcel(new ImportFunction<ProductClassImportDTO>() {
                @Override
                public void onProcess(int sheetIndex, int rowIndex, ProductClassImportDTO infoImportDTO) {
                    StringBuffer stringBuffer = new StringBuffer();
                    ProductClass productClass = productClassMapping.ImportToPO(infoImportDTO);
                    ProductClassAttribute classAttribute = productClassAttributeMapping.ImportToPO(infoImportDTO);
                    // 名称
                    if (StringUtil.isEmpty(infoImportDTO.getPrclName())) {
                        stringBuffer.append("第" + rowIndex + "行,名称不能为空！");
                    }

                    // 英文名称
                    if (StringUtil.isEmpty(infoImportDTO.getPrcaNameEn())) {
                        stringBuffer.append("第" + rowIndex + "行,英文名称不能为空！");
                    }
                    // 中文名称
                    if (StringUtil.isEmpty(infoImportDTO.getPrcaNameCh())) {
                        stringBuffer.append("第" + rowIndex + "行,中文名称不能为空！");
                    }

					// 如果错误信息不为空说明有错误，追加到错误信息列表
                    if (StringUtil.isNotEmpty(stringBuffer.toString())) {                   
                        notInsertList.add(infoImportDTO);
                        errorList.add(stringBuffer.toString());
                    } else { 
                        // 追加到插入列表
                        insertList.add(productClass);
                    }
                }

                @Override
                public void onError(ErrorEntity errorEntity) {
                    ProductClassImportDTO importDTO = new ProductClassImportDTO();
                    importDTO.setTip("第" + errorEntity.getCellIndex() + "列导入失败，" + errorEntity.getErrorMessage() + "。解析错误，无法复制信息");
                    importDTO.setOldRow(errorEntity.getRowIndex());
                    notInsertList.add(importDTO);
                }
            });
        } catch (FileNotFoundException e) {
            jsonResult.setSuccess(false);
            jsonResult.setFailReasonShow("Excel导入失败，请重试！");
            e.printStackTrace();
        }

        // 插入
		saveBathch(insertList);

        if (ListUtil.isNotEmpty(notInsertList)) {
            // 导出
            jsonResult.buildTrueNew();
            jsonResult.setTip(exportNoInsertProductClass(notInsertList));
            return jsonResult;
        }

        jsonResult.buildTrueNew();
        return jsonResult;
    }


    /**
     * 导出插入不成功的数据
     *
     * @param notInsertList
     * @return
     */
    public String exportNoInsertProductClass(List<ProductClassImportDTO> notInsertList) {
        if (ListUtil.isNotEmpty(notInsertList)) {

            String outputpath = "excel" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            String mypath = outputpath + File.separator + System.currentTimeMillis() + "_ProductClassExport.xlsx";
            String fileName = basePath + mypath;
            File file1 = new File(basePath + outputpath);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(new File(fileName));
                ExcelBoot.ExportBuilder(fileOutputStream,
                                "Sheet1", ProductClassImportDTO.class)
                        .exportStream(null, new ExportFunction<QueryWrapper, ProductClassImportDTO>() {
                            @Override
                            public List<ProductClassImportDTO> pageQuery(QueryWrapper queryWrapper, int i, int i1) {
                                return notInsertList;
                            }

                            @Override
                            public Object convert(ProductClassImportDTO t) {
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
```



导出模板

```java
@Data
public class TblEventlogExportDTO {

    @ExportField(columnName = "发生位置")
    private String happendPlace;

    @ExportField(columnName = "班次名称")
    private String ClassName;

    @ExportField(columnName = "事件名称")
    private String happenEventName;

    @ExportField(columnName = "呼叫时间")
    private Date HappenTime;

    @ExportField(columnName = "响应时间")
    private Date ResponseTime;

    @ExportField(columnName = "解除时间")
    private Date OverTime;

    @ExportField(columnName = "异常呼叫等待时间(分)")
    private String errorWaitTime;

    @ExportField(columnName = "异常处理时间(分)")
    private String errorHandleTime;

    @ExportField(columnName = "备注")
    private String Remark;

}
```



导出

```java
	
	private JsonResult exportEventLog(List<TblEventlogVO> tblEventlogVOS) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.buildTrueNew();
        
        TblEventlogQueryRequest queryQaram = new TblEventlogQueryRequest();
        
        List<TblEventlogExportDTO> eventlogExportDTOS = tblEventlogMapping.VOToExportDTO(tblEventlogVOS);
        String outputpath = "excel" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        String mypath = outputpath + File.separator + System.currentTimeMillis() + "_EventLog.xlsx";
        String fileName = basePath + mypath;
        File file1 = new File(basePath + outputpath);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        try {
            ExcelBoot.ExportBuilder(new FileOutputStream(new File(fileName)), "Sheet1", TblEventlogExportDTO.class)
                    .exportStream(queryQaram, new ExportFunction<TblEventlogQueryRequest, TblEventlogExportDTO>() {
                        @Override
                        public List<TblEventlogExportDTO> pageQuery(TblEventlogQueryRequest param, int pageNum, int pageSize) {
                            return eventlogExportDTOS;
                        }

                        @Override
                        public Object convert(TblEventlogExportDTO queryResult) {
                            return queryResult;
                        }
                    });
        } catch (FileNotFoundException e) {
            jsonResult.buildFalseNew(RequestEnum.REQUEST_ERROR_EXCEL_EXPORT);
            throw new RuntimeException(e);
        }
        // 如果出现过错误就返回
        if (!jsonResult.isSuccess()) {
            return jsonResult;
        }
        String fileDownLoadPath = File.separator + mypath;
        jsonResult.setTip(fileDownLoadPath.replaceAll(Pattern.quote("\\"), "/"));
        return jsonResult;
    }

```

