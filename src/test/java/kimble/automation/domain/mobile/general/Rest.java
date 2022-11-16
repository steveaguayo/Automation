package kimble.automation.domain.mobile.general;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Benjamin on 7/7/2015.
 */
public class Rest {

    public static Map<String, Object> newAttachment(String aFilePath, String aExpenseItemId) throws Exception {
        Map<String, Object> fields = new HashMap();
        fields.put("Name", aFilePath);
        fields.put("Body", Functions.toBase64String(TnXContext.openFile(aFilePath)));
        fields.put("ParentId", aExpenseItemId);
        return fields;
    }

    public static class CreateResponse {
        public String id;
        public boolean success;
        public List<String> errors;
    }

    public static class FailResponse {
        public String message;
        public String errorCode;
        public List<String> fields;
    }
}
