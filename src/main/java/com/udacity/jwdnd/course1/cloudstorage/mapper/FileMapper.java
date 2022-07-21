package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findFileById(Long fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> findFilesByUserId(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Update("UPDATE FILES SET filename = #{noteTitle}, contenttype = #{noteDescription}, filesize = #{fileSize}, filedata = #{fileData} WHERE fileId = #{fileId}")
    int update(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int delete(Long fileId);
}
