package noeffect;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlAggregator {

	private static EvaluationService evaluationService = new EvaluationService();

	public static Object[] aggregate(Document doc) throws Exception {
		NodeList nList = doc.getElementsByTagName("staff");
		Object[] res = new Object[nList.getLength()];
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
					
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String name = eElement.getElementsByTagName("firstname").item(0).getTextContent();
				String id = eElement.getAttribute("id");
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath.compile("/salary/current");
				
				NodeList nl = (NodeList) expr.evaluate(eElement, XPathConstants.NODESET);
				String math = nl.toString();
				String evaluation = evaluationService.getEvaluation(id);
				evaluation = cleanText(evaluation);
				URL url = XmlAggregator.class.getClass().getResource("/"+evaluation);
				File fXmlFile = new File(url.getFile()); 
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document xmlEval = dBuilder.parse(fXmlFile);
				expr = xpath.compile("/evaluation/result");
				final NodeList nodel = (NodeList)expr.evaluate(xmlEval, XPathConstants.NODESET);
				
				res[temp] = new Serializable() {
					String Name = name;
					String Id = id;
					String calculation = math;
					String Color = XmlAggregator.toColor(nodel);
					String Details = toDetails(nl);
					private String toDetails(NodeList nl) {
						return nl.toString();
					}
				};
			}
		}
		return res;
	}

	protected static String toColor(NodeList nl) {
		return null;
	}

	private static String cleanText(String evaluation) {
		return evaluation.replace("The evaluation has been stored here : ", "");
	}

}
