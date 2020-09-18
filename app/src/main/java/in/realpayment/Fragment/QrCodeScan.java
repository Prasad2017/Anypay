package in.realpayment.Fragment;

import android.content.Intent;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.realpayment.Activity.MainPage;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import info.androidhive.barcode.BarcodeReader;


public class QrCodeScan extends Fragment {

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qr_code_scan, container, false);
        ButterKnife.bind(this, view);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        MainPage.title.setText("");

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.e("result==", ""+result);
        if (result != null) {
            if (result.getContents() == null) {

                /*View view = getLayoutInflater().inflate(R.layout.layout_qr_code_sheet, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(view);

                TextView scanAgain = view.findViewById(R.id.scanAgain);

                scanAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

                        IntentIntegrator integrator = new IntentIntegrator(getActivity());
                        integrator.setPrompt("Scan & Pay");
                        integrator.setOrientationLocked(false);
                        integrator.setBeepEnabled(false);
                        integrator.forSupportFragment(QrCodeScan.this).initiateScan(IntentIntegrator.ALL_CODE_TYPES);

                    }
                });*/

                Toasty.normal(getActivity(), "Canceled", Toasty.LENGTH_SHORT).show();
                ((MainPage) getActivity()).loadFragment(new Home(), false);

            } else {

                try {

                    //converting the data to json

                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textViews
                    Log.e("Name", ""+obj.getString("userName"));
                    Log.e("Id", ""+obj.getString("userId"));
                    Log.e("Number", ""+obj.getString("userNumber"));

                    QRCodePayment qrCodePayment = new QRCodePayment();
                    Bundle bundle = new Bundle();
                    bundle.putString("receiverName", obj.getString("userName"));
                    bundle.putString("receiverId", obj.getString("userId"));
                    bundle.putString("receiverNumber", obj.getString("userNumber"));
                    qrCodePayment.setArguments(bundle);
                    ((MainPage) getActivity()).loadFragment(qrCodePayment, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {

            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setPrompt("Scan & Pay");
            integrator.setOrientationLocked(false);
            integrator.setBeepEnabled(false);
            integrator.forSupportFragment(QrCodeScan.this).initiateScan(IntentIntegrator.ALL_CODE_TYPES);

        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

}