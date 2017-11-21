package cn.com.do1.component.contact.department.thread;

import java.io.File;

import cn.com.do1.component.util.ImportResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.common.vo.ResultVO;
import cn.com.do1.component.contact.contact.thread.BatchImportThread;
import cn.com.do1.component.contact.department.service.IDepartmentMgrService;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.ImportErrorVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.dqdp.core.DqdpAppContext;

public class BatchDeptToUserImportThread implements Runnable{
	private final static transient Logger logger = LoggerFactory.getLogger(BatchImportThread.class);
	
	private String id;
	
	private File file;
	private String fileName;
	private UserOrgVO userOrgVO;
	private IContactService contactService;
	private IDepartmentMgrService departmentService;
	
	private String type;
	
	
	public BatchDeptToUserImportThread(File file,String fileName,UserOrgVO userOrgVO,String type,String id){

    	logger.debug("BatchDeptToUserImportThread "+file.getPath()+"size"+file.length());
		this.id=id;
		this.file=file;
		this.fileName=fileName;
		this.userOrgVO=userOrgVO;
		this.type=type;
		this.contactService = DqdpAppContext.getSpringContext().getBean(
				"contactService", IContactService.class);
		this.departmentService = DqdpAppContext.getSpringContext().getBean(
				"departmentService", IDepartmentMgrService.class);
		
		this.type=type;
	}
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		logger.debug("导入部门负责人开始,orgId:"+userOrgVO.getOrgId()+",corpId："+userOrgVO.getCorpId());
		ResultVO resultvo=new ResultVO();
		ImportResultUtil.putResultObject(id, resultvo);
		ImportErrorVO error=new ImportErrorVO();
		ImportResultUtil.putErrorObject(id, error);
		try{
	    	logger.debug("BatchDeptToUserImportThread run "+file.getPath()+"size"+file.length());
			departmentService.importDeptToUserExcel(file, fileName, userOrgVO, id);
			logger.debug("导入部门负责人结束,orgId:"+userOrgVO.getOrgId()+",corpId："+userOrgVO.getCorpId());
		}catch(BaseException e){
			logger.info("导入部门负责人失败orgId："+userOrgVO.getOrgId(), e);
		}catch(Exception e){
			logger.info("导入部门负责人失败orgId："+userOrgVO.getOrgId(), e);
		}finally{
			resultvo.setFinish(true);
			error.setFinish(true);
			file.delete();
		}
	}

}
