package com.coolgua.mail.service;

import com.coolgua.mail.domain.BlackList;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * Created by lihongde on 2018/1/27 13:27.
 */
public interface BlackListService {

    PageInfo<BlackList> findPageBlackList(Integer orgId, String recipient, int page, int size);

    void addBlackList(BlackList blackList);

    void remove(List<String> ids);

}
