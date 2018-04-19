package com.coolgua.mail.service.impl;

import com.coolgua.mail.domain.BlackList;
import com.coolgua.mail.mapper.BlackListMapper;
import com.coolgua.mail.service.BlackListService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Created by lihongde on 2018/1/27 13:31.
 */@Service
public class BlackListServiceImpl implements BlackListService {

  @Resource
  private BlackListMapper blackListMapper;

  @Override
  public PageInfo<BlackList> findPageBlackList(Integer orgId,String recipient, int page, int size) {
    PageHelper.startPage(page, size);
    List<BlackList> list = blackListMapper.findBlackList(orgId, recipient);
    return new PageInfo<>(list);
  }

  @Override
  public void addBlackList(BlackList blackList) {
    blackListMapper.addBlackList(blackList);
  }

  @Override
  public void remove(List<String> ids) {
    blackListMapper.remove(ids);
  }
}
