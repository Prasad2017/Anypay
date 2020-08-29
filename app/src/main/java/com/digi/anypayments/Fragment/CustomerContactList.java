package com.digi.anypayments.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.digi.anypayments.Activity.MainPage;
import com.digi.anypayments.Adapter.ContactAdapter;
import com.digi.anypayments.Model.ContactResponse;
import com.digi.anypayments.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomerContactList extends Fragment {

    View view;
    @BindView(R.id.contactRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.searchContact)
    FormEditText searchContact;
    List<ContactResponse> contactResponseList =new ArrayList<>();
    List<ContactResponse> searchContactResponseList =new ArrayList<>();
    ContactAdapter contactAdapter;
    @BindView(R.id.contactLayout)
    LinearLayout contactLayout;
    @BindView(R.id.allContactLayout)
    LinearLayout allContactLayout;
    @BindView(R.id.customerMobile)
    TextView customerMobile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_contact_list, container, false);
        ButterKnife.bind(this, view);
        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));

        searchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    if (editable.toString().length()>0) {
                        searchContactList(editable.toString());
                    }else {
                        getContactList();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;

    }

    @OnClick({R.id.contactLayout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.contactLayout:

                ContactAdapter.customerName = "New Number";
                ContactAdapter.customerNumber = customerMobile.getText().toString();
                Log.e("customerNumber", ""+ContactAdapter.customerNumber);
                ((MainPage) getActivity()).loadFragment(new CustomerRechargeMobile(), true);

                break;
        }
    }

    private void searchContactList(String contact) {

        searchContactResponseList = new ArrayList<>();
        Log.e("Contact", ""+contact);
        if (!contact.isEmpty()) {

            if (contact.length() > 0) {
                for (int i = 0; i < contactResponseList.size(); i++)
                    if ((contactResponseList.get(i).getCustomerName() + contactResponseList.get(i).getCustomerNumber()).toLowerCase().contains(contact.toLowerCase().trim())) {
                        searchContactResponseList.add(contactResponseList.get(i));
                    }
                if (searchContactResponseList.size() < 1) {
                    contactLayout.setVisibility(View.VISIBLE);
                    allContactLayout.setVisibility(View.GONE);
                    customerMobile.setText(contact);
                } else {
                    contactLayout.setVisibility(View.GONE);
                    allContactLayout.setVisibility(View.VISIBLE);
                }

            } else {
                searchContactResponseList = new ArrayList<>();
                contactLayout.setVisibility(View.GONE);
                allContactLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < contactResponseList.size(); i++) {
                    searchContactResponseList.add(contactResponseList.get(i));
                }

            }
        } else {
            contactLayout.setVisibility(View.VISIBLE);
            allContactLayout.setVisibility(View.GONE);
        }

        contactAdapter = new ContactAdapter(getActivity(), searchContactResponseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
        contactAdapter.notifyItemInserted(searchContactResponseList.size() - 1);
        recyclerView.setHasFixedSize(true);

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        requestPermission();

    }

    private void getContactList() {

        contactLayout.setVisibility(View.GONE);
        allContactLayout.setVisibility(View.VISIBLE);

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        Log.e("count", ""+phones.getCount());

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
                    contactResponse.setCustomerNumber(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    contactResponse.setCustomerNumber(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    contactResponseList.add(contactResponse);
                    Log.d("getContactsList", name + "---" + phoneNumber + " -- " + contactId + " -- " + photoUri);
                }
            }
        }
        phones.close();

        contactAdapter = new ContactAdapter(getActivity(), contactResponseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
        contactAdapter.notifyItemInserted(contactResponseList.size() - 1);
        recyclerView.setHasFixedSize(true);


    }

    private void requestPermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            getContactList();
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