package com.autollow.camerareader.util;

import android.graphics.ImageFormat;
import android.media.Image;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;

import java.nio.ByteBuffer;

/**
 * Created by radsen on 7/14/17.
 */

public class ImageProcessor implements Runnable {

    public static final String TAG = ImageProcessor.class.getSimpleName();

    private Image mImage;
    private final BarcodeDetector mDetector;
    private final Size mPreviewSize;

    public ImageProcessor(BarcodeDetector barcodeDetector, Size previewSize) {
        mDetector = barcodeDetector;
        mPreviewSize = previewSize;
    }

    public ImageProcessor(Image image, BarcodeDetector barcodeDetector, Size previewSize) {
        mImage = image;
        mDetector = barcodeDetector;
        mPreviewSize = previewSize;
    }

    public void setImage(Image mImage) {
        this.mImage = mImage;
    }

    @Override
    public void run() {
        if (mImage == null){
            return;
        }

        try{
            ByteBuffer byteBuffer = mImage.getPlanes()[0].getBuffer();

            Frame outputFrame = new Frame.Builder()
                    .setImageData(byteBuffer, mPreviewSize.getWidth(),
                            mPreviewSize.getHeight(), ImageFormat.NV16)
                    .build();

            SparseArray<Barcode> barcodes = mDetector.detect(outputFrame);
            if(barcodes.size() != 0){
                String rawValue = barcodes.valueAt(0).displayValue;
                Log.d(TAG, rawValue);
            }

        } finally {
            if (mImage != null)
                mImage.close();
        }
    }

}
