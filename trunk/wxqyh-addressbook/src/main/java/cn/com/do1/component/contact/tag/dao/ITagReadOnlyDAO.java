package cn.com.do1.component.contact.tag.dao;

import cn.com.do1.common.framebase.dqdp.IBaseDAO;
import cn.com.do1.component.addressbook.tag.vo.QyTagPageInfoVO;
import cn.com.do1.component.addressbook.tag.vo.QyTagRefPageVO;

import java.sql.SQLException;
import java.util.List;

/**
 * 只读DAO专用
 * 
 * @date 2015-10-28
 */
public interface ITagReadOnlyDAO extends IBaseDAO {
    List<QyTagPageInfoVO> getTagPageInfoList(String orgId, int tagRang, int status) throws SQLException;

    List<QyTagPageInfoVO> getTagPageInfoList(String orgId, int status) throws SQLException;

    List<QyTagRefPageVO> getTagRefPageVOList(String tagId) throws SQLException;

    List<String> getTagRefMenberIdListByTagIds(List<String> tagIds, int menberType) throws SQLException;

    List<String> getTagIdsByWxTagIds(String orgId, String[] wxTagIds) throws SQLException;
}
