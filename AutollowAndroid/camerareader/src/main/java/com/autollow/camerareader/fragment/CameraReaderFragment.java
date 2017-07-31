package com.autollow.camerareader.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.autollow.camerareader.R;
import com.autollow.camerareader.constant.CameraReader;
import com.autollow.camerareader.util.BarcodeDetector;
import com.autollow.camerareader.util.ImageProcessor;
import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by radsen on 7/12/17.
 */

public class CameraReaderFragment extends Fragment implements TextureView.SurfaceTextureListener,
        ImageReader.OnImageAvailableListener {

    public static final String TAG = CameraReaderFragment.class.getSimpleName();
    private static final int MAX_IMAGES = 3;

    private TextureView surface;

    private String cameraId;
    private Size imageDimension;
    private CameraDevice mCamera;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession cameraCaptureSessions;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private ImageReader imgReader;

    private CameraCaptureSession.CaptureCallback captureListener =
            new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request, @NonNull
                                               TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            createCameraPreview();
        }
    };

    private BarcodeDetector mBarcodeDetector;
    private ImageProcessor mImageProcessor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_reader, container, false);

        surface = (TextureView) view.findViewById(R.id.tv_camera_preview);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        surface.setSurfaceTextureListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startBackgroundThread();
        if(surface.isAvailable()){
            openCamera();
            transformImagePreview(surface.getWidth(), surface.getHeight());
        } else {
            surface.setSurfaceTextureListener(this);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        stopBackgroundThread();
        super.onPause();
    }

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getActivity()
                .getSystemService(Context.CAMERA_SERVICE);

        try {

            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CameraReader.REQUEST_CAMERA_PERMISSION);
                return;
            }

            imgReader = ImageReader.newInstance(imageDimension.getWidth(),
                    imageDimension.getHeight(), ImageFormat.YUV_420_888, MAX_IMAGES);
            imgReader.setOnImageAvailableListener(this, mBackgroundHandler);

            List barcodeFormats = new ArrayList<BarcodeFormat>();
            barcodeFormats.add(BarcodeFormat.PDF_417);
            mBarcodeDetector = new BarcodeDetector.Builder()
                    .setBarcodeFormats(barcodeFormats)
                    .build();

            mImageProcessor = new ImageProcessor(mBarcodeDetector, imageDimension);

            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    mCamera = camera;
                    createCameraPreview();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    mCamera.close();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    mCamera.close();
                    mCamera = null;
                }
            }, mBackgroundHandler);

        } catch (CameraAccessException camAccessEx){
            Log.d(TAG, camAccessEx.getMessage());
        }
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = surface.getSurfaceTexture();
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            captureRequestBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            List surfacesList = new ArrayList();

            Surface surface = new Surface(texture);
            surfacesList.add(surface);
            captureRequestBuilder.addTarget(surface);

            Surface readerSurface = imgReader.getSurface();
            if(readerSurface != null){
                surfacesList.add(readerSurface);
                captureRequestBuilder.addTarget(readerSurface);
            }

            mCamera.createCaptureSession(surfacesList, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (null == mCamera) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.d(TAG, "Configuration Change");
                }
            }, mBackgroundHandler);

        } catch (CameraAccessException camAccessEx){
            Log.d(TAG, camAccessEx.getMessage());
        }
    }

    private void updatePreview() {
        if(null == mCamera) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null,
                    mBackgroundHandler);
        } catch (CameraAccessException camAccessEx) {
            Log.d(TAG, camAccessEx.getMessage());
        }
    }

    private void transformImagePreview(int width, int height){
        if(imageDimension == null || surface == null){
            return;
        }

        Matrix matrix = new Matrix();
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        RectF textureViewRectF = new RectF(0, 0, width, height);
        RectF previewRectF = new RectF(0, 0, imageDimension.getHeight(), imageDimension.getWidth());
        float centerX = textureViewRectF.centerX();
        float centerY = textureViewRectF.centerY();

        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            previewRectF.offset(centerX - previewRectF.centerX(), centerY - previewRectF.centerY());
            matrix.setRectToRect(textureViewRectF, previewRectF, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float) width/imageDimension.getWidth(),
                    (float) height/imageDimension.getHeight());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }

        surface.setTransform(matrix);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureAvailable");
        openCamera();
        transformImagePreview(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(TAG, "onSurfaceTextureDestroyed");
        if(mCamera != null){
            mCamera.close();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //Log.d(TAG, "onSurfaceTextureUpdated");
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        Log.d(TAG, "onImageAvailable");
        mImageProcessor.setImage(reader.acquireLatestImage());
        mBackgroundHandler.post(mImageProcessor);
    }
}
