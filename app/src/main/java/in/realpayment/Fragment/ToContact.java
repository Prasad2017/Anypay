package in.realpayment.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.realpayment.Activity.MainPage;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.ContactListResponse;
import in.realpayment.Model.ContactResponse;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ToContact extends Fragment {


    View view;
    @BindView(R.id.contactRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.searchContact)
    FormEditText searchContact;
    @BindView(R.id.contactLayout)
    LinearLayout contactLayout;
    @BindView(R.id.allContactLayout)
    LinearLayout allContactLayout;
    @BindView(R.id.customerMobile)
    TextView customerMobile;
    List<ContactResponse> contactResponseList =new ArrayList<>();
    List<ContactResponse> toRealPaymentContactResponseList =new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_to_contact, container, false);
        ButterKnife.bind(this, view);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        MainPage.title.setText("");

        return view;

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            requestPermission();
        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void requestPermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            getContactList();
                            getRealPaymentList();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void getRealPaymentList() {

        if (contactResponseList.size()>0) {

            for (int j = 0; j< contactResponseList.size(); j++){

                final int i= j;

                Map<String, String> stringStringMap = new HashMap<>();
                stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));

                Call<ContactListResponse> call = Api.getClient().getRealPaymentList(stringStringMap, "1", "100000", contactResponseList.get(i).getCustomerNumber().trim());
                call.enqueue(new Callback<ContactListResponse>() {
                    @Override
                    public void onResponse(Call<ContactListResponse> call, Response<ContactListResponse> response) {

                        if (response.isSuccessful()){

                            if (response.body().getSuccess().booleanValue()==true) {

                                ContactResponse contactResponse = new ContactResponse();
                                contactResponse.setCustomerName(contactResponseList.get(i).getCustomerName());
                                contactResponse.setCustomerNumber(contactResponseList.get(i).getCustomerNumber().trim().replace("+91", ""));
                                contactResponse.setCustomerStatus("1");

                                toRealPaymentContactResponseList.add(contactResponseList.get(i));
                            } else {

                                ContactResponse contactResponse = new ContactResponse();
                                contactResponse.setCustomerName(contactResponseList.get(i).getCustomerName());
                                contactResponse.setCustomerNumber(contactResponseList.get(i).getCustomerNumber().trim().replace("+91", ""));
                                contactResponse.setCustomerStatus("0");

                                toRealPaymentContactResponseList.add(contactResponseList.get(i));

                                Log.e("RPContactList", ""+ toRealPaymentContactResponseList.size());

                            }

                        } else {

                            ContactResponse contactResponse = new ContactResponse();
                            contactResponse.setCustomerName(contactResponseList.get(i).getCustomerName());
                            contactResponse.setCustomerNumber(contactResponseList.get(i).getCustomerNumber().trim().replace("+91", ""));
                            contactResponse.setCustomerStatus("0");

                            toRealPaymentContactResponseList.add(contactResponseList.get(i));

                        }

                    }

                    @Override
                    public void onFailure(Call<ContactListResponse> call, Throwable t) {
                        Log.e("realPayError", ""+t.getMessage());

                        ContactResponse contactResponse = new ContactResponse();
                        contactResponse.setCustomerName(contactResponseList.get(i).getCustomerName());
                        contactResponse.setCustomerNumber(contactResponseList.get(i).getCustomerNumber().trim().replace("+91", ""));
                        contactResponse.setCustomerStatus("0");

                        toRealPaymentContactResponseList.add(contactResponseList.get(i));

                    }
                });

            }

        } else {


        }

        Log.e("contactList", ""+ contactResponseList.size());
        Log.e("RPContactList", ""+ toRealPaymentContactResponseList.size());

    }

    private void getContactList() {

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        String lastPhoneName = " ";

        if (phones.getCount() > 0) {
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactId = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                String photoUri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (!name.equalsIgnoreCase(lastPhoneName)) {
                    lastPhoneName = name;
                    ContactResponse contactResponse = new ContactResponse();
                    contactResponse.setCustomerName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    contactResponse.setCustomerNumber(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim().replace("+91", ""));
                    contactResponseList.add(contactResponse);
                    Log.d("getContactsList", name + "---" + phoneNumber + " -- " + contactId + " -- " + photoUri);

                }
            }
        }

        phones.close();

    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


}