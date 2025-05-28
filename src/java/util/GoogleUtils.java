/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import java.io.IOException;
import java.net.ProtocolException;
import org.json.JSONObject;
import constant.IConstant;



/**
 *
 * @author duong
 */
public class GoogleUtils {
    public static String getToken(String code) throws ProtocolException, IOException {
   String response = Request.Post(IConstant.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(
                        Form.form()
                                .add("client_id", IConstant.GOOGLE_CLIENT_ID)
                                .add("client_secret", IConstant.GOOGLE_CLIENT_SECRET)
                                .add("redirect_uri", IConstant.GOOGLE_REDIRECT_URI)
                                .add("code", code)
                                .add("grant_type", IConstant.GOOGLE_GRANT_TYPE)
                                .build()
                )
                .execute()
                .returnContent()
                .asString();

        // Parse JSON lấy access token
        JSONObject jobj = new JSONObject(response);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }
}
