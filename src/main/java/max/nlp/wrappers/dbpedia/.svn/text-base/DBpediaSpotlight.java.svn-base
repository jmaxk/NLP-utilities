package max.nlp.wrappers.dbpedia;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import max.nlp.dal.annotations.DBpediaAnnotation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DBpediaSpotlight {
	private final String USER_AGENT = "Mozilla/5.0";
	private final String url = "http://localhost:2222/rest/annotate";

	public static void main(String[] args) throws Exception {

		DBpediaSpotlight dbpediaConnection = new DBpediaSpotlight();

		String input = "President Obama called Wednesday on Congress to extend a tax break";

		dbpediaConnection.sendPost(input);

	}

	// must start server first 
	public List<DBpediaAnnotation> sendPost(String input) {
		List<DBpediaAnnotation> annotations = new ArrayList<DBpediaAnnotation>();

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept", "application/json");
			@SuppressWarnings("deprecation")
			String urlParameters = "text=" + URLEncoder.encode(input);

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			// System.out.println(response.toString());

			JSONObject json = new JSONObject(response.toString());
			JSONArray data = json.getJSONArray("Resources");
			if (data != null) {
				for (int i = 0; i < data.length(); i++) {

					DBpediaAnnotation annotation = new DBpediaAnnotation();
					JSONObject o = data.getJSONObject(i);

					int startOffset = o.getInt("@offset");
					int endOffset = o.getString("@surfaceForm").length();
					String uri = o.getString("@URI");

					annotation.setendOffset(endOffset);
					annotation.setStartOffset(startOffset);
					annotation.setUri(uri);

					annotations.add(annotation);
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return annotations;
	}
}
