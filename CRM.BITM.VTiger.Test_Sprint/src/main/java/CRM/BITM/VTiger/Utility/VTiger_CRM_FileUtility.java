package CRM.BITM.VTiger.Utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class VTiger_CRM_FileUtility {
	public String getDataFromPropertyFile(String key) throws Exception{
		FileInputStream fis = new FileInputStream("./src/test/resources/CRM.BITM.VTiger.DDT/VTiger_CRM_Common_Properties");
		Properties p = new Properties();
		p.load(fis);
		
		return p.getProperty(key);
	}
}