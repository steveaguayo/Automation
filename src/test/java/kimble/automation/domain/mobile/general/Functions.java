package kimble.automation.domain.mobile.general;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;

import kimble.automation.domain.mobile.ActivityMob;
import kimble.automation.domain.mobile.LocationMob;
import kimble.automation.domain.mobile.PlanTaskAssignmentMob;
import kimble.automation.domain.mobile.TimeCategoryMob;
import kimble.automation.domain.mobile.TimeEntryMob;
import kimble.automation.domain.mobile.TypesMob.UsageAllocationType;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;

import com.google.common.collect.Lists;

public class Functions {

    public enum ImageSize {
        large(1600, 70),
        medium(800, 70),
        small(400, 70),
        ;

        public final int maxDim;
        public final int quality;
        private ImageSize(int aMaxDim, int aQuality) {
            maxDim = aMaxDim; quality = aQuality;
        }
    }

    public static final BigInteger one = new BigInteger("1");

    public static String readFileToString(String path, Charset encoding) throws Exception {
        return readStreamToString(new FileInputStream(path), encoding);
    }

    public static String readStreamToString(InputStream aIs, Charset encoding) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedInputStream bis = new BufferedInputStream(aIs);
        byte[] data = new byte[10000];
        int len;
        while ((len = bis.read(data)) > -1)
            sb.append(new String(data, 0, len, encoding));
        return sb.toString();
    }

    public static <T> T deserialize(String aJson, Class<T> aType) throws Exception {
        return TnXContext.getMapper().readValue(aJson, aType);
    }

    public static <T> T deserialize(InputStream aJson, Class<T> aType) throws Exception {
        return TnXContext.getMapper().readValue(aJson, aType);
     }

    public static <T> T deserialize(byte[] aJson, Class<T> aType) throws Exception {
        return TnXContext.getMapper().readValue(aJson, aType);
    }

    public static void serialize(Object o, OutputStream out) throws Exception {
        TnXContext.getMapper().writerWithDefaultPrettyPrinter().writeValue(out, o);
     }

    public static LocationMob parseLocation(String aLocationString) {
        LocationMob l = new LocationMob();

        if (aLocationString == null)
            return l;

        String[] split1 = aLocationString.split("\\(");
        l.setName(split1[0].trim());
        if (split1.length > 1) {
            String[] split2 = split1[1].replace(")", "").split(",");
            l.setLatitude(Double.parseDouble(split2[0].trim()));
            l.setLongitude(Double.parseDouble(split2[1].trim()));
        }

        return l;
    }

    public static String composeLocation(String aName, Double aLatitude, Double aLongitude) {
        if (aLatitude == 0 || aLongitude == 0)
            return "";
        StringBuilder sb = new StringBuilder(aName == null ? "" : aName);
        if (aLatitude != null && aLongitude != null) {
            sb.append('(').append(aLatitude).append(',').append(aLongitude).append(')');
        }
        return sb.toString();
    }

    public static final List<String> categorisableAllocationTypes = Lists.newArrayList(
            UsageAllocationType.Category.name(),
            UsageAllocationType.Task.name(),
            UsageAllocationType.CategoryAndTask.name());

    public static void validateForecastTimeEntriesForSave(Iterable<TimeEntryMob> aFtes) throws Exception {
        for (TimeEntryMob te : aFtes)
            validateForecastTimeEntryForSave(te);
    }

    public static void validateForecastTimeEntryForSave(TimeEntryMob aFte) throws Exception {
        if (!aFte.getStatus().equals(TimeEntryMob.Status.Forecast))
            throw new Exception(TimeEntryMob.class.getSimpleName() + " isn't of type : " + TimeEntryMob.Status.Forecast.name());
        UsageAllocationType uat = aFte.getActivityRateBand().getUsageAllocationType();
        if (uat == UsageAllocationType.Category || uat == UsageAllocationType.CategoryAndTask)
            validateForecastTimeCategories(aFte);
        if (uat == UsageAllocationType.Task || uat == UsageAllocationType.CategoryAndTask)
            validateTimeEntryTask(aFte);
    }

    public static void validateTimeEntryTask(TimeEntryMob aFte) throws Exception {
        if (aFte.getPlanTaskAssignment() == null)
            throw new Exception("The " + PlanTaskAssignmentMob.class.getSimpleName() + " of " + TimeEntryMob.class.getSimpleName() + " : " + aFte.getId() + " is null");
        if (aFte.getDraftRemainingEffort() == null)
            throw new Exception("The drafted remaining effort of " + TimeEntryMob.class.getSimpleName() + " : " + aFte.getId() + " is 0");
    }

    public static void validateForecastTimeCategories(TimeEntryMob aFte) throws Exception {
        ActivityMob a = aFte.getAssignment().getActivity();
        for (TimeCategoryMob tc : a.getTimeCategories())
            validateForecastTimeCategory(aFte, tc);
    }

    public static void validateForecastTimeCategory(TimeEntryMob aFte, TimeCategoryMob aTimeCategory) throws Exception {
        if (aTimeCategory.getIsMandatory()) {
            String category = null;
            switch (aTimeCategory.getDisplaySequence()) {
                case 1:
                    category = aFte.getCategory1();
                    break;
                case 2:
                    category = aFte.getCategory2();
                    break;
                case 3:
                    category = aFte.getCategory3();
                    break;
                case 4:
                    category = aFte.getCategory4();
                    break;
                case 5:
                    category = aFte.getCategory5();
                    break;
                case 6:
                    category = aFte.getCategory6();
                    break;
            }
            if (category == null || category.length() == 0)
                throw new Exception("The category number " + aTimeCategory.getDisplaySequence() + " in " + TimeEntryMob.class.getSimpleName() + " is invalid");
        }
    }

    public static String toBase64String(File aFile) throws Exception {
        return toBase64String(aFile, 10000);
    }

    public static String toBase64String(File aFile, int aBatchSize) throws Exception {
        InputStream is = new Base64InputStream(new FileInputStream(aFile));
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, Charset.defaultCharset().toString());
        return writer.toString();
    }

	/*public static void fromBase64String(InputStream aIs, File aFile) throws Exception {
        fromBase64String(aIs, aFile, 10000);
	}

	public static void fromBase64String(InputStream aIs, File aFile, int aBatchSize) throws Exception {
		OutputStream os = new FileOutputStream(aFile);
		byte[] batch = new byte[aBatchSize];
		int bytesRead = -1;
		HttpEntity entity;
		while((bytesRead = aIs.read(batch)) > 0)
			os.write(Base64.decode(batch, 0, bytesRead, Base64.DEFAULT));
	}*/

    public static <T> Class<T[]> getArrayType(Class<T> aComponentType) throws Exception {
        Class cls = Array.newInstance(aComponentType, 0).getClass();
        return (Class<T[]>) cls;
    }

    public static InputStream createAttachmentInputStream(String aParentId, String aName, final File aAttachment) throws Exception {
        String str1 = (new StringBuilder("{")
                .append("\"ParentId\" : \"").append(aParentId).append("\",")
                .append("\"Name\" : \"").append(aName).append("\",")
                .append("\"Body\" : \"")).toString();
        InputStream is1 = new ByteArrayInputStream(str1.getBytes("UTF-8"));

        InputStream attachmentIs = Streams.createInputStream(new FileInputStream(aAttachment), new Streams.OutputStreamConverter() {
            @Override
            public OutputStream convert(OutputStream aOs) throws Exception {
                return new Base64OutputStream(aOs);
            }
        });

        String str2 = "\"}";
        InputStream is3 = new ByteArrayInputStream(str2.getBytes("UTF-8"));

        return new BufferedInputStream(Streams.secuenceStreams(is1, attachmentIs, is3));
    }

    public static HttpEntity createAttachmentFileEntity(String aParentId, String aName, final File aAttachment) throws Exception {
        return new InputStreamEntity(createAttachmentInputStream(aParentId, aName, aAttachment), -1);
    }

    /**
     * Not in use - I have left this here just in case  - Benjamin
     *
     * @param aParentId
     * @param aName
     * @param aAttachment
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
	public static FileEntity createAttachmentFileEntity2(String aParentId, String aName, final File aAttachment) throws Exception {
        InputStream is = createAttachmentInputStream(aParentId, aName, aAttachment);
        File tempFile = TnXContext.openFile(aName + ".tmp");
        if(!tempFile.exists())
            tempFile.createNewFile();
        OutputStream os = new FileOutputStream(tempFile);
        IOUtils.copy(is, os);
        os.flush();
        is.close();
        os.close();
        return new FileEntity(tempFile, ContentType.APPLICATION_JSON.getMimeType());
    }

    /**
     * Not in use - I have left this here just in case  - Benjamin
     *
     * @param aParentId
     * @param aName
     * @param aAttachment
     * @return
     * @throws Exception
     */
    public static HttpEntity createAttachmentFileEntity3(String aParentId, String aName, final File aAttachment) throws Exception {
        String str1 = (new StringBuilder("{")
                .append("\"ParentId\" : \"").append(aParentId).append("\",")
                .append("\"Name\" : \"").append(aName).append("\",")
                .append("\"Body\" : \"")).toString();
        InputStream is1 = new ByteArrayInputStream(str1.getBytes("UTF-8"));

        InputStream attachmentIs = new FileInputStream(aAttachment);
        String str2 = "\"}";
        InputStream is3 = new ByteArrayInputStream(str2.getBytes("UTF-8"));

        File tempFile = TnXContext.openFile(aName + ".tmp");
        if(!tempFile.exists())
            tempFile.createNewFile();

        OutputStream os = new FileOutputStream(tempFile);
        IOUtils.copy(is1, os);
        is1.close();
        os.flush();

        OutputStream bos = new Base64OutputStream(os);
        IOUtils.copy(attachmentIs, bos);
        attachmentIs.close();
        os.flush();

        IOUtils.copy(is3, os);
        is3.close();
        os.flush();

        os.close();

        InputStreamEntity entity = new InputStreamEntity(new FileInputStream(tempFile), tempFile.length());
        return entity;
    }

//    public static String toHtmlP(int aContentId) { return toHtmlP(TnXContext.getInstance().getString(aContentId)); }
//
//    public static String toHtmlA(int aHrefId, int aNameId) { return toHtmlA(TnXContext.getInstance().getString(aHrefId), TnXContext.getInstance().getString(aNameId)); }
//
//    public static String toHtmlP(String aContent) { return "<p>" + aContent + "</p>"; }
//
//    public static String toHtmlA(String aHref, String aName) { return "<a href='" + aHref + "'>" + aName + "</a>"; }

    /**
     *
     * @param aImageUri : the saveable image Uri
     * @param aSize : The limiting dimensions of the image. If the image is larger, it will be scaled down.
     * @return
     */
//    public static void saveImageToAppNamespace(Uri aImageUri, File aOutFile, ImageSize aSize, Actions.ActionCallback aCallback) {
//        CompressJpegAction action = new CompressJpegAction(aImageUri, aOutFile, aSize.maxDim, aSize.quality);
//        action.setCallback(aCallback);
//        action.execute();
//    }

}
