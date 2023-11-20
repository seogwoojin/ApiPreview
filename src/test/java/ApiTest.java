import org.assertj.core.api.Assertions;
import org.example.Notice;
import org.example.NoticeService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@SpringBootTest
public class ApiTest {
    private final NoticeService noticeService;
    public ApiTest(NoticeService noticeService) throws IOException {
        this.noticeService = noticeService;
    }
    @Test
    void test1() {

        String key = "202310746OIX95182";
        String surl = "https://wise.uos.ac.kr/uosdoc/api.ApiApiMainBd.oapi";
        String result;
        try {
            URL url = new URL(surl + "?apiKey=" + key);
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "EUC-KR"));

            StringBuilder sb = new StringBuilder();
            String line;

            //System.out.println(sb);
            JSONObject jsonObject = XML.toJSONObject(bf);
            jsonObject.put("ro", "루트");
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("root");
            JSONObject jsonObject2 = (JSONObject) jsonObject1.get("schList");
            JSONArray jsonArray = (JSONArray) jsonObject2.get("list");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                    Notice notice = new Notice();
                    notice.setSch_date((String) jsonObj.get("sch_date"));
                    notice.setMonth((String) jsonObj.get("month"));
                    notice.setYear((String) jsonObj.get("year"));
                    notice.setContent((String) jsonObj.get("content"));
                    System.out.println(notice.getContent());
                    noticeService.create(notice);
                }
            }
            String json = jsonObject2.toString(4);

            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("--------------끝-----------");
        Assertions.assertThat(key).as(surl);
    }

}
