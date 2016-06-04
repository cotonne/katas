package noeffect;

import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

public class XmlAggregatorShould {

	@Test
	public void return_the_good_information() throws Exception {
		URL url = this.getClass().getResource("/staff.xml");
		File fXmlFile = new File(url.getFile()); 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		Assert.assertEquals(2, XmlAggregator.aggregate(doc).length);
	}
	
}
