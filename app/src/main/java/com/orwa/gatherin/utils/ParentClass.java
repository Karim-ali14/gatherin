package com.orwa.gatherin.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.R;
import com.squareup.picasso.Picasso;
import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.vdx.designertoast.DesignerToast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ParentClass extends AppCompatActivity implements MainRequest {
    public static int paginate = 0;
    public static String lat;
    public static String lng;
    public static int time_end = 0;
    static boolean checked = true;
    protected static LocationManager locationManager;

    public static FragmentManager manager;
    public static SharedPrefManager sharedPrefManager;
    public static List<String> fragments = new ArrayList<String>();
    public static MainRequest mainRequest;

    public List<String> list_names;
    public List<Integer> list_idss;
    //    public List<SpinnerData> spinner_list;
    public static Typeface typeface_bold;
    public static Dialog spinKit;
    public static ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    public static android.app.FragmentManager manager1;

    public static List<Integer> imageList = new ArrayList<Integer>();
    public static FlipProgressDialog fpd;

    private static final int REQUEST_LOCATION = 1;
    public static String latitude = "", longitude = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParentClass.getLang(this).equals("ar")) {
            ParentClass.setDefaultLang("ar",this);
        } else {
            ParentClass.setDefaultLang("en",this);
        }
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(getLang(this)));
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

        sharedPreferences = getSharedPreferences("title",MODE_PRIVATE);
        imageList.add(R.drawable.logo);
        manager1 = getFragmentManager();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sharedPrefManager = new SharedPrefManager(this);
        manager = getSupportFragmentManager();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


    }


//    public static Bitmap resizeImage(Bitmap originalImage,float maxImageSize,
//                                     boolean filter) {
//        float ratio = Math.min(
//                (float) maxImageSize / originalImage.getWidth(),
//                (float) maxImageSize / originalImage.getHeight());
//        int width = Math.round((float) ratio * originalImage.getWidth());
//        int height = Math.round((float) ratio * originalImage.getHeight());
//
//        Bitmap newBitmap = Bitmap.createScaledBitmap(originalImage,width,
//                height,filter);
//        return newBitmap;
//    }

//    /**
//     * check network availability
//     */
//    private static boolean isConnected(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager)
//                context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo nInfo = connectivity.getActiveNetworkInfo();
//            if (nInfo != null && nInfo.getState() == NetworkInfo.State.CONNECTED) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean isEmailValid(String email) {
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }

//    // display current data in a TextView
//    public static void setCurrentDate(TextView tvDisplayDate) {
//        Calendar calendar = Calendar.getInstance();
//        tvDisplayDate.setText(AppUtils.formatDate(calendar.getTime()));
//    }
//
//    // get current data 05-02-2018
//    public static String getCurrentDate() {
//        Calendar calendar = Calendar.getInstance();
//        return AppUtils.formatDate2(calendar.getTime());
//    }
//    /**
//     * show SnackBar Message
//     */
//    public static void showSnackBarMessage(View view, String message) {
//        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
//    }

    public void languageConfiguration(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_out_right);
    }

//    public boolean check_empty(EditText check) {
//        if (TextUtils.isEmpty(check.getText().toString())) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permisson","Permission is granted");
                return true;
            } else {

                Log.v("permisson","Permission is revoked");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permisson","Permission is granted");
            return true;
        }
    }

    public boolean isStoargePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permisson","Permission is granted");
                return true;
            } else {

                Log.v("permisson","Permission is revoked");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permisson","Permission is granted");
            return true;
        }
    }

    public String getRealPathFromUri(Context context,Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri,proj,null,null,null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void imageBrowse(int PICK_IMAGE_REQUEST) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST);
    }

    public static void replaceFragmentInside(Fragment fragment) {
        manager.beginTransaction().add(android.R.id.content,fragment).setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit).addToBackStack(null).commit();
    }


    public static void replaceFragment(Fragment fragment,FrameLayout frameLayout) {
        String backStateName = fragment.getClass().getName();
        Log.e("backStateName",backStateName);

        //fragment not in back stack, create it.
        FragmentTransaction ft = manager.beginTransaction();
        if (!fragments.contains(backStateName)) {
            Log.e("check","added");
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_out_right,R.anim.enter_from_right,R.anim.exit_out_left);
            ft.replace(frameLayout.getId(),fragment);
            ft.addToBackStack(backStateName);
            ft.commit();

            fragments.add(backStateName);
            System.out.println("backStateName" + fragments);
        } else {
            try {
//                if (HomeActivity.type.equals("privilages")) {
//                    HomeActivity.type = "about";
//                } else if (HomeActivity.type.equals("about")) {
//                    HomeActivity.type = "privilages";
//                }
            } catch (Exception e) {

            }

            Log.e("check","not_added");
            ft.replace(frameLayout.getId(),fragment);
            ft.commit();

        }
    }


    public static void performNoBackStackTransaction(final FragmentManager fragmentManager,String tag,Fragment fragment) {

    }

    public static void paginate(int dy,int visibleItemCount,int totalItemCount,int pastVisiblesItems,
                                GridLayoutManager gridLayoutManager,RecyclerView recyclerView,boolean loading) {
        if (dy > 0) //check for scroll down
        {
            visibleItemCount = gridLayoutManager.getChildCount();
            totalItemCount = gridLayoutManager.getItemCount();
            pastVisiblesItems = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//                    LogFragment.e("LOOOAAAD", nextUrl);
            if (loading) {
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loading = false;
                    Log.e("...","Last Item Wow !");
                    paginate = 1;
                } else {
                    paginate = 0;
                }
            } else {
                paginate = 0;
            }
        }
    }


    public static void makeToast(Context context,int msg) {
        Toast.makeText(context,context.getResources().getString(msg),Toast.LENGTH_SHORT).show();
    }

    public static void makeToast(Context context,String msg) {


        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }


    public static int colour_converter(String hexa_Color) {
        return Color.parseColor(hexa_Color);
    }

    public static void Peform_log(String log_name,String log_msg) {
        Log.e(log_name,log_msg);
    }

    public void time_handler(int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                time_end = 1;
            }
        },time);
    }

    public void configuration_for_language(String language) {
        int currentOrientation = this.getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());
            overridePendingTransition(R.anim.enter_from_left,R.anim.exit_out_right);

        }

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());
            overridePendingTransition(R.anim.enter_from_left,R.anim.exit_out_right);
        }
    }

