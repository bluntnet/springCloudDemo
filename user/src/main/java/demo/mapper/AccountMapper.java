package demo.mapper;

import demo.bean.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {
    Account loadByName(@Param("name") String name);
}
