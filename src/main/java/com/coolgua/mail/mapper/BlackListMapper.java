package com.coolgua.mail.mapper;

import com.coolgua.mail.domain.BlackList;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihongde on 2018/1/27 13:32.
 */
@Mapper
public interface BlackListMapper {

  List<BlackList> findBlackList(@Param("orgId") Integer orgId, @Param("recipient") String recipient);

  void addBlackList(BlackList blackList);

  void remove(@Param("ids") List<String> ids);
}
