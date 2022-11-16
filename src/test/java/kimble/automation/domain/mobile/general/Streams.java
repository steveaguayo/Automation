package kimble.automation.domain.mobile.general;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64InputStream;
//import org.apache.commons.codec.binary.Base64InputStream;
//import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;

/**
 * Created by Benjamin on 7/9/2015.
 */
public class Streams {

    public static InputStream createInputStream(final InputStream aFeeder, final OutputStreamConverter aConverter) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final PipedInputStream pis = new PipedInputStream(){
            public void close() throws IOException {
                super.close();
                latch.countDown();
            }
        };
        final PipedOutputStream pout = new PipedOutputStream(pis);
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            //InputStream is = new FileInputStream(aAttachment);
                            OutputStream os = aConverter.convert(pout);
                            IOUtils.copy(aFeeder, os);
                            os.flush();
                            aFeeder.close();
                            os.close();
                            latch.await();
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to convert Base64 outputstream  to input stream", e);
                        }
                    }
                }
        ).start();

        return pis;
    }

    public static InputStream secuenceStreams(InputStream... aStreams) {
        if(aStreams == null || aStreams.length == 0 )
            return null;
        InputStream is = aStreams[0];
        int i = 1;
        while(is != null && i < aStreams.length)
            is = new SequenceInputStream(is, aStreams[i++]);
        return is;
    }

    // OutputStream converters

    public interface OutputStreamConverter {
        OutputStream convert(OutputStream aOs) throws Exception;
    }

    public static class Base64OSEncoder implements OutputStreamConverter {
        public OutputStream convert(OutputStream aOs) throws Exception {
             return new org.apache.commons.codec.binary.Base64OutputStream(aOs);
        }
    }

    public static class BufferedOSConverter implements OutputStreamConverter {

        int size;

        public BufferedOSConverter(int aSize) { size = aSize; }

        public OutputStream convert(OutputStream aOs) throws Exception { return new BufferedOutputStream(aOs, size); }
    }

    // InputStream converters

    public interface InputStreamConverter {
        InputStream convert(InputStream aIs) throws Exception;
    }

    public static class Base64ISDecoder implements InputStreamConverter {
        public InputStream convert(InputStream aOs) throws Exception {
            return new Base64InputStream(aOs);
        }
    }

    public static class BufferedISConverter implements InputStreamConverter {

        int size;

        public BufferedISConverter(int aSize) { size = aSize; }

        public InputStream convert(InputStream aOs) throws Exception { return new BufferedInputStream(aOs, size); }
    }

    //public static

//    public static void compressToJpeg(Uri aUri, File aOutFile, int aReqSmallerDim, int aReqLargerDim, int aQuality) throws Exception {
//        OutputStream os = new BufferedOutputStream(new FileOutputStream(aOutFile));
//        if(!openImage(aUri, aReqSmallerDim, aReqLargerDim).compress(Bitmap.CompressFormat.JPEG, aQuality, os))
//            throw new Exception("Failed to compress the stream to jpeg target " + aOutFile.getName());
//        os.flush();
//        os.close();
//    }

//    public static Bitmap openImage(Uri aUri, int aReqSmallerDim, int aReqLargerDim) throws Exception {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        InputStream is = TnXContext.getInstance().getContentResolver().openInputStream(aUri);
//        BitmapFactory.decodeStream(is, null, options);
//        is.close();
//
//        int sampleSize = calculateImageSampleSize(aReqSmallerDim, aReqLargerDim, options);
//        options = new BitmapFactory.Options();
//        options.inSampleSize = sampleSize;
//        is = TnXContext.getInstance().getContentResolver().openInputStream(aUri);
//        return BitmapFactory.decodeStream(new BufferedInputStream(is), null, options);
//        //return BitmapFactory.decodeFile(aInFile.getAbsolutePath(), options);
//    }

//    public static int calculateImageSampleSize(int aReqSmallerDim, int aReqLargerDim, BitmapFactory.Options aOptions) {
//        return calculateImageSampleSize(aReqSmallerDim, aReqLargerDim, Math.min(aOptions.outWidth, aOptions.outHeight), Math.max(aOptions.outWidth, aOptions.outHeight));
//    }

    public static int calculateImageSampleSize(int aReqSmallerDim, int aReqLargerDim, int aExistingSmallerDim, int aExistingLargerDim) {
        int sample = 1;
        while(aExistingSmallerDim / sample > aReqSmallerDim || aExistingLargerDim / sample > aReqLargerDim)
            sample *= 2;
        return sample;
    }

//    public static String getMediaPath(Uri aMedia) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        CursorLoader loader = new CursorLoader(TnXContext.getInstance(), aMedia, projection, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

//    public static File getMediaFile(Uri aMedia) {
//        return new File(getMediaPath(aMedia));
//    }

    public static void zipFile(File aZippaple, File aZipped) throws Exception {
        InputStream is = new FileInputStream(aZippaple);
        ZipOutputStream os = new ZipOutputStream(new FileOutputStream(aZipped));
        try {
            os.putNextEntry(new ZipEntry(aZippaple.getName()));
            IOUtils.copy(is, os);
            os.closeEntry();
        }finally {
            is.close();
            os.close();
        }
    }

    public static void zipFiles(File aZipped, File... aZippaples) throws Exception {
        ZipOutputStream os = new ZipOutputStream(new FileOutputStream(aZipped));
        try {
            for (File zippable : aZippaples) {
                InputStream is = new FileInputStream(zippable);
                os.putNextEntry(new ZipEntry(zippable.getName()));
                IOUtils.copy(is, os);
                is.close();
                os.closeEntry();
             }
        } finally {
            os.close();
        }
    }
}
