package com.example.flow.displayClasses.AccountScreen;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.flow.displayClasses.ChangePassword.ChangePasswordFragment;
import com.google.zxing.EncodeHintType;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.scheme.VCard;
import com.example.flow.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.example.flow.displayClasses.AccountScreen.PreferenceFragmentAccount.*;



public class AccountFragment extends Fragment {

    private FragmentActivity myContext;
    private ViewPager mViewPager;
    private View RootView;

    private static final String TAG = AccountFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int TAB_PRIVATE = 1;
    public static final int TAB_CORP = 2;

    //For the image
    private ImageButton btn;
    private ImageView imageview;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //image
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(myContext);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(myContext,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
    private void  requestMultiplePermissions(){
        Dexter.withActivity(myContext)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getActivity().getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
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
                        Toast.makeText(getActivity().getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(myContext.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(myContext, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(myContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(myContext, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    //End image

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    public static void newInstance(int sectionNumber) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_account, container, false);

        Log.i(TAG, "onCreateView()");

        // Set up the ViewPager with the sections adapter.
        mViewPager = RootView.findViewById(R.id.container);


        //BUTTON
        Button openSettingsButton = RootView.findViewById(R.id.open_settings);

        openSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePasswordFragment termsFragment = new ChangePasswordFragment();
                FragmentManager manager =myContext.getSupportFragmentManager();
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                        .replace(R.id.relativelayout_for_fragment,
                                termsFragment,
                                termsFragment.getTag()
                        ).commit();


                Fragment preferenceFragmentAccount = new PreferenceFragmentAccount();
                FragmentTransaction ft =myContext.getSupportFragmentManager().beginTransaction();
                ft.add(R.id.relativelayout_for_fragment, preferenceFragmentAccount);
                ft.commit();

            }
        });

        //image
        requestMultiplePermissions();

        btn = RootView.findViewById(R.id.btn_account);
        imageview = RootView.findViewById(R.id.iv_account);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        return RootView;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
        View view = getView();
        if (view == null) {
            Log.e(TAG, "view is null!");
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username=null;
        String email=null;
        String firstname=null;
        String lastname=null;
        String phoneNumber=null;

            username = sharedPreferences.getString(USERNAME, null);
            email = sharedPreferences.getString(EMAIL, null);
            firstname = sharedPreferences.getString(FIRSTNAME, null);
            lastname = sharedPreferences.getString(LASTNAME, null);
            phoneNumber = sharedPreferences.getString(PHONE, null);


        ImageView qrCode = view.findViewById(R.id.qr);
        qrCode.setImageDrawable(getResources().getDrawable(R.drawable.ic_hourglass_full_teal_24dp));

        if (TextUtils.isEmpty(username)) {
            username="fghjk";
            //username = getString(R.string.no_name);
        }

        VCard vCard = new VCard(username)
                .setEmail(email)
                .setName(firstname + lastname)
                .setPhoneNumber(phoneNumber);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        view.getHeight();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int qrSize = (int) (0.3 * (double) Math.min(size.x, size.y));

        setTextToTextViewOrHide(username, R.id.name, view);
        setTextToTextViewOrHide(email, R.id.email, view);
        setTextToTextViewOrHide(firstname, R.id.firstname, view);
        setTextToTextViewOrHide(lastname, R.id.lastname, view);
        setTextToTextViewOrHide(phoneNumber, R.id.phoneNumber, view);

        new SetQrCodeTask().execute(new SetQrCodeTaskBundle(vCard, qrSize));




    }

    private void setTextToTextViewOrHide(String value, @IdRes int id, View view) {
        TextView textView = view.findViewById(id);
        if (TextUtils.isEmpty(value)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(value);
        }
    }

    private class SetQrCodeTaskBundle {
        private VCard mVCard;
        private int mQrSize;

        SetQrCodeTaskBundle(VCard vCard, int qrSize) {
            mVCard = vCard;
            mQrSize = qrSize;
        }

        VCard getVCard() {
            return mVCard;
        }

        int getQrSize() {
            return mQrSize;
        }
    }

    private class SetQrCodeTask extends AsyncTask<SetQrCodeTaskBundle, Void, Bitmap> {
        private final String TAG = SetQrCodeTask.class.getSimpleName();

        @Override
        protected Bitmap doInBackground(SetQrCodeTaskBundle... setQrCodeTaskBundles) {
            Log.d(TAG, "Generate QR code");
            return QRCode
                    .from(setQrCodeTaskBundles[0].getVCard())
                    .withColor(0xFF000000, 0x00000000)
                    .withSize(setQrCodeTaskBundles[0].getQrSize(), setQrCodeTaskBundles[0].getQrSize())
                    .withHint(EncodeHintType.CHARACTER_SET, "UTF-8")
                    .bitmap();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "Set QR code");
            ImageView qrCode = getView().findViewById(R.id.qr);
            qrCode.setImageBitmap(bitmap);
        }
    }

    }



