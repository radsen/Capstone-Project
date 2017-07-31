package com.autollow.camerareader.util;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by radsen on 7/21/17.
 */

public class BarcodeDetector extends Detector<Barcode> {

    private static final String TAG = BarcodeDetector.class.getSimpleName();

    private final MultiFormatReader mMultiFormatReader;
    private final float mOffsetPercentage;

    private BarcodeDetector(){
        throw new IllegalStateException("Default constructor called");
    }

    private BarcodeDetector(MultiFormatReader multiFormatReader, float percentage){
        mMultiFormatReader = multiFormatReader;
        mOffsetPercentage = percentage / 2.f;
    }

    @Override
    public SparseArray<Barcode> detect(Frame frame) {
        if(frame == null) {
            throw new IllegalArgumentException("No frame supplied.");
        } else {
            Frame.Metadata frameMetadata = frame.getMetadata();
            ByteBuffer imageData = frame.getGrayscaleImageData();
            byte[] data = new byte[imageData.capacity()];
            ((ByteBuffer) imageData.duplicate().clear()).get(data);

                int width = frameMetadata.getWidth();
                int height = frameMetadata.getHeight();

                int centerX = width / 2;
                int centerY = height / 2;

                int min = Math.min(width, height);
                int halfSize = (int) (mOffsetPercentage * min);
                int offsetX = centerX - halfSize;
                int offsetY = centerY - halfSize;

                // Go ahead and assume it's YUV rather than die.
                PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height,
                        offsetX, offsetY, 2*halfSize, 2*halfSize, false);

                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result rawResult = null;
                try {
                    rawResult = mMultiFormatReader.decodeWithState(bitmap);
                } catch (ReaderException re) {
                    Log.d(TAG, re.toString());
                } finally {
                    mMultiFormatReader.reset();
                }

                if (rawResult != null) {
                    SparseArray<Barcode> results = new SparseArray<>(1);
                    Barcode barcode = new Barcode(rawResult, frame.getMetadata(), offsetX, offsetY);
                    results.append(rawResult.hashCode(), barcode);
                    return results;
                }

            return new SparseArray<>(0);
        }
    }

    public static class Builder{
        private int mPercentage = 100;
        private Collection<BarcodeFormat> mDecodeFormats;

        public Builder(){}

        public BarcodeDetector.Builder setPercentage(int percentage){
            mPercentage = percentage;
            return this;
        }

        public BarcodeDetector.Builder setBarcodeFormats(Collection<BarcodeFormat> decodeFormats){
            mDecodeFormats = decodeFormats;
            return this;
        }

        public BarcodeDetector build(){
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, mDecodeFormats);

            MultiFormatReader multiFormatReader = new MultiFormatReader();
            multiFormatReader.setHints(hints);

            return new BarcodeDetector(multiFormatReader, 0.01f * mPercentage);
        }
    }
}