//    public static void date(View v, final TextView text) {
//        Calendar calendar = Calendar.getInstance();
//        int y = calendar.get(Calendar.YEAR);
//        int m = calendar.get(Calendar.MONTH);
//        int d = calendar.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog dialog = new DatePickerDialog(v.getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                String date_from = year + "-" + (month + 1) + "-" + day;
//                text.setText(date_from);
//            }
//        }, y, m, d);
//        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        dialog.show();
//
//    }


    public static void handleException(Context context,Throwable t) {
        if (t instanceof SocketTimeoutException)
            makeErrorToast(context,"خطأ فى الانترنت");
        else if (t instanceof UnknownHostException)
            makeErrorToast(context,"خطأ فى الاتصال");

        else if (t instanceof ConnectException)
            makeErrorToast(context,"خطأ فى الاتصال");
        else
            makeErrorToast(context,t.getLocalizedMessage());

    }

    public void dismiss_keyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public Bitmap roundCornerImage(Bitmap raw,float round) {
        int width = raw.getWidth();
        int height = raw.getHeight();
        Bitmap result = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0,0,0,0);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#000000"));

        final Rect rect = new Rect(0,0,width,height);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF,round,round,paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        canvas.drawBitmap(raw,rect,rect,paint);

        return result;
    }

    public static void makeSuccessToast(Context context,String msg) {

        DesignerToast.Success(context,context.getString(R.string.app_name),msg,Gravity.TOP,Toast.LENGTH_LONG,DesignerToast.STYLE_DARK);

//        KToast.customBackgroudToast((Activity) context, msg, Gravity.TOP, KToast.LENGTH_AUTO, R.drawable.background_toast, null, R.drawable.ic_infinite_white);
    }

    public static void makeErrorToast(Context context,String msg) {
        DesignerToast.Error(context,context.getString(R.string.app_name),msg,Gravity.TOP,Toast.LENGTH_LONG,DesignerToast.STYLE_DARK);

//        KToast.customBackgroudToast((Activity) context, msg, Gravity.TOP, KToast.LENGTH_AUTO, R.drawable.background_error_toast, null, R.drawable.error);
    }


    public static void showdialog() {
//        hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel(please_waiting)
//                .setCancellable(true)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();
    }

    public static void dismis_dialog() {
//        hud.dismiss();
        spinKit.dismiss();

    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    public static void storeLang(String ln,Context context) {
        SharedPreferences settings = context.getSharedPreferences("language",
                0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("language",ln);
        editor.commit();
    }

    public static String getLang(Context context) {
        String value = "ar";
        final SharedPreferences prefs = context.getSharedPreferences(
                "language",0);
        if (!prefs.getString("language","language").equals("language")) {
            return prefs.getString("language","language");
        }
        return value;
    }

    public static void setDefaultLang(String ln,Context context) {
        Resources res = context.getResources();
        Locale locale = new Locale(ln);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        res.updateConfiguration(config,res.getDisplayMetrics());
        storeLang(ln,context);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

    private void setLocale() {
        Locale locale = new Locale(getLang(this));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }


    public static void LoadImageWithPicasso(String url,Context context,ImageView imageView) {
        if (!url.equals("")) {
            Picasso.get().load(url).error(R.drawable.default_image).into(imageView);
        } else {
            Picasso.get().load(R.drawable.default_image).error(R.drawable.default_image).into(imageView);
        }
    }

    public static void showFlipDialog() {
        fpd = new FlipProgressDialog();

        fpd.setImageList(imageList);                              // *Set a imageList* [Have to. Transparent background png recommended]
        fpd.setCanceledOnTouchOutside(false);                      // If true, the dialog will be dismissed when user touch outside of the dialog. If false, the dialog won't be dismissed.
        fpd.setDimAmount(0.3f);                                   // Set a dim (How much dark outside of dialog)

// About dialog shape, color
        fpd.setBackgroundColor(Color.parseColor("#FFFFFF"));      // Set a background color of dialog
        fpd.setBackgroundAlpha(1f);                             // Set a alpha color of dialog
        fpd.setBorderStroke(0);                                   // Set a width of border stroke of dialog
        fpd.setBorderColor(-1);                                   // Set a border stroke color of dialog
        fpd.setCornerRadius(16);                                  // Set a corner radius

// About image
        fpd.setImageSize(200);                                    // Set an image size
        fpd.setImageMargin(10);                                   // Set a margin of image

// About rotation
        fpd.setOrientation("rotationY");                          // Set a flipping rotation
        fpd.setDuration(600);                                     // Set a duration time of flipping ratation
        fpd.setStartAngle(0.0f);                                  // Set an angle when flipping ratation start
        fpd.setEndAngle(180.0f);                                  // Set an angle when flipping ratation end
        fpd.setMinAlpha(0.0f);                                    // Set an alpha when flipping ratation start and end
        fpd.setMaxAlpha(1.0f);
        try {
            fpd.show(manager1,"");                        // Show flip-progress-dialg

        } catch (Exception e) {

        }
    }

    public static void dismissFlipDialog() {
        try {
            fpd.dismiss();                                            // Dismiss flip-progress-dialg
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager1 = getFragmentManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager1 = getFragmentManager();

    }

    public static String getCountryName(Context context,double latitude,double longitude) {
        Geocoder geocoder = new Geocoder(context,Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);

            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.e("address",address + "GOOD");
                Log.e("city",city + "GOOD");
                Log.e("state",state + "GOOD");
                Log.e("country",country + "GOOD");
                Log.e("postalCode",postalCode + "GOOD");
                Log.e("knownName",knownName + "GOOD");

                if (knownName != null && !knownName.matches("\\d+(?:\\.\\d+)?")) {
                    return knownName;
                } else if (city != null) {
                    return city;
                } else {
                    return address;
                }
            }

            return null;
        } catch (Exception ignored) {
            Log.e("ignored",ignored.toString());
            return "";
        }
    }

    public static double distance(double lat1,double lon1,double lat2,double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static void setWindowFlag(Activity activity,final int bits,boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public static int calculateNoOfColumns(Context context,int width) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / width);
        // Where 180 is the width of your grid item. You can change it as per your convention.
        return noOfColumns;
    }

    public static void getLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Log.e("latitude1",latitude);
                Log.e("longitude1",longitude);
//                Toast.makeText(context, context.getString(R.string.gotYourLocation), Toast.LENGTH_SHORT).show();

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Log.e("latitude2",latitude);
                Log.e("longitude2",longitude);
//                Toast.makeText(context, context.getString(R.string.gotYourLocation), Toast.LENGTH_SHORT).show();
            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Log.e("latitude3",latitude);
                Log.e("longitude3",longitude);
//                Toast.makeText(context, context.getString(R.string.gotYourLocation), Toast.LENGTH_SHORT).show();
            } else {
            }
        }
    }


    @Override
    public void startLoading() {
        fpd = new FlipProgressDialog();

        fpd.setImageList(imageList);                              // *Set a imageList* [Have to. Transparent background png recommended]
        fpd.setCanceledOnTouchOutside(false);                      // If true, the dialog will be dismissed when user touch outside of the dialog. If false, the dialog won't be dismissed.
        fpd.setDimAmount(0.3f);                                   // Set a dim (How much dark outside of dialog)

// About dialog shape, color
        fpd.setBackgroundColor(Color.parseColor("#FFFFFF"));      // Set a background color of dialog
        fpd.setBackgroundAlpha(1f);                             // Set a alpha color of dialog
        fpd.setBorderStroke(0);                                   // Set a width of border stroke of dialog
        fpd.setBorderColor(-1);                                   // Set a border stroke color of dialog
        fpd.setCornerRadius(16);                                  // Set a corner radius

// About image
        fpd.setImageSize(200);                                    // Set an image size
        fpd.setImageMargin(10);                                   // Set a margin of image

// About rotation
        fpd.setOrientation("rotationY");                          // Set a flipping rotation
        fpd.setDuration(600);                                     // Set a duration time of flipping ratation
        fpd.setStartAngle(0.0f);                                  // Set an angle when flipping ratation start
        fpd.setEndAngle(180.0f);                                  // Set an angle when flipping ratation end
        fpd.setMinAlpha(0.0f);                                    // Set an alpha when flipping ratation start and end
        fpd.setMaxAlpha(1.0f);
        try {
            fpd.show(manager1,"");                        // Show flip-progress-dialg

        } catch (Exception e) {

        }
    }

    @Override
    public void stopLoading() {
        try {
            fpd.dismiss();                                            // Dismiss flip-progress-dialg
        } catch (Exception e) {

        }
    }


    @Override
    public void errorToast(Context context,String msg) {
        makeErrorToast(context,msg);
    }

    @Override
    public void successToast(Context context,String msg) {
        makeSuccessToast(context,msg);

    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleApiException(Context context,Throwable t) {
        handleException(context,t);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
