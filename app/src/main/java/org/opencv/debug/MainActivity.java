package org.opencv.debug;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.R;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();

    private TextView mTextInfo;
    private ImageView mImgPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mTextInfo = (TextView) findViewById(R.id.text_info);
        mImgPic = (ImageView)findViewById(R.id.img_show);
        mImgPic.setImageResource(R.mipmap.timg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallBack);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            updateText("连接状态：" + status);
        }
    };

    private void updateText(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextInfo.setText(text);
            }
        });
    }

    public void onClick(View view){
//        procSrc2Gray();
        testlk();
    }

    public void procSrc2Gray(){
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        Bitmap grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(srcBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
        mImgPic.setImageBitmap(grayBitmap);
        Log.i(TAG, "procSrc2Gray sucess...");
    }

    private void testlk(){
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        Bitmap grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Mat srcMat = new Mat();
        Mat hierarchy = new Mat();
        Mat grayMat = new Mat();
        List<MatOfPoint> lstContours = new ArrayList<>();
        Utils.bitmapToMat(srcBitmap, srcMat);
        Imgproc.findContours(srcMat,lstContours, hierarchy, Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(grayMat, lstContours, 0, new Scalar(0));
        Utils.matToBitmap(grayMat, grayBitmap);
        mImgPic.setImageBitmap(grayBitmap);
    }
}
