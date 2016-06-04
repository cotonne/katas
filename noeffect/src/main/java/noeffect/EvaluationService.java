package noeffect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class EvaluationService {

	public String getEvaluation(String id) {
		URL url = this.getClass().getResource("/eval_" + id + ".txt");
		File fXmlFile = new File(url.getFile());
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fXmlFile));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
