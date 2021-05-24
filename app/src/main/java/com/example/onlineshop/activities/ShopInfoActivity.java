package com.example.onlineshop.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.onlineshop.Constants;
import com.example.onlineshop.R;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
import com.kroegerama.imgpicker.ButtonType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShopInfoActivity extends AppCompatActivity implements BottomSheetImagePicker.OnImagesSelectedListener {
    private SmartMaterialSpinner spCity;
    private List<String> cityList;
    private ProgressDialog progressDialog;
    private EditText etName, etLink;
    private Button btnSubmit;
    private String city = null;
    private File file;
    private ImageView ivLogoShop, ivAdd;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);

        spCity = findViewById(R.id.sp_city);
        etName = findViewById(R.id.et_name);
        etLink = findViewById(R.id.et_link);
        btnSubmit = findViewById(R.id.btn_submit);
        ivLogoShop = findViewById(R.id.iv_logo_shop);
        ivAdd = findViewById(R.id.iv_add);

        progressDialog = new ProgressDialog(this);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                city = cityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String link = etLink.getText().toString().trim();

                boolean isEmpty = false;

                if (name.isEmpty()) {
                    isEmpty = true;
                    etName.setError("Required");
                }

                if (link.isEmpty()) {
                    isEmpty = true;
                    etLink.setError("Required");
                }

                if (city == null) {
                    Toast.makeText(ShopInfoActivity.this, "Required address", Toast.LENGTH_SHORT).show();
                }

                if (!isEmpty && city != null) {
                    progressDialog.setTitle("Loading...");
                    progressDialog.show();

                    SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
                    String tokenUser = sp.getString("token_user", "");

                    if (file == null) {
                        AndroidNetworking.put(Constants.API + "/shops/" + id)
                                .addHeaders("Authorization", "Bearer " + tokenUser)
                                .addBodyParameter("name", name)
                                .addBodyParameter("link", link)
                                .addBodyParameter("city", city)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String status = response.getString("status");

                                            if (status.equals("success")) {
                                                Intent intent = new Intent(ShopInfoActivity.this, SettingActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Toast.makeText(ShopInfoActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        if (anError.getErrorCode() != 0) {
                                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                                        } else {
                                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                                        }
                                    }
                                });
                    } else {
                        AndroidNetworking.upload(Constants.API + "/shops/" + id + "?_method=PUT")
                                .addHeaders("Authorization", "Bearer " + tokenUser)
                                .addMultipartFile("logo", file)
                                .addMultipartParameter("name", name)
                                .addMultipartParameter("link", link)
                                .addMultipartParameter("city", city)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String status = response.getString("status");

                                            if (status.equals("success")) {
                                                Intent intent = new Intent(ShopInfoActivity.this, SettingActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Toast.makeText(ShopInfoActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        if (anError.getErrorCode() != 0) {
                                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                                        } else {
                                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                                        }
                                    }
                                });
                    }
                }
            }
        });

        getShop();
    }

    public void getCity() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        cityList = new ArrayList<>();

        AndroidNetworking.get("https://api.rajaongkir.com/starter/city")
                .addHeaders("key", "25c806b53d47f38a8327d29cda61b0df")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rajaongkir = response.getJSONObject("rajaongkir");
                            JSONArray results = rajaongkir.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject item = results.getJSONObject(i);
                                cityList.add(item.getString("city_name"));
                            }

                            spCity.setItem(cityList);
                            for (int i = 0; i < cityList.size(); i++) {
                                String name = cityList.get(i);
                                if (name.equals(city)) {
                                    spCity.setSelection(i);
                                }
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ShopInfoActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        if (anError.getErrorCode() != 0) {
                            Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        } else {
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    }
                });
    }

    private void getShop() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        SharedPreferences sp = getSharedPreferences("online_shop", MODE_PRIVATE);
        String tokenShop = sp.getString("token_shop", "");

        AndroidNetworking.get(Constants.API + "/auth-decode")
                .addHeaders("Authorization", "Bearer " + tokenShop)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            id = response.getString("id");
                            String name = response.getString("name");
                            city = response.getString("city");
                            String link = response.getString("link");
                            String logo = response.getString("logo");

                            etName.setText(name);
                            etLink.setText(link);

                            if (!logo.equals("null")) {
                                ivLogoShop.setBackground(null);
                                ivAdd.setVisibility(View.GONE);

                                Glide.with(ShopInfoActivity.this)
                                        .load(Constants.STORAGE + logo)
                                        .into(ivLogoShop);
                            }

                            getCity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401) {
                        } else {
                            Toast.makeText(ShopInfoActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            if (anError.getErrorCode() != 0) {
                                Log.d("TAG", "onError errorCode : " + anError.getErrorCode());
                                Log.d("TAG", "onError errorBody : " + anError.getErrorBody());
                                Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                            } else {
                                Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                            }
                        }
                    }
                });
    }

    public void imagePicker(View view) {
        new BottomSheetImagePicker.Builder(getString(R.string.file_provider))
                .cameraButton(ButtonType.Button)
                .galleryButton(ButtonType.Button)
                .singleSelectTitle(R.string.pick_single)
                .requestTag("single")
                .show(getSupportFragmentManager(), null);
    }

    @Override
    public void onImagesSelected(List<? extends Uri> list, String s) {
        for (Uri uri : list) {
            Glide.with(this).load(uri).into(ivLogoShop);
            file = new File(getUriRealPath(this, uri));
            ivLogoShop.setBackground(null);
            ivAdd.setVisibility(View.GONE);
        }
    }

    /*
   This method can parse out the real local file path from a file URI.
   */
    private String getUriRealPath(Context ctx, Uri uri) {
        String ret = "";
        if (isAboveKitKat()) {
            // Android sdk version number bigger than 19.
            ret = getUriRealPathAboveKitkat(ctx, uri);
        } else {
            // Android sdk version number smaller than 19.
            ret = getImageRealPath(getContentResolver(), uri, null);
        }
        return ret;
    }

    /*
    This method will parse out the real local file path from the file content URI.
    The method is only applied to android sdk version number that is bigger than 19.
    */
    private String getUriRealPathAboveKitkat(Context ctx, Uri uri) {
        String ret = "";
        if (ctx != null && uri != null) {
            if (isContentUri(uri)) {
                if (isGooglePhotoDoc(uri.getAuthority())) {
                    ret = uri.getLastPathSegment();
                } else {
                    ret = getImageRealPath(getContentResolver(), uri, null);
                }
            } else if (isFileUri(uri)) {
                ret = uri.getPath();
            } else if (isDocumentUri(ctx, uri)) {
                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);
                // Get uri authority.
                String uriAuthority = uri.getAuthority();
                if (isMediaDoc(uriAuthority)) {
                    String idArr[] = documentId.split(":");
                    if (idArr.length == 2) {
                        // First item is document type.
                        String docType = idArr[0];
                        // Second item is document real id.
                        String realDocId = idArr[1];
                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if ("image".equals(docType)) {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(docType)) {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(docType)) {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;
                        ret = getImageRealPath(getContentResolver(), mediaContentUri, whereClause);
                    }
                } else if (isDownloadDoc(uriAuthority)) {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");
                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));
                    ret = getImageRealPath(getContentResolver(), downloadUriAppendId, null);
                } else if (isExternalStoreDoc(uriAuthority)) {
                    String idArr[] = documentId.split(":");
                    if (idArr.length == 2) {
                        String type = idArr[0];
                        String realDocId = idArr[1];
                        if ("primary".equalsIgnoreCase(type)) {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }
        return ret;
    }

    /* Check whether current android os version is bigger than kitkat or not. */
    private boolean isAboveKitKat() {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    private boolean isDocumentUri(Context ctx, Uri uri) {
        boolean ret = false;
        if (ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }

    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private boolean isContentUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("content".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private boolean isFileUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("file".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this document is provided by ExternalStorageProvider. Return true means the file is saved in external storage. */
    private boolean isExternalStoreDoc(String uriAuthority) {
        boolean ret = false;
        if ("com.android.externalstorage.documents".equals(uriAuthority)) {
            ret = true;
        }
        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. return true means this file is a downloaed file. */
    private boolean isDownloadDoc(String uriAuthority) {
        boolean ret = false;
        if ("com.android.providers.downloads.documents".equals(uriAuthority)) {
            ret = true;
        }
        return ret;
    }

    /*
    Check if MediaProvider provide this document, if true means this image is created in android media app.
    */
    private boolean isMediaDoc(String uriAuthority) {
        boolean ret = false;
        if ("com.android.providers.media.documents".equals(uriAuthority)) {
            ret = true;
        }
        return ret;
    }

    /*
    Check whether google photos provide this document, if true means this image is created in google photos app.
    */
    private boolean isGooglePhotoDoc(String uriAuthority) {
        boolean ret = false;
        if ("com.google.android.apps.photos.content".equals(uriAuthority)) {
            ret = true;
        }
        return ret;
    }

    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";
        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);
        if (cursor != null) {
            boolean moveToFirst = cursor.moveToFirst();
            if (moveToFirst) {
                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;
                if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA;
                } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA;
                } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA;
                }
                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);
                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }
        return ret;
    }
}