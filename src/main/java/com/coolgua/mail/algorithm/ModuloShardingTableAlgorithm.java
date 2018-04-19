package com.coolgua.mail.algorithm;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import java.util.Collection;

/**
 * Created by lihongde on 2018/2/8 14:10.
 */
public class ModuloShardingTableAlgorithm implements PreciseShardingAlgorithm<Integer> {

  @Override
  public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<Integer> shardingValue) {
    for (String each : tableNames) {
      if (each.endsWith(shardingValue.getValue() % 5 + "")) {
        return each;
      }
    }
    throw new UnsupportedOperationException();
  }
}
