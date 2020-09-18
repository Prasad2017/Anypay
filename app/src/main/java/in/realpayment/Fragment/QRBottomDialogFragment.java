package in.realpayment.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.realpayment.R;

public class QRBottomDialogFragment extends BottomSheetDialogFragment {

    public static QRBottomDialogFragment newInstance() {
        return new QRBottomDialogFragment();
    }

    public static TextView scanAgain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_qr_code_sheet, container, false);

        scanAgain = view.findViewById(R.id.scanAgain);

        // get the views and attach the listener

        return view;

    }

}
