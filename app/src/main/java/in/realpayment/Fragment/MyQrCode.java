package in.realpayment.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import in.realpayment.Activity.MainPage;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.Profile;
import in.realpayment.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static android.content.Context.WINDOW_SERVICE;
import static android.graphics.Color.WHITE;

public class MyQrCode extends Fragment{

    View view;
    @BindView(R.id.qrCodeImage)
    ImageView qrCodeImage;
    @BindView(R.id.sendQRCodeImage)
    ImageView sendQRCodeImage;
    @BindView(R.id.upiId)
    TextView upiId;
    @BindView(R.id.upiName)
    TextView upiName;
    @BindView(R.id.sendUpiId)
    TextView sendUpiId;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    QRGEncoder qrgEncoder;
    Bitmap bitmap, layoutBitmap;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/RealPayment/";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.my_qr_code, container, false);
        ButterKnife.bind(this, view);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        MainPage.title.setText("My QR Code");

        upiId.setText(""+MainPage.userNumber+"");
        sendUpiId.setText(""+MainPage.userNumber+"");
        upiName.setText(""+MainPage.userName+"");



        try{

            layoutBitmap = convertLayoutToImage();

        } catch (Exception e){
            e.printStackTrace();
        }

        return view;


    }

    private Bitmap convertLayoutToImage() {

        LinearLayout linearView = (LinearLayout) view.findViewById(R.id.linearLayout); //you can pass your xml layout

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        return linearView.getDrawingCache();// creates bitmap and returns the same

    }

    @OnClick({R.id.refresh, R.id.download, R.id.share})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.download:

                boolean save;
                String result;
                try {
                    save = QRGSaver.save(savePath, MainPage.userId, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    result = save ? "QRCode Saved" : "QRCode Not Saved";
                    Toasty.normal(getActivity(), result, Toasty.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.share:

                String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), layoutBitmap, "RealPayment QR Code", null);
                Uri bitmapUri = Uri.parse(bitmapPath);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                intent.putExtra(Intent.EXTRA_TEXT, "Scan this to through RealPayment");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Share"));

                break;


            case R.id.refresh:

                GenerateClick();

                break;
        }

    }


    public void GenerateClick(){

        try {
            //setting size of qr code
            int width =300;
            int height = 300;
            int smallestDimension = width < height ? width : height;

            //setting parameters for qr code
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("userId", MainPage.userId);
                jsonObject.put("userName", MainPage.userName);
                jsonObject.put("userNumber", MainPage.userNumber);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.e("Profile", ""+String.valueOf(jsonObject));

            CreateQRCode(String.valueOf(jsonObject), charset, hintMap, smallestDimension, smallestDimension);

        } catch (Exception ex) {
            Log.e("QrGenerate",ex.getMessage());
        }

    }

    public  void CreateQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth){

        try {
            //generating qr code in bitMatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitMatrix to bitmap

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    //pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                    pixels[offset + x] = matrix.get(x, y) ?
                            ResourcesCompat.getColor(getResources(),R.color.black,null) :WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view

            Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.logosmm);
            qrCodeImage.setImageBitmap(mergeBitmaps(overlay,bitmap));
            sendQRCodeImage.setImageBitmap(mergeBitmaps(overlay,bitmap));

        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
        }

    }

    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth  - overlay.getWidth()) /2;
        int centreY = (canvasHeight - overlay.getHeight()) /2 ;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            GenerateClick();
        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }
}
