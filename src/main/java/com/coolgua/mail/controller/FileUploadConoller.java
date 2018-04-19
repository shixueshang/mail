package com.coolgua.mail.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coolgua.common.util.ExcelUtil;
import com.coolgua.common.util.HttpClientUtil;
import com.coolgua.mail.exception.BadRequestException;
import com.coolgua.mail.exception.FailedReqeustException;
import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant.JSON_RESULT;
import com.coolgua.mail.util.StringUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by lihongde on 2018/2/5 9:11.
 */
@RestController
@RequestMapping(value = "/mail/file/{fp}/{orgId}/{userId}")
public class FileUploadConoller extends BaseController {

  private Logger logger = LoggerFactory.getLogger(FileUploadConoller.class);

  private static final String SYSTEM_CODE = "mail";
  @Value("${PAN_URL}")
  private String panUrl;

  /**
   * 添加数据源，上传本地文件
   * @param file
   * @return
   */
  @RequestMapping(value = "/uploadLocalSource", method = RequestMethod.POST)
  public AjaxJson uploadLocalSource(@RequestParam(value = "file", required = true) MultipartFile file, @PathVariable String fp, @PathVariable Integer orgId, @PathVariable String userId) {
    AjaxJson result = new AjaxJson(JSON_RESULT.OK);
    File convFile = null;
    try {
      String filename = file.getOriginalFilename();
      if (!StringUtil.isValidFileExt(filename)) {
        throw new BadRequestException("不支持的文件格式");
      }
      convFile = this.convertFile(file);
      List<String> fields = ExcelUtil.getExcelTitles(convFile.getAbsolutePath());
      Set<String> tempFields = new HashSet<>(fields);
      if(CollectionUtils.isEmpty(tempFields)){
        throw new BadRequestException("上传的文件没有表头");
      }
      String address = this.getRemoteFileAddress(convFile, session, fp, orgId, userId);
      Map<String, Object>  map = new HashMap<>();
      map.put("fields", tempFields);
      map.put("address", address);
      result.setMap(map);
    } catch (Exception e) {
      throw new FailedReqeustException("上传文件出错");
    }finally {
      if (convFile.exists()) {   //删除临时文件
        convFile.delete();
      }
    }
    return result;
  }


  /**
   * 上传邮件附件
   * @param file
   * @return
   */
  @RequestMapping(value = "/uploadMailAttachments", method = RequestMethod.POST)
  public AjaxJson uploadMailAttachments(@RequestParam(value = "file", required = true) MultipartFile file, @PathVariable String fp, @PathVariable Integer orgId, @PathVariable String userId){
    AjaxJson result = new AjaxJson(JSON_RESULT.OK);
    try {
      String filename = file.getOriginalFilename();
      if (!StringUtil.isValidFileExt(filename)) {
        throw new BadRequestException("不支持的文件格式");
      }
      File convFile = this.convertFile(file);
      String address = this.getRemoteFileAddress(convFile, session, fp, orgId, userId);
      Map<String, Object>  map = new HashMap<>();
      map.put("address", address);
      result.setMap(map);
    } catch (Exception e) {
      throw new FailedReqeustException("上传文件出错");
    }
    return result;
  }

  /**
   * 文件转换
   * @param file
   * @return
   */
  private File convertFile(MultipartFile file) {
    File convFile = new File(file.getOriginalFilename());
    try {
      convFile.createNewFile();
      FileOutputStream fos = new FileOutputStream(convFile);
      fos.write(file.getBytes());
      fos.close();
    } catch (IOException e) {
      logger.error("文件转换错误");
      e.printStackTrace();
    }
    return convFile;
  }

  /**
   * 上传文件，获得文件系统返回的文件地址
   *
   */
  private String getRemoteFileAddress(File convFile, HttpSession session, String fp, Integer orgId, String userId) {
    String getOrgPath = basePath + "/common/login/"+ fp +"/getCurOrg/"+ userId +".action";
    String json = HttpClientUtil.doGet(getOrgPath);
    JSONObject jsonObject = JSON.parseObject(json);
    if(!(Boolean)jsonObject.get("success")){
      throw new FailedReqeustException("获取当前组织机构信息失败");
    }
    Map<String, Object> curOrg = (Map<String, Object>)jsonObject.get("data");
    String appSecret = (String) curOrg.get("appSecret");
    String url = panUrl + "/v1" + "/" + orgId + "/" + SYSTEM_CODE + "/" + userId + "/" + appSecret + "/upload";
    Map<String, String> params = new HashMap<>();
    Map<String, String> jsonMap = new HashMap<>();
    jsonMap.put("type", "1");
    jsonMap.put("fileName", convFile.getName());
    params.put("strJson", JSON.toJSONString(jsonMap));
    String fileResult = HttpClientUtil.doPostMultipartFile(url, convFile, params);
    if (convFile.exists()) {   //删除临时文件
      convFile.delete();
    }
    Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(fileResult);
    if ((Integer) resultMap.get("code") != 0) {
      throw new BadRequestException("上传文件失败，请检查相关参数");
    }
    Map<String, String> address = (Map<String, String>) resultMap.get("data");
    return address.get("address");
  }

}
