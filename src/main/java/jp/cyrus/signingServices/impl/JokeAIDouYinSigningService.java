package jp.cyrus.signingServices.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import jp.cyrus.constants.Configurations;
import jp.cyrus.signingServices.SigningService;
import jp.cyrus.utils.JsonHelper;

/**
 * JokeAI DouYin sign service based implementation.<br>
 * https://github.com/JokeAI/Sign-DouYin
 *
 * @author cyrus
 */
public class JokeAIDouYinSigningService implements SigningService {

	@Override
	public String signUrl(String url, long ts, long deviceId) {
		try {
			// Create request parameters
			Map<String, Object> requestData = new HashMap<>();
			requestData.put("url", url);
			String parameterString = JsonHelper.getObjectMapper().writeValueAsString(requestData);

			// Create request
			HttpPost httpPost = new HttpPost(Configurations.JOKE_AI_DOUYIN_API_BASE_URL + "/sign/");
			httpPost.setHeader("Content-type", "application/json;");
			httpPost.setEntity(new StringEntity(parameterString, StandardCharsets.UTF_8));
			try (CloseableHttpClient client = HttpClientBuilder.create().build();
					CloseableHttpResponse response = client.execute(httpPost)) {
				String responseString = EntityUtils.toString(response.getEntity());
				return JsonHelper.getObjectMapper().readTree(responseString).get("url").asText();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}